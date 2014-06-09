
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class RelSearcher {

    private static final String OUT_DIR = "C:\\Users\\Playtech\\New Folder\\gc\\hl\\tmp";
    //odd is start, inclusive
    //even is end, exclusive
    private static int[] INVALID_FIRST_BYTE_RANGES = {
        0x00, 0x20,
        0x7F, 0x81,
        0xA0, 0xA1,
        0xF0, 0x100,};
    private static int[] DOUBLE_BYTE_CHARACTER_RANGES = {
        0x81, 0xA0,
        0xE0, 0xF0,};
    private static int[] INVALID_SECOND_BYTE_CHARACTER_RANGES = {
        0x00, 0x40,
        0x7F, 0x80,
        0xFD, 0x100,};
    private static final byte END_OF_STRING = 0;
    private static final Charset SHIFT_JIS = Charset.forName("Shift-Jis");

    enum State {

        searching,
        matching,
        secondByte
    }

    public static void relStrings(String file, ByteBuffer rel, RelRange[] ranges, List<HomelandString> collector) throws IOException {
        for (RelRange rr : ranges) {
            rel.position(rr.offset);
            int start = rel.position();
            State state = State.searching;
            while (rel.hasRemaining() && rel.position() < rr.end) {
                byte b = rel.get();
                switch (state) {
                    case searching:
                        if (!inRange(b, INVALID_FIRST_BYTE_RANGES) /*
                                 * && (map.position() - 1) % 4 == 0
                                 */) {
                            state = inRange(b, DOUBLE_BYTE_CHARACTER_RANGES) ? State.secondByte : State.matching;
                            start = rel.position() - 1;
                        }
                        break;
                    case matching:
                        if (b == END_OF_STRING) {
                            //end of string
                            state = State.searching;
                            int end = rel.position() - 1;
                            int length = end - start;
                            if (length > 1) {
                                int pos = rel.position();
                                rel.position(start);
                                byte[] bytes = new byte[length];
                                rel.get(bytes);
                                rel.position(pos);
//                                System.out.println("String found " + toHex(start) + ":" + toHex(map.position()));
//                                        textOut.write("#".getBytes(SHIFT_JIS));
//                                        textOut.write(toHex(start).getBytes(SHIFT_JIS));
//                                        textOut.write("\t".getBytes(SHIFT_JIS));
//                                        textOut.write(toHex(length).getBytes(SHIFT_JIS));
//                                        textOut.write("\t".getBytes(SHIFT_JIS));
//                                        textOut.write(bytes);
//                                        textOut.write("\t".getBytes(SHIFT_JIS));
//                                        for (int j = 0; j < length; j++) {
//                                            textOut.write(0x3F);
//                                        }
//                                        textOut.write("\r\n".getBytes(SHIFT_JIS));
                                collector.add(new HomelandString(file, start, length, bytes));
                            }
                        } else if (inRange(b, INVALID_FIRST_BYTE_RANGES)) {
                            //not a string
                            state = State.searching;
                        } else if (inRange(b, DOUBLE_BYTE_CHARACTER_RANGES)) {
                            //multibyte character
                            state = State.secondByte;
                        }
                        break;
                    case secondByte:
                        if (inRange(b, INVALID_SECOND_BYTE_CHARACTER_RANGES)) {
                            //not a string
                            state = State.searching;
                        } else {
                            state = State.matching;
                        }
                        break;
                }
            }
        }
    }

    private static boolean inRange(byte b, int[] ranges) {
        int c = b;
        c &= 0xFF;
        for (int i = 1; i < ranges.length; i += 2) {
            if (c >= ranges[i - 1] && c < ranges[i]) {
                return true;
            }
        }
        return false;
    }
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String toHex(int i) {
        return new String(new char[]{
                    HEX[(i >> 28) & 0xF],
                    HEX[(i >> 24) & 0xF],
                    HEX[(i >> 20) & 0xF],
                    HEX[(i >> 16) & 0xF],
                    HEX[(i >> 12) & 0xF],
                    HEX[(i >> 8) & 0xF],
                    HEX[(i >> 4) & 0xF],
                    HEX[(i >> 0) & 0xF],});
    }

    public static int readInt(ByteBuffer bb) {
        int a = bb.get();
        int b = bb.get();
        int c = bb.get();
        int d = bb.get();
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
