import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/*
I'm not really sure what this class does
something about searching a keyword in others nodes, I guess...
 */

public class WordSearchMessage {
  public final String keyWord;
  public final Node node;
  private final List<FileSearchResult> fileSearchResults = new ArrayList<>();

  public WordSearchMessage(String kw, Node node) {
    System.out.println(kw);
    this.keyWord = kw;
    this.node = node;

    try {
      // For every connected Node
      System.out.println(node.getConnected_servers());

      for (int port : node.getConnected_servers()) {

        // Opens a socket connection
        // (Closes inside the "SearchThread" class)
        Socket socket = new Socket(node.getHost(), port);

        // Search on a separete thread
        new SearchThread(socket, fileSearchResults, this).start();

        // outToServer.reset();
        // inFromServer.reset();
      }

    } catch (Exception e){
        throw new RuntimeException(e);
      // e.printStackTrace();
    }
  }

  public List<FileSearchResult> getFileSearchResults() { return fileSearchResults; }

}
