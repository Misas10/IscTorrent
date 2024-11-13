import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection extends Thread {

  public enum Connection_State { FULL_CONNECTED, PARTIAL_CONNECTED, NOT_CONNECTED };

  private Socket connection_socket;
  private Connection_State state;
  private IP ip;

  public Connection( final IP ip ) { this.ip = ip; state = Connection_State.NOT_CONNECTED; }
  
  public Connection( final Socket connection_socket ) { this.connection_socket = connection_socket; state = Connection_State.PARTIAL_CONNECTED; }

  public Connection( final IP ip, final Socket connection_socket ) 
    { this.ip = ip; this.connection_socket = connection_socket; state = Connection_State.FULL_CONNECTED; }

  @Override
  public boolean equals( final Object other ) {

    if ( this == other ) return true; // Same object in memory
    if ( other == null ) return false; // Null object given
    if ( other.getClass() != getClass() ) return false; // Not an object from type Connection given

    // Convert object into Connection type
    Connection other_connection = ( Connection ) other;

    IP other_ip = other_connection.get_ip();
    IP this_ip = get_ip();

    return other_ip.get_host().equals( this_ip.get_host() ) && ( other_ip.get_port() == this_ip.get_port() );

  }

  public final boolean is_connected() { return state != Connection_State.NOT_CONNECTED; }

  public final Socket get_connection_socket() { return connection_socket; }

  public final Connection_State get_connection_state() { return state; }

  public synchronized final IP get_ip() { return ip; }

  // Will try to establish a socket connection
  // with target ip information
  public final boolean socket_connection() {

    if ( is_connected() ) return false;

    try {

      // Open a socket connection with "host:port"
      connection_socket = new Socket( get_ip().get_host() , get_ip().get_port() );

      state = Connection_State.FULL_CONNECTED; return true;
    
    } catch ( Exception e ) { System.out.println(e); }

    return false;

  }

  // Will send a request to set this connection
  // as a connection as well in the peer connected
  public boolean connection_request( IP node_ip ) {

    try {

      // Buffers from input and ouput communication to the connection
      ObjectOutputStream peer_output = new ObjectOutputStream( connection_socket.getOutputStream() );
      // ObjectInputStream inFromServer = new ObjectInputStream( connection_socket.getInputStream() );

      // Object to hold the request connection
      // data that will be sent to the peer
      ConnectionRequest connection_request = new ConnectionRequest( node_ip );

      // Send the connection request
      peer_output.writeObject( connection_request );

      // TODO: wait for peer response to 
      // the connection request

      return true;

    } catch ( Exception e ) { System.out.println(e); }

    return false;

  }

  @Override
  public void run() {

    try {

      ObjectInputStream input_peer = new ObjectInputStream( connection_socket.getInputStream() );

      Object object;

      while( ( object = input_peer.readObject() ) != null ) {

          switch ( object ) {
              
              // Connection Request received
              case ConnectionRequest connection_request -> {

                // Checks if the connection is at 
                // partial connected state
                if( get_connection_state() != Connection_State.PARTIAL_CONNECTED ) { break; } // Implement something more useful TODO:
                
                // Adds the new node and information about it

              }

              default -> { System.out.println("Nothing"); }

          }

      }

    } catch (Exception e) { throw new RuntimeException(e); }

  }

}
