import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;  // Import the File class

public class File_Data {

    private byte[] hash = null;       // File sha256 hash
    private long file_size = -1;      // File size
    private String file_name = null;  // File name
    private String file_path = null;  // File path
    private byte[] data = null;       // File data

    public File_Data( final Path file_path ) { // TODO: Better code pls and final

      try {

        data = Files.readAllBytes( file_path );
        
        this.file_path = file_path.toString(); file_name = this.file_path.split("/")[ this.file_path.split("/").length - 1 ];
        hash = MessageDigest.getInstance( "SHA-256" ).digest( data );
        file_size = data.length;

      } catch (Exception e) {}

    }

    @Override
    public final boolean equals( final Object other ) {

      if ( this == other ) return true; // Same object in memory
      if ( other == null ) return false; // Null object given
      if ( other.getClass() != getClass() ) return false; // Not an object from type File Data given

      File_Data file_data = ( File_Data ) other;

      return Arrays.equals( get_hash(),  file_data.get_hash() );

    }

    public final boolean is_valid() { return file_size != -1; }

    public final byte[] get_hash() { return hash; }

    public final long get_file_size() { return file_size; }
    
    public final String get_file_name() { return file_name; }
    
    public final String get_file_path() { return file_path; }
    
    public final byte[] get_data() { return data; }

    public final FileSearchResult convert_to_file_search_result( final WordSearchMessage word_search_message, final IP node_ip )
      { return new FileSearchResult( word_search_message, hash, file_size, file_name, node_ip ); }

}
