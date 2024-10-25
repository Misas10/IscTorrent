import java.security.MessageDigest;

public class FileSearchResult {
  private WordSearchMessage wordSearchMessage;
  private final MessageDigest hash = MessageDigest.getInstance("SHA-256");
}
