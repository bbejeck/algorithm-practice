package dev.bbejeck.design;

/**
 * Write with an Internal Buffer and a Max Chunk Size (reported Datadog technical screen question, via PracHub)
 *
 * Device.writeToDevice(chunk) is expensive per call and accepts at most M bytes per call. The device
 * copies the bytes it is given, so you may reuse your arrays.
 *
 * ChunkedBufferedWriter(device, M, B): B is the internal buffer capacity. Throw
 * IllegalArgumentException if M < 1 or B < 1.
 *
 * Follow these rules exactly, so the chunks sent to the device are deterministic:
 *   write(data):
 *     1. Bypass: whenever the buffer is empty and at least B bytes of this write remain, send the
 *        remaining bytes straight to the device in chunks of min(M, remaining) until fewer than B
 *        remain.
 *     2. Otherwise copy as many bytes as fit into the buffer. As soon as the buffer is full, send
 *        min(M, bufferedBytes) bytes from the FRONT of the buffer, keep the rest buffered, and carry
 *        on with the write (checking rule 1 again).
 *   flush(): send everything buffered, in chunks of at most M, leaving the buffer empty.
 *   close(): flush, then reject later writes and flushes with IllegalStateException. A second close
 *     does nothing.
 * No chunk may be larger than M or empty, and no byte may be lost, duplicated or reordered.
 *
 * Example (M = 3, B = 4):
 *   write [1, 2]          -> nothing sent, buffer [1, 2]
 *   write [3, 4, 5]       -> buffer fills at [1, 2, 3, 4]: send [1, 2, 3]; buffer [4, 5]
 *   flush                 -> send [4, 5]
 *   write [6..14] (9 bytes, buffer empty) -> bypass: send [6, 7, 8], [9, 10, 11]; 3 < B remain,
 *                            so buffer [12, 13, 14]
 *   close                 -> send [12, 13, 14]
 *
 * Constraints: M, B up to 10^5. Total bytes up to 2 * 10^5. Work should be O(total bytes).
 */
public class ChunkedBufferedWriter {

    public interface Device {
        void writeToDevice(byte[] chunk);
    }

    public ChunkedBufferedWriter(Device device, int maxChunk, int bufferSize) {
        throw new UnsupportedOperationException("TODO");
    }

    public void write(byte[] data) {
        throw new UnsupportedOperationException("TODO");
    }

    public void flush() {
        throw new UnsupportedOperationException("TODO");
    }

    public void close() {
        throw new UnsupportedOperationException("TODO");
    }
}
