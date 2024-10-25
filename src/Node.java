import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Node extends Thread{
    // private int id;
    private final String host;
    private final int port;
    private final Server server;
    // private final Client client;
    private List<Socket> connected_sockets;

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

    public List<Socket> getConnected_sockets() {
      return connected_sockets;
    }

    public void connect(String host, int port) {  new NewConnectionRequest(host, port); }

	  public void search(String text) { new WordSearchMessage(text, this); }

    @Override
    public String toString() {
        return "Node {" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

}
