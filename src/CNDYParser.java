
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CNDYParser {

//    public static class Text {
//
//        public final int offset;
//        public final int length;
//        public final String string;
//        public final byte[] bytes;
//
//        public Text(int offset, int length, String string, byte[] bytes) {
//            this.offset = offset;
//            this.length = length;
//            this.string = string;
//            this.bytes = bytes;
//        }
//    }
    private static final Charset SHIFT_JIS = Charset.forName("Shift-Jis");

    public static List<HomelandString> parse(final int startOffset, final String file, final byte[] bytes) {
        List<HomelandString> texts = new ArrayList<>(1000);
        int offset = 4;//skip CNDY
        final int numEvents = readInt(bytes, offset);
//        System.out.println("\tnum events " + numEvents);
        offset += 4;
        final int blockSize = readInt(bytes, offset);
        final int offsetOfPostBlockThings = 0xC/*
                 * CANDY header length
                 */ + blockSize;
//        System.out.println("\tdata offset from CNDY header " + blockSize);
        offset += 4;
        for (int i = 0; i < numEvents; i++) {
            int start = offset;
            while (bytes[offset] != 0) {
                offset++;
            }
            int end = offset;
            String eventName = new String(bytes, start, end - start, SHIFT_JIS);
//            System.out.println("\t\tevent " + eventName + " " + AllBinSplitter.toHex(start) + " " + AllBinSplitter.toHex(offset + 1) + " " + blockSize);
            offset++;
        }
        //block zero padding
        offset = offsetOfPostBlockThings;
        //one byte for each event
        //padded to multiple of four
        offset += numEvents;
        if (offset % 4 != 0) {
            offset += 4 - offset % 4;
        }
        //one 32 bit integer for each events
        offset += numEvents * 4;

        final int numStrings = readInt(bytes, offset);
        offset += 4;
//        System.out.println("\tnum strings " + numStrings);
        final int stringsSize = readInt(bytes, offset);
        offset += 4;
        for (int i = 0; i < numStrings; i++) {
            int start = offset;
            while (bytes[offset] != 0) {
                offset++;
            }
            int end = offset;
            String string = new String(bytes, start, end - start, SHIFT_JIS);
//            System.out.println("\t\tString |" + string + "| " + AllBinSplitter.toHex(start) + " " + AllBinSplitter.toHex(offset + 1) + " " + (end - start) + " bytes");
            texts.add(new HomelandString(file, startOffset + start, end - start, Arrays.copyOfRange(bytes, start, end)));
            offset++;
        }
        return texts;
    }

    public static int readInt(byte[] bb, int index) {
        int a = bb[index];
        int b = bb[index + 1];
        int c = bb[index + 2];
        int d = bb[index + 3];
        a &= 0xFF;
        b &= 0xFF;
        c &= 0xFF;
        d &= 0xFF;
        return ((a << 24)
                + (b << 16)
                + (c << 8)
                + d);
    }
}
