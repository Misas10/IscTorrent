import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/*
This is where all the server work is done
 */

public class ServerThread extends Thread{
    private Socket clientSocket = null;
	  private final Node node;

    public ServerThread(Socket s, Node node) {
        this.clientSocket = s;
        this.node = node;
    }

    @Override
    public void run() {

        // ObjectOutputStream outToClient;
        // ObjectInputStream inFromClient;

        try {
            // Socket socket = node;

            ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());

            Object object;

            // if(inFromClient.available() != 0)

            while((object = inFromClient.readObject()) != null){

                switch (object) {
                    case ArrayList<?> objects -> System.out.println(object);

                    // Commands that can be received from the client
                    case String line -> {
                        String[] options = line.split(" ");

                        switch (options[0]) {
                            case "/file" -> {
                                String fileName = options[1];
                                System.out.println(fileName);
                            }
                            case "/files" -> {
                                System.out.println("Request to send all the files!!");

                                System.out.println(node);
                                System.out.println(node.getFiles());
                                outToClient.writeObject(node.getFiles());
                            }
                            case "/connect" -> {
                                int port = Integer.parseInt(line.split(" ")[1]);
                                node.add(port);
                            }
                        }

                            System.out.println(object);
                    }

                    default -> {
                    }
                }

                // System.out.println(object);
            } // while (inFromClient.available() > 0);

            // outToClient.reset();
            // inFromClient.reset();

        } catch (Exception e) {
            throw new RuntimeException(e);

            /*System.err.println("Server Error: " + e.getMessage());
            System.err.println("Localized: " + e.getLocalizedMessage());
            System.err.println("Stack Trace: " + e.getStackTrace());
            System.err.println("To String: " + e.toString());*/
        }
    }
}
