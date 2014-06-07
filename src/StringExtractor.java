

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import static java.nio.file.StandardOpenOption.*;

public class StringExtractor {

    private static final Rel[] RELS = {
        new Rel("start.rel", new RelRange[]{
            //            len(0x10d88, 0x3e08),
            end(0x014894, 0x014a2a),
            end(0x014a98, 0x014b10),
            end(0x014cd4, 0x014e0c),
            end(0x014e68, 0x014f10),
            end(0x015128, 0x01513c),
            end(0x015380, 0x015460), //            len(0x14b90, 9152),
        }),
        new Rel("alone.rel", new RelRange[]{
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
            end(0x1ddc98, 0x1ddcc8), //len(0x1ae730, 194492),
        }),
        new Rel("client.rel", new RelRange[]{
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
            end(0x11aa70, 0x11aa84), //len(0xfc9a8, 134504),
        }),
        new Rel("server.rel", new RelRange[]{
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
            end(0x14e9b0, 0x151390), //len(0x1379c0, 111924),
        }),
        new Rel("boot.dol", new RelRange[]{
//            len(0xf3880, 0x85d80),
            end(0x0f4688, 0x0f531b),
            end(0x0f5398, 0x0f5404),
            end(0x0f5480, 0x0f54ea),
            end(0x0f570a, 0x0f8fc2),
            end(0x1798e8, 0x1798fe),
            end(0x179960, 0x179a5c),
            end(0x179a6a, 0x179ea0),
//            len(0x179600, 0x19e0),
        }),};
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static RelRange len(int offset, int length) {
        return new RelRange(offset, length);
    }

    private static RelRange end(int offset, int toOffset) {
        return new RelRange(offset, toOffset - offset);
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String baseDir = args[0];//"C:\\Users\\Playtech\\New Folder\\gc\\hl";
        String outFile = args[1];//"C:\\Users\\Playtech\\New Folder\\gc\\hl\\texts.dump";
        Path base = Paths.get(baseDir);
        List<HomelandString> strings = new ArrayList<>(25_000);
        Map<Key, List<HomelandString>> canonicalized = new LinkedHashMap<>(50_000);
        for (Rel rel : RELS) {
            try (FileChannel fc = FileChannel.open(base.resolve(rel.rel), READ)) {
                MappedByteBuffer map = fc.map(MapMode.READ_ONLY, 0, fc.size());
                if (rel.rel.equalsIgnoreCase("boot.dol")) {
                    List<HomelandString> bds = new ArrayList<>(25_000);
                    RelSearcher.relStrings(rel.rel, map, rel.ranges, bds);
                    strings.addAll(bds);
                    for (HomelandString hs : bds) {
                        System.out.println("" + toHex(hs.offset) + " " + toHex(hs.length) + " " + hs.string);
                    }
                } else {
                    RelSearcher.relStrings(rel.rel, map, rel.ranges, strings);
                }
            }
        }
        try (FileChannel allbin = FileChannel.open(base.resolve("all.bin"), READ);
                FileChannel bootdol = FileChannel.open(base.resolve("boot.dol"), READ);) {
            AllBinSplitter.allBinStrings("all.bin",
                    allbin.map(MapMode.READ_ONLY, 0, allbin.size()),
                    bootdol.map(MapMode.READ_ONLY, 0, bootdol.size()),
                    strings);
        }
        System.gc();
        for (HomelandString str : strings) {
            Key key = new Key(str.strBytes);
            List<HomelandString> list = canonicalized.get(key);
            if (list == null) {
                list = new ArrayList<>(1);
                canonicalized.put(key, list);
            }
            list.add(str);
        }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            int i = 0;
            final Set<String> ids = new TreeSet<>();
            for (Entry<Key, List<HomelandString>> e : canonicalized.entrySet()) {
//                String string = e.getKey();
                List<HomelandString> list = e.getValue();
                HomelandString hs = list.get(0);
                md.reset();
                byte[] digest = md.digest(hs.strBytes);
                String sha = toHex(digest);
                String id = sha.substring(0, 8);
                if (!ids.add(id)) {
                    throw new RuntimeException("duplicate id " + id);
                }
                id = "hl-string-" + id;
                fos.write(id.getBytes(HomelandString.SHIFT_JIS));
                fos.write("\r\n".getBytes(HomelandString.SHIFT_JIS));
                for (HomelandString h : list) {
                    fos.write(h.file.getBytes(HomelandString.SHIFT_JIS));
                    fos.write("\t".getBytes(HomelandString.SHIFT_JIS));
                    fos.write(toHex(h.offset).getBytes(HomelandString.SHIFT_JIS));
                    fos.write("\r\n".getBytes(HomelandString.SHIFT_JIS));
                }
                fos.write("\r\n".getBytes(HomelandString.SHIFT_JIS));
                fos.write(toHex(hs.length).getBytes(HomelandString.SHIFT_JIS));
                fos.write("\r\n".getBytes(HomelandString.SHIFT_JIS));
                fos.write(hs.strBytes);
//                fos.write("\r\n".getBytes(HomelandString.SHIFT_JIS));
//                byte[] strNumberBytes = sha.getBytes(HomelandString.SHIFT_JIS);
//                fos.write(strNumberBytes, 0, Math.min(strNumberBytes.length, hs.length));
//                for (int j = strNumberBytes.length; j < hs.strBytes.length; j++) {
//                    fos.write(0x3F);
//                }
                fos.write("\r\n".getBytes(HomelandString.SHIFT_JIS));
                i++;
            }
            System.out.println(i+" strings");
        }
    }

    private static class Key {

        private final byte[] key;

        public Key(byte[] key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            if (!Arrays.equals(this.key, other.key)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 71 * hash + Arrays.hashCode(this.key);
            return hash;
        }
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

    public static String toHex(byte[] ba) {
        char[] hex = new char[ba.length * 2];
        for (int i = 0; i < ba.length; i++) {
            int b = ba[i];
            b &= 0xFF;
            hex[2 * i] = HEX[(b >> 4) & 0xF];
            hex[2 * i + 1] = HEX[(b >> 0) & 0xF];
        }
        return new String(hex);
    }
}
