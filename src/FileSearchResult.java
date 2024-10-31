import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileSearchResult {
  private WordSearchMessage wordSearchMessage;
  private byte[] hash;
  private long fileSize;
  private String fileName;
  private int port;
  private String address;

    public FileSearchResult(
            WordSearchMessage wordSearchMessage,
            byte[] hash,
            String fileName,
            long fileSize,
            String address,
            int port
    ) {

      this.wordSearchMessage = wordSearchMessage;
      this.hash = hash;
      this.fileName = fileName;
      this.port = port;
      this.address = address;
      this.fileSize = fileSize;
    }
}
