
import java.util.Arrays;

class Key {

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

    @Override
    public String toString() {
        return new String(key, HomelandString.SHIFT_JIS);
    }
    
}
