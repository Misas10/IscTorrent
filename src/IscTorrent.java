import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IscTorrent {

    public static void main(String[] args) {

        // Receives the parameters from CLI
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        // Parameters given
        System.out.println("Host: " + host);
        System.out.println("Port: " + port);

        // Create a node
        if( ! Node.set_instance( host, port ) ) return;
        
        Node node = Node.get_instance();
        node.start();

        System.out.println("\nNew node: " + node);

        new Gui();
    }
}
