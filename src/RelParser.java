
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

public class RelParser {

    private static final String[] RELS = {//"C:\\Users\\Playtech\\New Folder\\gc\\hl\\alone.rel",
    //"C:\\Users\\Playtech\\New Folder\\gc\\hl\\client.rel",
    //"C:\\Users\\Playtech\\New Folder\\gc\\hl\\server.rel",
    "C:\\Users\\Playtech\\New Folder\\gc\\hl\\start.rel"};
    private static final String OUT_DIR = "C:\\Users\\Playtech\\New Folder\\gc\\hl\\tmp";

    static class Section {

        final int offset;
        final int length;

        public Section(int offset, int length) {
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

                map.position(0x0C);
                int numSections = readInt(map);
                int sectionInfo = readInt(map);
                List<Section> sections = new ArrayList<>(numSections);
                map.position(sectionInfo);
                for (int i = 0; i < numSections; i++) {
                    sections.add(new Section(readInt(map), readInt(map)));
                }
                for (Section s : sections) {
                    byte[] bytes = new byte[s.length];
                    map.position(s.offset);
                    map.get(bytes);
                    Files.write(new File(OUT_DIR).toPath().resolve(rp.getFileName()+".section@" + toHex(s.offset) + "+" + toHex(s.length)), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                }
                map.position(0x24);
                int relocations = readInt(map);
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
