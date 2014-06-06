
import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Patcher {

    private static final Charset SHIFT_JIS = Charset.forName("Shift-Jis");
    private static final String[] TEXTS = {
        "C:\\Users\\Playtech\\Documents\\GitHub\\HomelandStrings\\all.bin.texts",
        "C:\\Users\\Playtech\\Documents\\GitHub\\HomelandStrings\\alone.rel.texts",
        "C:\\Users\\Playtech\\Documents\\GitHub\\HomelandStrings\\client.rel.texts",
        "C:\\Users\\Playtech\\Documents\\GitHub\\HomelandStrings\\server.rel.texts",
        "C:\\Users\\Playtech\\Documents\\GitHub\\HomelandStrings\\start.rel.texts",
        "C:\\Users\\Playtech\\Documents\\GitHub\\HomelandStrings\\boot.dol.texts",};

    public static void dumpEntries(String[] args) throws IOException {
        for (int i = 0; i < TEXTS.length; i++) {
            String t = TEXTS[i];
            System.out.println(t);
            byte[] data = Files.readAllBytes(new File(t).toPath());
            List<TextsParser.Entry> entries = TextsParser.parse(data);
            for (TextsParser.Entry e : entries) {
                System.out.println("\t" + e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        final String targetdir = args[0];//"C:\\Users\\Playtech\\New folder\\gc\\hltmp";
        final String texts = args[1];//"C:\\Users\\Playtech\\Documents\\GitHub\\HomelandStrings\\texts.dump";
        Path hlDir = Paths.get(targetdir);
        byte[] data = Files.readAllBytes(new File(texts).toPath());
        List<TextsParser.Entry> entries = TextsParser.parse(data);
        for (TextsParser.Entry entry : entries) {
            System.out.println(entry);
        }
        List<FileChannel> toClose = new ArrayList<>(5);
        Map<String, MappedByteBuffer> maps = new HashMap<>(10);
        try {
            for (TextsParser.Entry e : entries) {
                System.out.println("patching string " + e.id);
                for (Entry<String, List<Integer>> entry : e.offsets.entrySet()) {
                    String file = entry.getKey();
                    List<Integer> offsets = entry.getValue();
                    MappedByteBuffer mm = maps.get(file);
                    if (mm == null) {
                        FileChannel fc = FileChannel.open(hlDir.resolve(file), StandardOpenOption.WRITE, StandardOpenOption.READ);
                        toClose.add(fc);
                        mm = fc.map(MapMode.READ_WRITE, 0, fc.size());
                        maps.put(file, mm);
                    }
                    for (Integer off : offsets) {
                        mm.position(off);
                        mm.put(e.replacementText);

                    }
                }
                System.out.println("\tpatched string " + e.id);
            }
            for (Entry<String, MappedByteBuffer> entry : maps.entrySet()) {
                entry.getValue().force();
            }
        } finally {
            for (FileChannel fc : toClose) {
                fc.close();
            }
        }
    }
}
