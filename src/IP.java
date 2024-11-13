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

    public final String get_host() { return host; }

    public final int get_port() { return port; }

    @Override
    public String toString() { return host + ":" + String.valueOf( port ); }

}
