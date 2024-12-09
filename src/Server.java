import java.io.*;
import java.net.*;

public class Server {
  
    private final Node node;

    public Server(Node node){

      this.node = node;
    
    }

    public void listen() throws IOException {
        
      ServerSocket serverSocket = null;
      Socket clientSocket = null;

      System.out.println("Server Listening......");

      serverSocket = new ServerSocket( node.get_ip().get_port() );
      
      // Wait for a client to connect
      while ( true ) {

        clientSocket = serverSocket.accept(); Connection new_connection = new Connection( clientSocket );

        Node.get_instance().add_new_open_connection_to_list( new_connection ); new_connection.start();

      }

      // TODO: close the fucking socket maybe :)
      // serverSocket.close();

    }
}
