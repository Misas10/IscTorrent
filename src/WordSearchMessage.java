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
      // Goes to all of the conencted nodes
      for (Socket socket : node.getConnected_sockets()) {

        ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());

        // Get the files to search
        outToServer.writeObject("/files");

        List<String> fileNames = new ArrayList<>();

        for (String fileName : fileNames) {
          // Filter the files in the connected node
          if (fileName.contains(kw)) {

            outToServer.writeObject("/file " + fileName);
            // Object object = inFromServer.readObject();
            File file;

            // if(object instanceof File)
               // file = (File) object;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] encondedHash = new byte[0]; // = digest.digest(file);

            FileSearchResult fileSearchResult = new FileSearchResult(
                    this,
                    encondedHash,
                    fileName,
                    0,// file.length(),
                    node.getHost(),
                    socket.getPort()
            );

            fileSearchResults.add(fileSearchResult);
            System.out.println("[Found: '" + fileName + "' in: " + socket.getPort() + "]");
          }
        }

        outToServer.reset();
        inFromServer.reset();
        // socket.close();
      }

    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public List<FileSearchResult> getFileSearchResults() { return fileSearchResults; }

}
