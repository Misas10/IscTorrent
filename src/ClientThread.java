import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/*
This is where all the client work is done
 */

public class ClientThread extends Thread{
    private Socket clientSocket;

    public ClientThread(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {

            ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

            Object object;

            while ((object = inFromServer.readObject()) != null) {
                if(object instanceof ArrayList<?>)
                    System.out.println(object);

                System.out.println(object);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
