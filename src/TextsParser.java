
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextsParser {

    private static final Charset SHIFT_JIS = Charset.forName("Shift-Jis");
    public static final int HASH = 0x23;
    public static final int TAB = 0x09;
    public static final int CR = 0x0D;
    public static final int LF = 0x0A;

    public static class Entry {

        public final int offset;
        public final int length;
        public final byte[] originalText;
        public final byte[] replacementText;

        public Entry(int offset, int length, byte[] originalText, byte[] replacementText) {
            this.offset = offset;
            this.length = length;
            this.originalText = originalText;
            this.replacementText = replacementText;
        }

        @Override
        public String toString() {
            return "Entry{" + "offset=" + offset + ", length=" + length
                    + ", originalText=" + new String(originalText, SHIFT_JIS)
                    + ", replacementText=" + new String(replacementText, SHIFT_JIS) + '}';
        }
    }

    public static List<Entry> parse(byte[] data) {
        int offset = 0;
        List<Entry> entries = new ArrayList<>(5000);
        while (offset < data.length) {
            final boolean ignore;
            if (HASH == b(data, offset)) {
                ignore = true;
                offset++;
            } else {
                ignore = false;
            }
            final int entryOffset;
            {
                final int offsetHexSize = next(data, offset, TAB);
                final byte[] hexBytes = Arrays.copyOfRange(data, offset, offset + offsetHexSize);
                final String hex = new String(hexBytes, SHIFT_JIS);
                entryOffset = Integer.parseInt(hex, 16);
                offset += offsetHexSize;
                offset++;//tab
            }
            final int entrySize;
            {
                final int sizeHexSize = next(data, offset, TAB);
                final byte[] hexBytes = Arrays.copyOfRange(data, offset, offset + sizeHexSize);
                final String hex = new String(hexBytes, SHIFT_JIS);
                entrySize = Integer.parseInt(hex, 16);
                offset += sizeHexSize;
                offset++;//tab
            }
            final byte[] originalBytes = Arrays.copyOfRange(data, offset, offset + entrySize);
            offset += entrySize;
            if (b(data, offset) != TAB) {
                err("tab expected at " + Integer.toHexString(offset));
            }
            offset++;
            final byte[] replacementBytes = Arrays.copyOfRange(data, offset, offset + entrySize);
            offset += entrySize;
            if (b(data, offset) != CR) {
                err("carriage return expected at " + Integer.toHexString(offset));
            }
            offset++;
            if (b(data, offset) != LF) {
                err("Line feed expected at " + Integer.toHexString(offset));
            }

            offset++;
            if (!ignore) {
                final Entry entry = new Entry(entryOffset, entrySize, originalBytes, replacementBytes);
                entries.add(entry);
            }
//            System.out.println("\t\t"+ignore+" "+entry);
        }
        return entries;
    }

    private static int next(byte[] data, int offset, int byteToFind) {
        int len = 0;
        for (; data[offset + len] != byteToFind; len++) {
        }
        return len;
    }

    private static void err(String err) {
        throw new RuntimeException(err);
    }

    private static int b(byte[] ba, int offset) {
        int i = ba[offset];
        return i & 0xFF;
    }
}
