import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*
I mean... is the node!

Extends a Thread, so it can waint other connections on another thread

Implements a Serializable to be able to send Nodes tru the sockets
(Only accepts Serializable classes, with Serializable parameters)
 */

public class Node extends Thread implements Serializable {
    // private int id;
    private final String host;
    private final int port;
    // private final Server server;
    private final List<Integer> connected_servers = new ArrayList<>();

    public Node(String host, int port) {
        this.host = host;
        this.port = port;


        // this.server = new Server(this);
    }

    @Override
    public void run() {
        try {
            new Server(this).listen();
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

    public void add(int node) {
        this.connected_servers.add(node);
    }

    public List<Integer> getConnected_servers() {
      return connected_servers;
    }

    public void connect(String host, int port) {
        if(!connected_servers.contains(port))
            new NewConnectionRequest(host, port, this);

        else
            System.out.println(port + " is already connected!\n");
    }

    public void search(String text) { new WordSearchMessage(text, this); }

    public String getFolderPath() { return Constants.FOLDER_PATH + (this.getPort() - 8080); }

    @Override
    public String toString() {
        return "Node {" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    // It gets all the files from the default folder for this Node
    public List<String> getFiles() {

        File folder = new File(getFolderPath());
        File[] files = folder.listFiles();

        if(files == null)

            return null;

        List<String> f = new ArrayList<>();

        for(File file : files)
            f.add(file.getName());

        return f;

    }

    // For testing
    public static void main(String[] args) {

        Node node = new Node("localhost", 8081);
        System.out.println(System.getProperty("user.dir") + node.getFolderPath());
        System.out.println(node.getFolderPath());

        System.out.println(node.getFiles());

        File folder = new File("./folders");
        File[] files = folder.listFiles();

        List<String> f = new ArrayList<>();

        assert files != null;
        for(File file : files)
            f.add(file.getName());

        System.out.println(f);
    }
}
