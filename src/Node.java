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
    private final IP node_ip;
    // private final Server server;
    private final List<Integer> connected_servers = new ArrayList<>(); // ??

    private final List< Connection > open_connections = new ArrayList<>();

    public Node(String host, int port) { node_ip = new IP( host, port ); }

    @Override
    public void run() {

        try {
            new Server(this).listen();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    public int getPort() {
        return node_ip.get_port();
    }

    public String getHost() {
        return node_ip.get_host();
    }

    public void add(int node) {
        this.connected_servers.add(node);
    }

    public void add_new_open_connection_to_list( final Connection open_connection ) 
        { open_connections.add( open_connection ); }

    public List<Integer> getConnected_servers() {
      return connected_servers;
    }

    public void connect(String host, int port) {

        Connection new_connection = new Connection( new IP( host, port ) );

        if( ! open_connections.contains( new_connection ) ) {

            // Tries to connect with given ip 
            // information
            new_connection.connect_with( node_ip );

            if ( new_connection.is_connected() ) { open_connections.add( new_connection ); return; }
        
        }

        System.out.println(port + " is already connected!\n");
        
    }

    public void search(String text) { new WordSearchMessage(text, this); }

    public String getFolderPath() { return Constants.FOLDER_PATH + (this.getPort() - 8080); }

    @Override
    public String toString() {
        return "Node {" +
                "host='" + getHost() + '\'' +
                ", port=" + getPort() +
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
