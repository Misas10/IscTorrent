import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Connection {

  public enum Connection_State { CONNECTED, NOT_CONNECTED };


  private Socket connection_socket;
  private Connection_State state;
  private final IP ip;

  public Connection( final IP ip ) { this.ip = ip; state = Connection_State.NOT_CONNECTED; }
  
  public Connection( final IP ip, final Socket connection_socket ) 
    { this.ip = ip; this.connection_socket = connection_socket; state = Connection_State.CONNECTED; }

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

  public final boolean is_connected() { return state == Connection_State.CONNECTED; }

  public final Socket get_connection_socket() { return connection_socket; }

  public final IP get_ip() { return ip; }

  // Will try to connect with the information of
  // this connection ip
  public void connect_with( IP node_ip ) {

    try {

      // Open a socket connection with "host:port"
      connection_socket = new Socket( get_ip().get_host() , get_ip().get_port() );

      // Buffers from input and ouput communication to the connection
      ObjectOutputStream outToServer = new ObjectOutputStream( connection_socket.getOutputStream() );
      // ObjectInputStream inFromServer = new ObjectInputStream( connection_socket.getInputStream() );
      
      // Send a request to inform that this connection must be stable
      outToServer.writeObject( "/connect " + node_ip.toString() );

      state = Connection_State.CONNECTED;
    
    } catch (Exception e) {}

    // ??


    // 
    // node.add(serverSocket.getPort());

    // System.out.println(this + " - [Connection establish with: " + host + ":" + serverSocket.getPort() + "]");


  }



}
