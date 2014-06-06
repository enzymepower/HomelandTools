
public class RelRange {

    public int offset;
    public int end;

    public RelRange(int offset, int length) {
        this.offset = offset;
        this.end = offset + length;
    }
}
