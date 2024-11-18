import java.io.Serializable;

public class FileBlockRequestMessage implements Serializable {

    private final byte[] file_hash;
    private final int length;
    private final int offset;

    public FileBlockRequestMessage( final byte[] file_hash, final int length, final int offset ) 
        { this.file_hash = file_hash; this.length = length; this.offset = offset; }

    public final byte[] get_file_hash() { return file_hash; }

    public final int get_length() { return length; }
    
    public final int get_offset() { return offset; }

}
