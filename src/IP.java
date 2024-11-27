import java.io.Serializable;

public class IP implements Serializable {

    private final String host;
    private final int port;

    public IP( final String host, final int port ) { this.host = host; this.port = port; }

    public IP( final String ip_representation ) {

        String[] ip_args = ip_representation.split( ":" );

        if( ip_args.length != 2 ) { this.host = null; this.port = -1; return; }

        this.host = ip_args[ 0 ];
        this.port = Integer.parseInt( ip_args[ 1 ] );

    }


    @Override
    public boolean equals( final Object other ) {

    if ( this == other ) return true; // Same object in memory
    if ( other == null ) return false; // Null object given
    if ( other.getClass() != getClass() ) return false; // Not an object from type Connection given

    // Convert object into Connection type
    IP other_ip = ( IP ) other;

    return other_ip.get_host().equals( get_host() ) && ( other_ip.get_port() == get_port() );

    }

    public final String get_host() { return host; }

    public final int get_port() { return port; }

    @Override
    public String toString() { return host + ":" + String.valueOf( port ); }

}
