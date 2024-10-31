import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/*
This is where all the client work is done
 */

public class ClientThread extends Thread{
    private Socket clientSocket;
    private Node node;

    public ClientThread(Socket socket, Node node) {
        this.clientSocket = socket;
        this.node = node;
    }

    @Override
    public void run() {
        try {

            ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

            outToServer.writeObject(node);
            // outToServer.writeObject(node.getFiles());

            Object object;

            while ((object = inFromServer.readObject()) != null) {
                if(object instanceof ArrayList<?>)
                    System.out.println(object);

                else if(object instanceof Node)
                    node.add((Node) object);

                System.out.println(object);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
