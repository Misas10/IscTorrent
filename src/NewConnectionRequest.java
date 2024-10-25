import java.io.IOException;
import java.net.Socket;

public class NewConnectionRequest {
  private String host;
  private int port;

  public NewConnectionRequest(String host, int port) {
    this.host = host; 
    this.port = port;

    try {
		Socket socket = new Socket(host, port);
    System.out.println(this + " - [Connection establish with: " + host + ":" + port + "]");

    socket.close();
	} catch (IOException e) {
		e.printStackTrace();
	} 
  }
}
