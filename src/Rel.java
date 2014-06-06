
public class Rel {

    public String rel;
    //odd is start, inclusive
    //even is end, exclusive
    //odd is start, inclusive
    //even is end, exclusive
    public RelRange[] ranges;

    public Rel(String rel, RelRange[] ranges) {
        this.rel = rel;
        this.ranges = ranges;
    }
}
