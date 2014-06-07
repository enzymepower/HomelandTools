
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TextsParser {

    private static final Charset SHIFT_JIS = Charset.forName("Shift-Jis");
    public static final int HASH = 0x23;
    public static final int TAB = 0x09;
    public static final int CR = 0x0D;
    public static final int LF = 0x0A;
    public static final int PIPE = 0x7C;

    public static class Entry {

        public final String id;
        public final int length;
        public final byte[] originalText;
//        public final byte[] replacementText;
        public final Map<String, List<Integer>> offsets;

        public Entry(String id, int length, byte[] originalText, /*
                 * byte[] replacementText,
                 */ Map<String, List<Integer>> offsets) {
            this.id = id;
            this.length = length;
            this.originalText = originalText;
//            this.replacementText = replacementText;
            this.offsets = offsets;
        }

        @Override
        public String toString() {
            return "Entry{" + "id=" + id + ", length=" + length
                    + ", originalText=" + new String(originalText, SHIFT_JIS)
                    //                    + ", replacementText=" + new String(replacementText, SHIFT_JIS)
                    + ", offsets=" + offsets + '}';
        }
    }

    public static Map<String, Entry> parseTexts(byte[] data) {
        int offset = 0;
        Map<String, Entry> entries = new LinkedHashMap<>(50_000);
        while (offset < data.length) {
            final boolean ignore;
            if (HASH == b(data, offset)) {
                ignore = true;
                offset++;
            } else {
                ignore = false;
            }
            final String entryId;
            {
                final int idSize = next(data, offset, CR);
                final byte[] idBytes = Arrays.copyOfRange(data, offset, offset + idSize);
                entryId = new String(idBytes, SHIFT_JIS).intern();
                offset += idSize;
                offset++;//cr
                if (b(data, offset) != LF) {
                    err("Line feed expected at " + Integer.toHexString(offset));
                }
                offset++;
            }
            final Map<String, List<Integer>> entryOffsets = new LinkedHashMap<>();
            for (;;) {
                final int offsetSize = next(data, offset, CR);
                if (offsetSize > 0) {
                    final int fileSize = next(data, offset, TAB);
                    final byte[] fileBytes = Arrays.copyOfRange(data, offset, offset + fileSize);
                    final String file = new String(fileBytes, SHIFT_JIS).intern();
                    offset += fileSize;
                    offset++;//tab

                    final int offsetHexSize = offsetSize - (fileSize + 1);//file+tab
                    final byte[] hexBytes = Arrays.copyOfRange(data, offset, offset + offsetHexSize);
                    final String hex = new String(hexBytes, SHIFT_JIS);
                    final int fileOffset = Integer.parseInt(hex, 16);
                    List<Integer> offsets = entryOffsets.get(file);
                    if (offsets == null) {
                        offsets = new ArrayList<>(1);
                        entryOffsets.put(file, offsets);
                    }
                    offsets.add(fileOffset);
                    offset += offsetHexSize;
                    offset++;//cr
                    if (b(data, offset) != LF) {
                        err(entryId + ": Line feed expected at " + Integer.toHexString(offset));
                    }
                    offset++;
                } else {
                    offset++;//cr
                    if (b(data, offset) != LF) {
                        err(entryId + ": Line feed expected at " + Integer.toHexString(offset));
                    }
                    offset++;
                    break;
                }
            }
            final int entrySize;
            {
                final int sizeHexSize = next(data, offset, CR);
                final byte[] hexBytes = Arrays.copyOfRange(data, offset, offset + sizeHexSize);
                final String hex = new String(hexBytes, SHIFT_JIS);
//                System.out.println(entryId + " " + hex);
                entrySize = Integer.parseInt(hex, 16);
                offset += sizeHexSize;
                offset++;//cr
                if (b(data, offset) != LF) {
                    err(entryId + ": Line feed expected at " + Integer.toHexString(offset));
                }
                offset++;
            }
            final byte[] originalBytes = Arrays.copyOfRange(data, offset, offset + entrySize);
            offset += entrySize;
            if (b(data, offset) != CR) {
                err(entryId + ": Carriage return expected at " + Integer.toHexString(offset));
            }
            offset++;//cr
            if (b(data, offset) != LF) {
                err(entryId + ": Line feed expected at " + Integer.toHexString(offset) + " " + Integer.toHexString(entrySize));
            }
            offset++;
//            final byte[] replacementBytes = Arrays.copyOfRange(data, offset, offset + entrySize);
//            offset += entrySize;
//            if (b(data, offset) != CR) {
//                err(entryId+": Carriage return expected at " + Integer.toHexString(offset) + " " + Integer.toHexString(entrySize));
//            }
//            offset++;
//            if (b(data, offset) != LF) {
//                err(entryId+": Line feed expected at " + Integer.toHexString(offset) + " " + Integer.toHexString(entrySize));
//            }
//            offset++;
            if (!ignore) {
                final Entry entry = new Entry(entryId, entrySize, originalBytes, /*
                         * replacementBytes,
                         */ entryOffsets);
                entries.put(entryId, entry);
            }
//            System.out.println("\t\t"+ignore+" "+entry);
        }
        return entries;
    }

    public static Map<String, byte[]> parseReplacements(byte[] data) {
        int offset = 0;
        Map<String, byte[]> entries = new HashMap<>(1000);
        while (offset < data.length) {
            final boolean ignore;
            if (HASH == b(data, offset)) {
                ignore = true;
                offset++;
            } else {
                ignore = false;
            }
            final String entryId;
            {
                final int idSize = next(data, offset, CR);
                final byte[] idBytes = Arrays.copyOfRange(data, offset, offset + idSize);
                entryId = new String(idBytes, SHIFT_JIS).intern();
                offset += idSize;
                offset++;//cr
                if (b(data, offset) != LF) {
                    err("Line feed expected at " + Integer.toHexString(offset));
                }
                offset++;
            }
            final byte[] replacementBytes;
            {
                final int length = next(data, offset, PIPE);
                replacementBytes = Arrays.copyOfRange(data, offset, offset + length);
                offset += length;
                offset++;//pipe
            }
            if (b(data, offset) != CR) {
                err(entryId + ": Carriage return expected at " + Integer.toHexString(offset));
            }
            offset++;//cr
            if (b(data, offset) != LF) {
                err(entryId + ": Line feed expected at " + Integer.toHexString(offset));
            }
            offset++;
//            final byte[] replacementBytes = Arrays.copyOfRange(data, offset, offset + entrySize);
//            offset += entrySize;
//            if (b(data, offset) != CR) {
//                err(entryId+": Carriage return expected at " + Integer.toHexString(offset) + " " + Integer.toHexString(entrySize));
//            }
//            offset++;
//            if (b(data, offset) != LF) {
//                err(entryId+": Line feed expected at " + Integer.toHexString(offset) + " " + Integer.toHexString(entrySize));
//            }
//            offset++;
            if (!ignore) {
                entries.put(entryId, replacementBytes);
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
