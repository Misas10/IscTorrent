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

            // ObjectOutputStream outToClient = new ObjectOutputStream( clientSocket.getOutputStream() );
            ObjectInputStream inFromClient = new ObjectInputStream( clientSocket.getInputStream() );

            Object object;

            while( ( object = inFromClient.readObject() ) != null ) {

                switch ( object ) {
                    
                    // Connection Request received
                    case NewConnectionRequest connection_request -> {}

                    default -> {}

                }

            }

        } catch (Exception e) { throw new RuntimeException(e); }

    }

}


/*
 * String client_input_data = ( String ) object;
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
 */