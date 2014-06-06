
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class RelSearcher {

//    private static final String[] RELS = {"C:\\Users\\Playtech\\New Folder\\gc\\hl\\alone.rel",
//        "C:\\Users\\Playtech\\New Folder\\gc\\hl\\client.rel",
//        "C:\\Users\\Playtech\\New Folder\\gc\\hl\\server.rel",
//        "C:\\Users\\Playtech\\New Folder\\gc\\hl\\start.rel"};
    private static final Rel[] RELS = {
        new Rel("C:\\Users\\Playtech\\New Folder\\gc\\hl\\alone.rel", new RelRange[]{
            //len(0x14c860, 401100),
            end(0x14c860, 0x156868),
            end(0x170540, 0x17e2cc),
            end(0x196a58, 0x196b08),
            end(0x196e20, 0x19724c),
            end(0x1a6a3a, 0x1a6abc),
            end(0x1ab696, 0x1ad890),
            end(0x1af000, 0x1af68c),
            end(0x1af6dc, 0x1b2572),
            end(0x1b35a0, 0x1b45e4),
            end(0x1b5ad8, 0x1b7890),
            end(0x1b7c80, 0x1ba758),
            end(0x1ba910, 0x1ba924),
            end(0x1bc1f0, 0x1bc230),
            end(0x1bd260, 0x1bd300),
            end(0x1bd3c4, 0x1bd3fc),
            end(0x1c6648, 0x1c66b4),
            end(0x1c6728, 0x1c6740),
            end(0x1c6b18, 0x1c9790),
            end(0x1c99b8, 0x1c99c4),
            end(0x1c9a80, 0x1c9a88),
            end(0x1cb060, 0x1cb718),
            end(0x1cb720, 0x1cb72c),
            end(0x1cdb40, 0x1cdb48),
            end(0x1cdc9c, 0x1cdcc4),
            end(0x1cdd74, 0x001cdd9c),
            end(0x1cde44, 0x1cde6c),
            end(0x1d0858, 0x1d0874),
            end(0x1d295c, 0x1d296c),
            end(0x1d43e8, 0x1d4444),
            end(0x1dadf8, 0x1daf70),
            end(0x1db140, 0x1db550),
            end(0x1dbe50, 0x1ddc60),
            end(0x1ddc98, 0x1ddcc8),
            //len(0x1ae730, 194492),
        }),
        new Rel("C:\\Users\\Playtech\\New Folder\\gc\\hl\\client.rel", new RelRange[]{
            //len(0xac9b8, 327664),
            end(0x0aca28, 0x0b5cb8),
            end(0x0b6120, 0x0b63d8),
            end(0x0e91b8, 0x0f6ee4),
            end(0x0fd278, 0x0fd904),
            end(0x0fd954, 0x1007ea),
            end(0x1013f0, 0x10143c),
            end(0x1014e8, 0x1014f8),
            end(0x101a80, 0x103838),
            end(0x103e18, 0x103e58),
            end(0x104eb4, 0x104f54),
            end(0x105018, 0x105050),
            end(0x1065b0, 0x1065b8),
            end(0x10e478, 0x110bcc),
            end(0x110c40, 0x110c58),
            end(0x111100, 0x11110c),
            end(0x111ae0, 0x111ae8),
            end(0x113030, 0x1133cc),
            end(0x113544, 0x1135b8),
            end(0x1136a8, 0x1136e8),
            end(0x1136f0, 0x1136fc),
            end(0x115b10, 0x115b18),
            end(0x115c88, 0x116120),
            end(0x116428, 0x116468),
            end(0x1177e2, 0x117804),
            end(0x117ce8, 0x11a8b8),
            end(0x11aa70, 0x11aa84),
            //len(0xfc9a8, 134504),
        }),
        new Rel("C:\\Users\\Playtech\\New Folder\\gc\\hl\\server.rel", new RelRange[]{
            //len(0xfa598, 250920),
            end(0x0fa600, 0x10420c),
            end(0x1209be, 0x124484),
            end(0x124d98, 0x124de0),
            end(0x126b10, 0x128190),
            end(0x129028, 0x1372a4),
            end(0x138e38, 0x138ea4),
            end(0x138f50, 0x138f60),
            end(0x139338, 0x13a378),
            end(0x13b840, 0x13c4b0),
            end(0x13c804, 0x13c810),
            end(0x13dbec, 0x13dc44),
            end(0x140160, 0x1401bc),
            end(0x14221c, 0x142248),
            end(0x142fd8, 0x142ff8),
            end(0x1431b8, 0x1431d4),
            end(0x147b00, 0x1485f8),
            end(0x1488e8, 0x1489bc),
            end(0x148c10, 0x148c48),
            end(0x148da8, 0x148db0),
            end(0x148e9c, 0x148edc),
            end(0x149040, 0x149544),
            end(0x149580, 0x1495dc),
            end(0x149748, 0x1499dc),
            end(0x149e44, 0x149e6c),
            end(0x14bcc0, 0x14bcd0),
            end(0x14c5f8, 0x14e408),
            end(0x14e440, 0x14e470),
            end(0x14e680, 0x14e688),
            end(0x14e9b0, 0x151390),
            //len(0x1379c0, 111924),
        }),
        new Rel("C:\\Users\\Playtech\\New Folder\\gc\\hl\\start.rel", new RelRange[]{
//            len(0x10d88, 0x3e08),
            end(0x014894, 0x014a2a),
            end(0x014a98, 0x014b10),
            end(0x014cd4, 0x014e0c),
            end(0x014e68, 0x014f10),
            end(0x015128, 0x01513c),
            end(0x015380, 0x015460),
//            len(0x14b90, 9152),
        }),
        new Rel("C:\\Users\\Playtech\\New Folder\\gc\\hl\\boot.dol", new RelRange[]{
            //len(0xf3880, 0x85d80),
            end(0x0f4688, 0x0f531b),
            end(0x0f5398, 0x0f5404),
            end(0x0f5480, 0x0f54ea),
            end(0x0f570a, 0x0f8fc2),
        }),
    };

    private static RelRange len(int offset, int length) {
        return new RelRange(offset, length);
    }

    private static RelRange end(int offset, int toOffset) {
        return new RelRange(offset, toOffset - offset);
    }
    private static final String OUT_DIR = "C:\\Users\\Playtech\\New Folder\\gc\\hl\\tmp";
    //odd is start, inclusive
    //even is end, exclusive
    private static int[] INVALID_FIRST_BYTE_RANGES = {
        0x00, 0x20,
        0x7F, 81,
        0xA0, 0xA1,
        0xF0, 0x100,};
    private static int[] DOUBLE_BYTE_CHARACTER_RANGES = {
        0x81, 0xA0,
        0xE0, 0xF0,};
    private static int[] INVALID_SECOND_BYTE_CHARACTER_RANGES = {
        0x00, 0x40,
        0x7F, 0x80,
        0xFC, 0x100,};
    private static final byte END_OF_STRING = 0;
    private static final Charset SHIFT_JIS = Charset.forName("Shift-Jis");

    static class Rel {

        public String rel;
        //odd is start, inclusive
        //even is end, exclusive
        public RelRange[] ranges;

        public Rel(String rel, RelRange[] ranges) {
            this.rel = rel;
            this.ranges = ranges;
        }
    }

    static class RelRange {

        public int offset;
        public int end;

        public RelRange(int offset, int length) {
            this.offset = offset;
            this.end = offset + length;
        }
    }

    enum State {

        searching,
        matching,
        secondByte
    }

    public static void main(String[] args) throws IOException {
        new File(OUT_DIR).mkdirs();
        for (Rel rel : RELS) {
            Path rp = new File(rel.rel).toPath();
            File out = new File(OUT_DIR + "\\" + rp.getFileName() + ".texts");
            out.createNewFile();
            try (FileChannel fc = FileChannel.open(rp, StandardOpenOption.READ);
                    FileOutputStream textOut = new FileOutputStream(out)) {
                MappedByteBuffer map = fc.map(MapMode.READ_ONLY, 0, fc.size());
                for (RelRange rr : rel.ranges) {
                    map.position(rr.offset);
                    int start = map.position();
                    State state = State.searching;
                    while (map.hasRemaining() && map.position() < rr.end) {
                        byte b = map.get();
                        switch (state) {
                            case searching:
                                if (!inRange(b, INVALID_FIRST_BYTE_RANGES) /*
                                         * && (map.position() - 1) % 4 == 0
                                         */) {
                                    state = State.matching;
                                    start = map.position() - 1;
                                }
                                break;
                            case matching:
                                if (b == END_OF_STRING) {
                                    //end of string
                                    state = State.searching;
                                    int end = map.position() - 1;
                                    int length = end - start;
                                    if (length > 3) {
                                        int pos = map.position();
                                        map.position(start);
                                        byte[] bytes = new byte[length];
                                        map.get(bytes);
                                        map.position(pos);
//                                System.out.println("String found " + toHex(start) + ":" + toHex(map.position()));
                                        textOut.write("#".getBytes(SHIFT_JIS));
                                        textOut.write(toHex(start).getBytes(SHIFT_JIS));
                                        textOut.write("\t".getBytes(SHIFT_JIS));
                                        textOut.write(toHex(length).getBytes(SHIFT_JIS));
                                        textOut.write("\t".getBytes(SHIFT_JIS));
                                        textOut.write(bytes);
                                        textOut.write("\t".getBytes(SHIFT_JIS));
                                        for (int j = 0; j < length; j++) {
                                            textOut.write(0x3F);
                                        }
                                        textOut.write("\r\n".getBytes(SHIFT_JIS));
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
