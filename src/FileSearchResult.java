import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileSearchResult implements Serializable {
  
  private final WordSearchMessage word_search_message;
  private final byte[] hash;
  private final long file_size;
  private final String file_name;
  private final IP node_ip;

  public FileSearchResult(
    final WordSearchMessage word_search_message,
    final byte[] hash,
    final long file_size,
    final String file_name,
    final IP node_ip
  ) {

    this.word_search_message = word_search_message;
    this.hash = hash;
    this.file_size = file_size;
    this.file_name = file_name;
    this.node_ip = node_ip;

  }

  public final WordSearchMessage get_word_search_message() { return word_search_message; }

  public final byte[] get_hash() { return hash; }
  
  public final long get_file_size() { return file_size; }

  public final String get_file_name() { return file_name; }
  
  public final IP get_node_ip() { return node_ip; }

}
