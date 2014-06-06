
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class DolParser {
    
    private static final String[] RELS = {"C:\\Users\\Playtech\\New Folder\\gc\\hl\\boot.dol"};
    private static final String OUT_DIR = "C:\\Users\\Playtech\\New Folder\\gc\\hl\\tmp";
    
    static class Section {
        
        final String type;
        final int offset;
        final int length;
        
        public Section(String type, int offset, int length) {
            this.type = type;
            this.offset = offset;
            this.length = length;
        }
    }
    
    public static void main(String[] args) throws IOException {
        for (String rel : RELS) {
            Path rp = new File(rel).toPath();
            try (FileChannel fc = FileChannel.open(rp, StandardOpenOption.READ)/*
                     * ;
                     * FileOutputStream textOut = new FileOutputStream(new
                     * File(outTexts))
                     */) {
                Files.createDirectories(new File(OUT_DIR).toPath());
                MappedByteBuffer map = fc.map(MapMode.READ_ONLY, 0, fc.size());
                List<Section> sections = new ArrayList<>(18);
                for (int i = 0; i < 7; i++) {
                    map.position(0x00 + 4 * i);
                    int offset = readInt(map);
                    map.position(0x90 + 4 * i);
                    int size = readInt(map);
                    sections.add(new Section("code", offset, size));
                }
                for (int i = 0; i < 11; i++) {
                    map.position(0x1C + 4 * i);
                    int offset = readInt(map);
                    map.position(0xAC + 4 * i);
                    int size = readInt(map);
                    sections.add(new Section("data", offset, size));
                }
                for (Section s : sections) {
                    byte[] bytes = new byte[s.length];
                    map.position(s.offset);
                    map.get(bytes);
                    Files.write(new File(OUT_DIR).toPath().resolve(rp.getFileName() + ".section." + s.type + "@" + toHex(s.offset) + "+" + toHex(s.length)), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                }
//                map.position(0x24);
//                int relocations = readInt(map);
            }
        }
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
