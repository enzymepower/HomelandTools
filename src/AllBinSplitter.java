
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class AllBinSplitter {

    private static final String ALL_BIN = "C:\\Users\\Playtech\\New Folder\\gc\\hl\\all.bin";
    private static final String BOOT_DOL = "C:\\Users\\Playtech\\New Folder\\gc\\hl\\boot.dol";
//    private static final String OUT_DIR = "C:\\Users\\Playtech\\New Folder\\gc\\hl\\tmp";
    private static final String OUT_TEXTS = "C:\\Users\\Playtech\\New Folder\\gc\\hl\\tmp\\texts.bin";
    private static final int OFFSET = 0xE22D4;
    private static final boolean CNDY_ONLY = true;
    private static final byte[] SPLIT_BYTES = // CNDY
            {0x43, 0x4E, 0x44, 0x59};
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final Charset SHIFT_JIS = Charset.forName("Shift-Jis");

    public static void allBinStrings(String file, ByteBuffer allBin, ByteBuffer bootDol, List<HomelandString> collector) throws IOException {
        List<Integer> offsets = readOffsets(bootDol);
        for (int i = 1; i <= offsets.size(); i++) {
            int start = offsets.get(i - 1);
            int end;
            if (i == offsets.size()) {
                end = allBin.capacity();
            } else {
                end = offsets.get(i);
            }
            boolean startsWithCNDY = true;
            if (end - start > SPLIT_BYTES.length) {
                for (int j = 0; j < SPLIT_BYTES.length; j++) {
                    byte c = SPLIT_BYTES[j];
                    if (c != allBin.get(start + j)) {
                        startsWithCNDY = false;
                        break;
                    }
                }
            } else {
                startsWithCNDY = false;
            }
            if (CNDY_ONLY && !startsWithCNDY) {
                continue;
            }
            int size = end - start;
            byte[] bytes = new byte[size];
            allBin.position(start);
            allBin.get(bytes);
            allBin.position(0);

            if (startsWithCNDY) {
                List<HomelandString> texts = CNDYParser.parse(start, file, bytes);
                collector.addAll(texts);
            }
        }
    }

    public static List<Integer> readOffsets(ByteBuffer map) {
        List<Integer> offsets = new ArrayList<>(15376);
        map.position(OFFSET);
        int count = 1;
        int offset = readInt(map);
        do {
            offsets.add(offset);
            offset = readInt(map);
            count++;
        } while (offset != 0);
        return offsets;
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
}
