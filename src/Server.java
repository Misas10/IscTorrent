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
        serverSocket = new ServerSocket(node.getPort());

        // Wait for a client to connect
        while (true){
            clientSocket = serverSocket.accept();

            // After connection receive from "clientSocket"
            // Add to connected sockets
            node.add(clientSocket);

            System.out.println("Connected in port: " + clientSocket.getPort());
            ServerThread st = new ServerThread(clientSocket, node);
            st.start();
        }
    }
}
