import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*
A class to accept new connections requests from the socket (AKA server)
 */

public class NewConnectionRequest {
  private String host;
  private int port;
  private Node node;

  public NewConnectionRequest(String host, int port, Node node) {
    this.host = host; 
    this.port = port;
    this.node = node;

    try {
        // Open a socket connection with "host:port"
		    Socket socket = new Socket(host, port);
        node.add(socket);

        System.out.println(this + " - [Connection establish with: " + host + ":" + socket.getPort() + "]");

        // Handles all the work in another thread
        new ClientThread(socket).start();

        // socket.close();
	} catch (Exception e) {
		e.printStackTrace();
	} 
  }
}
