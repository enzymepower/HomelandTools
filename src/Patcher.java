
import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

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
        final String target = "C:\\Users\\Playtech\\New folder\\gc\\hltmp\\start.rel";
        final String texts = "C:\\Users\\Playtech\\Documents\\GitHub\\HomelandStrings\\start.rel.texts";
        byte[] data = Files.readAllBytes(new File(texts).toPath());
        List<TextsParser.Entry> entries = TextsParser.parse(data);
        Path tPath = new File(target).toPath();
        try (FileChannel fc = FileChannel.open(tPath, StandardOpenOption.WRITE,StandardOpenOption.READ)) {
            MappedByteBuffer map = fc.map(MapMode.READ_WRITE, 0, fc.size());
            for (TextsParser.Entry e : entries) {
                map.position(e.offset);
                map.put(e.replacementText);
            }
            map.force();
        }
    }
}
