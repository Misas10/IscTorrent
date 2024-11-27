import java.util.List;

public class Rofly {

    private final byte[] hash;
    private final long file_size;
    private final String file_name;
    private final List< Connection > connections;

    public Rofly( final byte[] hash, final long file_size, final String file_name, final List< Connection > connections ) 
        { this.hash = hash; this.file_size = file_size; this.file_name = file_name; this.connections = connections; }

    public final byte[] get_hash() { return hash; }

    public final long get_file_size() { return file_size; }
    
    public final String get_file_name() { return file_name; }
    
    public final List< Connection > get_connections() { return connections; }

    public void add_new_connection( final Connection new_connection ) { synchronized( connections ) { connections.add( new_connection ); }}

    @Override
    public String toString() { return get_file_name(); }

}