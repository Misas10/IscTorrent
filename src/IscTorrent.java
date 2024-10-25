import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IscTorrent {

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Node node = new Node(host, port);
        node.start();

        System.out.println("\nNew node: " + node);

        try {
            Files.createDirectories(Paths.get("../folders/dl" + (port - 8080)));
        } catch (IOException e) {
            System.out.println("Error creating the folder: " + e.getMessage());
        }

        new Gui(node);
    }
}
