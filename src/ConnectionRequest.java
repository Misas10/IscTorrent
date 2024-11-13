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

public class ConnectionRequest implements Serializable {
  
  final IP node_ip;

  public ConnectionRequest( final IP node_ip ) { this.node_ip = node_ip; }
  
}
