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
    private final List<Socket> connected_sockets = new ArrayList<>();

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

    public void add(Socket node) {
        this.connected_sockets.add(node);
    }

    public List<Socket> getConnected_sockets() {
      return connected_sockets;
    }

    public void connect(String host, int port) { new NewConnectionRequest(host, port, this); }

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

        File[] files = new File(getFolderPath()).listFiles();

        if(files == null)
            return null;

        List<String> f = new ArrayList<>();

        for(File file : files)
            f.add(file.getName());

        return f;

    }
}
