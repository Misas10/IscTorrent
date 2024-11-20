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

        try {
            // Create a directory to store all the music file, and it creates only if is NOT already created
            Files.createDirectories(Paths.get(node.getFolderPath()));

        } catch (IOException e) {
            System.out.println("Error creating the folder: " + e.getMessage());
        }

        new Gui();
    }
}
