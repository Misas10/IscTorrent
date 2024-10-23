import java.io.IOException;
import java.net.Socket;

public class Node extends Thread{
    // private int id;
    private final String host;
    private final int port;
    private final Server server;
    // private final Client client;
    private Socket socket = null;

    public Node(String host, int port) {
        this.host = host;
        this.port = port;

        this.server = new Server(port);
    }

    @Override
    public void run() {
        try {
            server.listen();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            System.out.println(this + " - [Connection establish with: " + host + ":" + port + "]");

            socket.close();
        } catch (IOException e) {
           System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Node {" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
