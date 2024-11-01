import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        Socket serverSocket = new Socket(host, port);

        ObjectOutputStream outToServer = new ObjectOutputStream(serverSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(serverSocket.getInputStream());

        outToServer.writeObject("/connect " + node.getPort());
        node.add(serverSocket.getPort());

        System.out.println(this + " - [Connection establish with: " + host + ":" + serverSocket.getPort() + "]");

        // Handles all the work in another thread
        new ClientThread(serverSocket).start();

        // socket.close();
	} catch (Exception e) {
		e.printStackTrace();
	} 
  }
}
