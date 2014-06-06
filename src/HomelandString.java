
import java.nio.charset.Charset;

public class HomelandString {

    public static final Charset SHIFT_JIS = Charset.forName("Shift-Jis");
    public final String file;
    public final int offset;
    public final int length;
    public final String string;
    public final byte[] strBytes;

    public HomelandString(String file, int offset, int length, byte[] strBytes) {
        this.file = file;
        this.offset = offset;
        this.length = length;
        this.string = new String(strBytes, SHIFT_JIS);
        this.strBytes = strBytes;
    }
}
