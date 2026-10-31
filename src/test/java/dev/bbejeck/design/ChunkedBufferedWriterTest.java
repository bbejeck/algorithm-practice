package dev.bbejeck.design;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ChunkedBufferedWriter
 */
public class ChunkedBufferedWriterTest {

    // Records a copy of every chunk sent to the device
    private static final class RecordingDevice implements ChunkedBufferedWriter.Device {
        final List<List<Integer>> chunks = new ArrayList<>();
        final ByteArrayOutputStream all = new ByteArrayOutputStream();

        @Override
        public void writeToDevice(byte[] chunk) {
            List<Integer> c = new ArrayList<>();
            for (byte b : chunk) {
                c.add((int) b);
            }
            chunks.add(c);
            all.writeBytes(chunk);
        }
    }

    private static byte[] bytes(int... values) {
        byte[] result = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = (byte) values[i];
        }
        return result;
    }

    private static byte[] range(int from, int toInclusive) {
        byte[] result = new byte[toInclusive - from + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (from + i);
        }
        return result;
    }

    @Test
    @DisplayName("Example from the problem statement")
    void testExample() {
        RecordingDevice device = new RecordingDevice();
        ChunkedBufferedWriter w = new ChunkedBufferedWriter(device, 3, 4);
        w.write(bytes(1, 2));
        assertEquals(List.of(), device.chunks);
        w.write(bytes(3, 4, 5));
        assertEquals(List.of(List.of(1, 2, 3)), device.chunks);
        w.flush();
        assertEquals(List.of(List.of(1, 2, 3), List.of(4, 5)), device.chunks);
        w.write(range(6, 14));
        assertEquals(List.of(List.of(1, 2, 3), List.of(4, 5), List.of(6, 7, 8), List.of(9, 10, 11)), device.chunks);
        w.close();
        assertEquals(List.of(12, 13, 14), device.chunks.get(4));
        assertEquals(5, device.chunks.size());
    }

    @Test
    @DisplayName("Small writes are combined into one device call")
    void testCombining() {
        RecordingDevice device = new RecordingDevice();
        ChunkedBufferedWriter w = new ChunkedBufferedWriter(device, 10, 4);
        w.write(bytes(1));
        w.write(bytes(2));
        w.write(bytes(3));
        assertEquals(List.of(), device.chunks);
        w.write(bytes(4));
        assertEquals(List.of(List.of(1, 2, 3, 4)), device.chunks);
    }

    @Test
    @DisplayName("Bypass sends large writes straight through when the buffer is empty")
    void testBypassWithLargeM() {
        RecordingDevice device = new RecordingDevice();
        ChunkedBufferedWriter w = new ChunkedBufferedWriter(device, 10, 4);
        w.write(range(1, 10));
        assertEquals(List.of(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)), device.chunks);
    }

    @Test
    @DisplayName("Bypass resumes once a full buffer empties in the middle of a write")
    void testBypassAfterSpill() {
        RecordingDevice device = new RecordingDevice();
        ChunkedBufferedWriter w = new ChunkedBufferedWriter(device, 10, 4);
        w.write(bytes(1, 2));
        w.write(range(3, 10));
        // buffer fills at [1..4], spills all 4 (M >= B), then 6 >= B remain -> bypass
        assertEquals(List.of(List.of(1, 2, 3, 4), List.of(5, 6, 7, 8, 9, 10)), device.chunks);
    }

    @Test
    @DisplayName("Spills only min(M, buffered) from the front when M < B")
    void testPartialSpill() {
        RecordingDevice device = new RecordingDevice();
        ChunkedBufferedWriter w = new ChunkedBufferedWriter(device, 2, 5);
        w.write(bytes(1));
        w.write(range(2, 8));
        // [1..5] full -> send [1,2], buffer [3,4,5]; add 6,7 -> full -> send [3,4], buffer [5,6,7]; add 8
        assertEquals(List.of(List.of(1, 2), List.of(3, 4)), device.chunks);
        w.flush();
        assertEquals(List.of(List.of(1, 2), List.of(3, 4), List.of(5, 6), List.of(7, 8)), device.chunks);
    }

    @Test
    @DisplayName("Empty writes and flushes send nothing")
    void testEmpty() {
        RecordingDevice device = new RecordingDevice();
        ChunkedBufferedWriter w = new ChunkedBufferedWriter(device, 3, 3);
        w.write(new byte[0]);
        w.flush();
        w.flush();
        assertEquals(List.of(), device.chunks);
    }

    @Test
    @DisplayName("close flushes and rejects later use")
    void testClose() {
        RecordingDevice device = new RecordingDevice();
        ChunkedBufferedWriter w = new ChunkedBufferedWriter(device, 3, 8);
        w.write(range(1, 5));
        w.close();
        assertEquals(List.of(List.of(1, 2, 3), List.of(4, 5)), device.chunks);
        w.close();
        assertThrows(IllegalStateException.class, () -> w.write(bytes(1)));
        assertThrows(IllegalStateException.class, w::flush);
        assertEquals(2, device.chunks.size());
    }

    @Test
    @DisplayName("Invalid sizes")
    void testInvalid() {
        RecordingDevice device = new RecordingDevice();
        assertThrows(IllegalArgumentException.class, () -> new ChunkedBufferedWriter(device, 0, 4));
        assertThrows(IllegalArgumentException.class, () -> new ChunkedBufferedWriter(device, 4, 0));
    }

    @Test
    @DisplayName("Random writes match a reference simulation")
    void testRandom() {
        Random random = new Random(31);
        for (int round = 0; round < 300; round++) {
            int m = 1 + random.nextInt(6);
            int b = 1 + random.nextInt(6);
            RecordingDevice device = new RecordingDevice();
            ChunkedBufferedWriter w = new ChunkedBufferedWriter(device, m, b);
            List<List<Integer>> expected = new ArrayList<>();
            List<Integer> buffer = new ArrayList<>();
            ByteArrayOutputStream written = new ByteArrayOutputStream();
            int next = 0;
            for (int op = 0; op < 20; op++) {
                if (random.nextInt(5) == 0) {
                    w.flush();
                    drain(buffer, m, expected);
                } else {
                    byte[] data = new byte[random.nextInt(15)];
                    for (int i = 0; i < data.length; i++) {
                        data[i] = (byte) next++;
                    }
                    written.writeBytes(data);
                    w.write(data);
                    simulateWrite(data, m, b, buffer, expected);
                }
            }
            w.close();
            drain(buffer, m, expected);
            assertEquals(expected, device.chunks, "M=" + m + " B=" + b);
            assertArrayEquals(written.toByteArray(), device.all.toByteArray());
        }
    }

    @Test
    @DisplayName("Large writes with a small buffer run in linear time")
    void testPerformance() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            RecordingDevice device = new RecordingDevice();
            ChunkedBufferedWriter w = new ChunkedBufferedWriter(device, 1000, 100_000);
            byte[] data = new byte[150_000];
            Arrays.fill(data, (byte) 7);
            for (int i = 0; i < 20; i++) {
                w.write(Arrays.copyOf(data, 1 + i));
                w.write(data);
            }
            w.flush();
            assertEquals(20 * 150_000 + 210, device.all.size());
        });
    }

    // Straightforward simulation of the rules, used as the oracle
    private static void simulateWrite(byte[] data, int m, int b, List<Integer> buffer, List<List<Integer>> out) {
        int i = 0;
        while (i < data.length) {
            if (buffer.isEmpty() && data.length - i >= b) {
                while (data.length - i >= b) {
                    int c = Math.min(m, data.length - i);
                    List<Integer> chunk = new ArrayList<>();
                    for (int k = 0; k < c; k++) {
                        chunk.add((int) data[i + k]);
                    }
                    out.add(chunk);
                    i += c;
                }
            } else {
                buffer.add((int) data[i++]);
                if (buffer.size() == b) {
                    int c = Math.min(m, buffer.size());
                    out.add(new ArrayList<>(buffer.subList(0, c)));
                    buffer.subList(0, c).clear();
                }
            }
        }
    }

    private static void drain(List<Integer> buffer, int m, List<List<Integer>> out) {
        while (!buffer.isEmpty()) {
            int c = Math.min(m, buffer.size());
            out.add(new ArrayList<>(buffer.subList(0, c)));
            buffer.subList(0, c).clear();
        }
    }
}
