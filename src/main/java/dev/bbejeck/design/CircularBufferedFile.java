package dev.bbejeck.design;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Buffered File Writer, follow-up: partial writes and a circular buffer
 *
 * The File you write to now behaves like a real write() syscall: it may accept
 * only some of the bytes you give it.
 *
 *   int File.write(byte[] data, int offset, int length)
 *       - Writes the first k bytes of data[offset .. offset + length) and returns k.
 *       - k can be anything from 0 to length. 0 means nothing was written; call it again.
 *       - length must be > 0 and the range must be inside data, otherwise it throws.
 *       - One call can only take one contiguous range of the array.
 *
 * Implement CircularBufferedFile with the same API as before:
 *
 *   CircularBufferedFile(File file, int maxBytes)
 *       - Throw IllegalArgumentException if maxBytes <= 0.
 *
 *   void write(byte[] data)
 *       - Copies data into the buffer.
 *       - As soon as the buffer is full, call File.write until it accepts at least
 *         one byte, then keep copying data into the space that frees up.
 *       - Do NOT empty the whole buffer here. Only flush() does that.
 *
 *   void flush()
 *       - Keeps calling File.write until the buffer is empty.
 *
 * Rules:
 *   - Bytes reach the File in the order they were written, none lost or duplicated.
 *   - The buffer never holds more than maxBytes.
 *   - Allocate the buffer once. Pass the buffer itself to File.write with an
 *     offset and length; don't copy it into a new array first.
 *   - Don't shift bytes to the front of the buffer after a partial write. Track
 *     where the unwritten data starts and let data wrap around the end of the array.
 *
 * Run main() to check your solution against the sample cases.
 */
public class CircularBufferedFile {

    // ---------------------------------------------------------------
    // Provided: do not modify
    // ---------------------------------------------------------------
    public static class File {
        private final int[] acceptPattern;
        private final StringBuilder contents = new StringBuilder();
        private final Set<byte[]> arraysSeen = Collections.newSetFromMap(new IdentityHashMap<>());
        private int calls;
        private int largestWrite;

        /** A File that accepts every byte it's given. */
        public File() {
            this(Integer.MAX_VALUE);
        }

        /** Call number i accepts at most acceptPattern[i % acceptPattern.length] bytes. */
        public File(int... acceptPattern) {
            this.acceptPattern = acceptPattern;
        }

        public int write(byte[] data, int offset, int length) {
            if (length <= 0 || offset < 0 || offset + length > data.length) {
                throw new IllegalArgumentException(
                        "bad range: offset=" + offset + " length=" + length + " array=" + data.length);
            }
            int accepted = Math.min(length, acceptPattern[calls % acceptPattern.length]);
            calls++;
            contents.append(new String(data, offset, accepted));
            largestWrite = Math.max(largestWrite, accepted);
            arraysSeen.add(data);
            return accepted;
        }

        public int writeCalls() {
            return calls;
        }

        public String contents() {
            return contents.toString();
        }

        public int largestWrite() {
            return largestWrite;
        }

        /** How many different arrays were passed to write(). */
        public int distinctArrays() {
            return arraysSeen.size();
        }
    }
    // ---------------------------------------------------------------

    int maxBytes;
    File file;
    int count = 0;
    byte[] buffer;
    int head = 0;
    int tail = 0;
    public CircularBufferedFile(File file, int maxBytes) {
        if (maxBytes <= 0) {
            throw new IllegalArgumentException("maxBytes must be greater than 0");
        }
        this.file = file;
        this.maxBytes = maxBytes;
        this.buffer = new byte[maxBytes];
    }

    public void write(byte[] data) {
        int srcPos = 0;
        while (srcPos < data.length) {
           int remainBufferSpace = maxBytes - count;
           int remainToCopy = data.length - srcPos;
           int spaceBeforeEnd = maxBytes - tail;
           int toCopy = Math.min(Math.min(remainBufferSpace, remainToCopy), spaceBeforeEnd);
           System.arraycopy(data, srcPos, buffer, tail, toCopy);
           tail = (tail + toCopy) % maxBytes;
           count += toCopy;
           srcPos += toCopy;
           if (count == maxBytes) {
               writeOnce();
           }
        }
    }

    public void flush() {
       while (count > 0) {
           writeOnce();
       }
    }

    // One File.write from the front of the buffer, retrying until at least one byte is accepted
    private void writeOnce() {
        int bytesBeforeEnd = Math.min(count, maxBytes - head);
        int k;
        while ((k = file.write(buffer, head, bytesBeforeEnd)) == 0) {
            // File accepted nothing, try again
        }
        head = (head + k) % maxBytes;
        count -= k;
    }

    public static void main(String[] args) {
        // Case 1: File accepts everything -> same behavior as BufferedFile
        File f1 = new File();
        CircularBufferedFile c1 = new CircularBufferedFile(f1, 8);
        c1.write("abc".getBytes());
        c1.write("de".getBytes());
        check("case 1 before flush", "", f1.contents());
        c1.flush();
        check("case 1 after flush", "abcde", f1.contents());
        check("case 1 write calls", 1, f1.writeCalls());

        // Case 2: File accepts 2 bytes per call -> data wraps around the end of the buffer
        File f2 = new File(2);
        CircularBufferedFile c2 = new CircularBufferedFile(f2, 4);
        c2.write("abcdef".getBytes());
        check("case 2 before flush", "abcd", f2.contents());
        c2.flush();
        check("case 2 after flush", "abcdef", f2.contents());
        check("case 2 write calls", 3, f2.writeCalls());

        // Case 3: File returns 0 sometimes -> keep retrying
        File f3 = new File(0, 0, 3);
        CircularBufferedFile c3 = new CircularBufferedFile(f3, 3);
        c3.write("abcdef".getBytes());
        check("case 3 before flush", "abcdef", f3.contents());
        check("case 3 write calls", 6, f3.writeCalls());

        // Case 4: flush() keeps going until the buffer is empty
        File f4 = new File(1);
        CircularBufferedFile c4 = new CircularBufferedFile(f4, 8);
        c4.write("hello".getBytes());
        check("case 4 before flush", "", f4.contents());
        c4.flush();
        check("case 4 after flush", "hello", f4.contents());
        check("case 4 write calls", 5, f4.writeCalls());

        // Case 5: write() frees space with one successful call, it doesn't empty the buffer
        File f5 = new File(3);
        CircularBufferedFile c5 = new CircularBufferedFile(f5, 5);
        c5.write("abcde".getBytes());
        c5.write("fg".getBytes());
        c5.write("hij".getBytes());
        check("case 5 before flush", "abcdefgh", f5.contents());
        c5.flush();
        check("case 5 after flush", "abcdefghij", f5.contents());
        check("case 5 write calls", 4, f5.writeCalls());

        // Case 6: flush() has to send the data in two pieces because it wraps
        File f6 = new File(1);
        CircularBufferedFile c6 = new CircularBufferedFile(f6, 4);
        c6.write("abcd".getBytes());
        c6.write("xy".getBytes());
        check("case 6 before flush", "abc", f6.contents());
        c6.flush();
        check("case 6 after flush", "abcdxy", f6.contents());
        check("case 6 write calls", 6, f6.writeCalls());

        // Case 7: a write much larger than the buffer, with an uneven File
        File f7 = new File(2, 3);
        CircularBufferedFile c7 = new CircularBufferedFile(f7, 4);
        c7.write("the quick brown fox".getBytes());
        c7.flush();
        check("case 7", "the quick brown fox", f7.contents());
        check("case 7 largest write <= maxBytes", true, f7.largestWrite() <= 4);
        check("case 7 one buffer passed to File", 1, f7.distinctArrays());

        // Case 8: flush twice must not duplicate data
        File f8 = new File(2);
        CircularBufferedFile c8 = new CircularBufferedFile(f8, 8);
        c8.write("abc".getBytes());
        c8.flush();
        c8.flush();
        check("case 8", "abc", f8.contents());
        check("case 8 write calls", 2, f8.writeCalls());

        // Case 9: writes after a flush keep going in order
        File f9 = new File(3);
        CircularBufferedFile c9 = new CircularBufferedFile(f9, 4);
        c9.write("abcd".getBytes());
        c9.flush();
        c9.write("efgh".getBytes());
        c9.write("ij".getBytes());
        c9.flush();
        check("case 9", "abcdefghij", f9.contents());
        check("case 9 one buffer passed to File", 1, f9.distinctArrays());

        // Case 10: flush with nothing buffered
        File f10 = new File(2);
        CircularBufferedFile c10 = new CircularBufferedFile(f10, 4);
        c10.flush();
        check("case 10", "", f10.contents());
        check("case 10 write calls", 0, f10.writeCalls());

        // Case 11: maxBytes <= 0 is rejected
        boolean threw = false;
        try {
            new CircularBufferedFile(new File(), 0);
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check("case 11 maxBytes 0 throws", true, threw);
    }

    private static void check(String name, Object expected, Object actual) {
        String status = expected.equals(actual) ? "\u001B[32m●\u001B[0m" : "\u001B[31m✗\u001B[0m";
        System.out.printf("%s %s: expected=\"%s\" actual=\"%s\"%n", status, name, expected, actual);
    }
}
