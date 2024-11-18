import java.io.Serializable;

/*
I'm not really sure what this class does
something about searching a keyword in others nodes, I guess...
 */

public class WordSearchMessage implements Serializable {

  private final String key_word;

  public WordSearchMessage( final String key_word ) { this.key_word = key_word; }

  public final String get_key_word() { return key_word; }

}
