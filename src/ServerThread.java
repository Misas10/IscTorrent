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

public class ServerThread extends Thread {

    private Socket clientSocket = null;
    private final Node node;

    public ServerThread(Socket s, Node node) {
        this.clientSocket = s;
        this.node = node;
    }

    @Override
    public void run() {

        try {
            // Socket socket = node;

            ObjectOutputStream outToClient = new ObjectOutputStream( clientSocket.getOutputStream() );
            ObjectInputStream inFromClient = new ObjectInputStream( clientSocket.getInputStream() );

            Object object;

            // if(inFromClient.available() != 0)

            while( ( object = inFromClient.readObject() ) != null ) {

                String client_input_data = ( String ) object;
                String[] client_input_data_args = client_input_data.split( " " );

                switch ( client_input_data_args[ 0 ] ) {

                    case "/file": {
                        String fileName = client_input_data_args[1];
                        System.out.println(fileName);
                    }
                    case "/files": {
                        System.out.println("Request to send all the files!!");

                        System.out.println(node);
                        System.out.println(node.getFiles());
                        outToClient.writeObject(node.getFiles());
                    }
                    case "/connect": {

                        node.add_new_open_connection_to_list(
                            new Connection( new IP( client_input_data_args[ 1 ] ) , clientSocket )
                        );

                    }

                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);

            /*System.err.println("Server Error: " + e.getMessage());
            System.err.println("Localized: " + e.getLocalizedMessage());
            System.err.println("Stack Trace: " + e.getStackTrace());
            System.err.println("To String: " + e.toString());*/
        }
    }
}
