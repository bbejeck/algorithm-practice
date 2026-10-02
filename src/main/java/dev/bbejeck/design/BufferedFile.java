package dev.bbejeck.design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Buffered File Writer
 * <p>
 * You are given a File class that writes bytes directly to disk. Every call
 * to File.write is expensive (think: one syscall per call).
 * <p>
 * Implement a BufferedFile class that wraps a File and buffers writes in memory.
 * <p>
 * BufferedFile(File file, int maxBytes)
 * - file:     the underlying File to write to
 * - maxBytes: the maximum number of bytes the buffer may hold
 * <p>
 * void write(byte[] data)
 * - Adds data to the buffer.
 * - When the buffer is full, its contents must be written to the
 * underlying File.
 * - All bytes must pass through the buffer before reaching the File.
 * - The buffer must never use more than maxBytes of memory.
 * - data may be larger than maxBytes.
 * <p>
 * void flush()
 * - Writes any buffered bytes to the underlying File.
 * <p>
 * Bytes must reach the File in the same order they were written.
 * <p>
 * Run main() to check your solution against the sample cases.
 */
public class BufferedFile {

    // ---------------------------------------------------------------
    // Provided: do not modify
    // ---------------------------------------------------------------
    public static class File {
        private final List<byte[]> writes = new ArrayList<>();

        public void write(byte[] data) {
            writes.add(Arrays.copyOf(data, data.length));
        }

        public int writeCalls() {
            return writes.size();
        }

        public int largestWrite() {
            int max = 0;
            for (byte[] w : writes) {
                max = Math.max(max, w.length);
            }
            return max;
        }

        public String contents() {
            StringBuilder sb = new StringBuilder();
            for (byte[] w : writes) {
                sb.append(new String(w));
            }
            return sb.toString();
        }
    }
    // ---------------------------------------------------------------

    private final File file;
    private final int maxBytes;
    private final byte[] buffer;
    private int count;

    public BufferedFile(File file, int maxBytes) {
        this.file = file;
        this.maxBytes = maxBytes;
        this.buffer = new byte[maxBytes];
    }

    public void write(byte[] data) {
        int srcPos = 0;
        while (srcPos < data.length) {
            int space = maxBytes - count;
            int remaining = data.length - srcPos;
            int n = Math.min(space, remaining);
            System.arraycopy(data, srcPos, buffer, count, n);
            srcPos += n;
            count += n;
            if (count == maxBytes) {
                flush();
            }
        }
    }

    public void flush() {
        if (count > 0) {
            file.write(Arrays.copyOf(buffer, count));
            count = 0;
        }
    }

    public static void main(String[] args) {
        // Case 1: small writes, then flush
        File f1 = new File();
        BufferedFile bf1 = new BufferedFile(f1, 8);
        bf1.write("abc".getBytes());
        bf1.write("de".getBytes());
        check("case 1 before flush", "", f1.contents());
        bf1.flush();
        check("case 1 after flush", "abcde", f1.contents());

        check("case 1 write calls", 1, f1.writeCalls());

        // Case 2: buffer fills across two writes -> full buffer reaches the file without flush()
        File f2 = new File();
        BufferedFile bf2 = new BufferedFile(f2, 4);
        bf2.write("abc".getBytes());
        bf2.write("de".getBytes());
        check("case 2 before flush", "abcd", f2.contents());
        bf2.flush();
        check("case 2 after flush", "abcde", f2.contents());
        check("case 2 largest write <= maxBytes", true, f2.largestWrite() <= 4);

        // Case 3: a single write larger than the buffer
        File f3 = new File();
        BufferedFile bf3 = new BufferedFile(f3, 3);
        bf3.write("hello world".getBytes());
        check("case 3 before flush", "hello wor", f3.contents());
        bf3.flush();
        check("case 3 after flush", "hello world", f3.contents());
        check("case 3 largest write <= maxBytes", true, f3.largestWrite() <= 3);

        // Case 4: write just under 2x the buffer size
        File f4 = new File();
        BufferedFile bf4 = new BufferedFile(f4, 4);
        bf4.write("abcdefg".getBytes());
        bf4.flush();
        check("case 4", "abcdefg", f4.contents());
        check("case 4 largest write <= maxBytes", true, f4.largestWrite() <= 4);

        // Case 5: flush with nothing buffered
        File f5 = new File();
        BufferedFile bf5 = new BufferedFile(f5, 5);
        bf5.flush();
        check("case 5", "", f5.contents());
        check("case 5 write calls", 0, f5.writeCalls());

        // Case 6: flush twice must not duplicate data
        File f6 = new File();
        BufferedFile bf6 = new BufferedFile(f6, 8);
        bf6.write("abc".getBytes());
        bf6.flush();
        bf6.flush();
        check("case 6", "abc", f6.contents());

        // Case 7: writes after a flush
        File f7 = new File();
        BufferedFile bf7 = new BufferedFile(f7, 8);
        bf7.write("ab".getBytes());
        bf7.flush();
        bf7.write("cd".getBytes());
        bf7.flush();
        check("case 7", "abcd", f7.contents());

        // Case 8: data length an exact multiple of the buffer -> no empty writes
        File f8 = new File();
        BufferedFile bf8 = new BufferedFile(f8, 2);
        bf8.write("abcdef".getBytes());
        bf8.flush();
        check("case 8", "abcdef", f8.contents());
        check("case 8 write calls", 3, f8.writeCalls());
    }

    private static void check(String name, Object expected, Object actual) {
        String status = expected.equals(actual) ? "\u001B[32m●\u001B[0m" : "\u001B[31m✗\u001B[0m";
        System.out.printf("%s %s: expected=\"%s\" actual=\"%s\"%n", status, name, expected, actual);
    }
}
