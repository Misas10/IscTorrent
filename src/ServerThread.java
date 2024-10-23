import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread{
    Socket socket = null;
    public ServerThread(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        // Do something

        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
