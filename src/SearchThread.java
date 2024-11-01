import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class SearchThread extends Thread{
    private final Socket socket;
    private final  Node node;
    private final List<FileSearchResult> fileSearchResults;
    private final WordSearchMessage wordSearchMessage;

    public SearchThread(Socket socket, List<FileSearchResult> fileSearchResults, WordSearchMessage wordSearchMessage) {

        this.socket = socket;
        this.node = wordSearchMessage.node;
        this.fileSearchResults = fileSearchResults;
        this.wordSearchMessage = wordSearchMessage;
    }
    @Override
    public void run() {
        try {

            ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());

            // Get the files to search
            outToServer.writeObject("/files");
            List<String> fileNames = new ArrayList<>();

            Object object = inFromServer.readObject();
            if(object instanceof ArrayList<?>)
                fileNames = (ArrayList<String>) object;

            for (String fileName : fileNames) {
                // Filter the files in the connected node
                if (fileName.contains(wordSearchMessage.keyWord)) {

                    outToServer.writeObject("/file " + fileName);
                    // Object object = inFromServer.readObject();
                    File file;

                    // if(object instanceof File)
                    // file = (File) object;

                    MessageDigest digest = MessageDigest.getInstance("SHA-256");

                    byte[] encondedHash = new byte[0]; // = digest.digest(file);

                    FileSearchResult fileSearchResult = new FileSearchResult(
                            wordSearchMessage,
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

            socket.close();
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
