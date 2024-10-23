import java.io.*;
import java.net.*;

public class Server {
    // private static final String host = Constants.HOST;
    private final int port;
    public Server(int port){
        this.port = port;
    }

    public void listen() throws IOException {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        System.out.println("Server Listening......");
        serverSocket = new ServerSocket(port);

        // Wait for a client to connect
        while (true){
            clientSocket = serverSocket.accept();
            System.out.println("Connected in port: " + clientSocket.getPort());
            ServerThread st = new ServerThread(clientSocket);
            st.start();
        }
        // System.out.println("Client connected in: " + clientSocket.getPort());
    }
}
