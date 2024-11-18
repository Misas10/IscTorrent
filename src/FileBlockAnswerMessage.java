import java.io.Serializable;

public class FileBlockAnswerMessage  implements Serializable {

    private final byte[] hash;
    private final byte[] data;

    public FileBlockAnswerMessage( final byte[] hash, final byte[] data ) 
        { this.hash = hash; this.data = data; }

    public final byte[] get_hash() { return hash; }

    public final byte[] get_data() { return data; }
 
}
