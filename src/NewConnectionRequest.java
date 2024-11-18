import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*
A class to accept new connections requests from the socket (AKA server)
 */

public class NewConnectionRequest implements Serializable {
  
  private final IP node_ip;

  public NewConnectionRequest( final IP node_ip ) { this.node_ip = node_ip; }
  
  public final IP get_node_ip() { return node_ip; }

}
