import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Connection extends Thread {

  public enum Connection_State { FULL_CONNECTED, PARTIAL_CONNECTED, NOT_CONNECTED };

  private Connection_State state = Connection_State.NOT_CONNECTED;
  private ObjectOutputStream output_peer;
  private ObjectInputStream input_peer;
  private Socket connection_socket;
  private IP ip;

  public Connection( final IP ip ) { this.ip = ip; state = Connection_State.NOT_CONNECTED; }
  
  public Connection( final Socket connection_socket ) { 
  
    try {

      this.connection_socket = connection_socket;  

      output_peer = new ObjectOutputStream( get_connection_socket().getOutputStream() );
      input_peer = new ObjectInputStream( get_connection_socket().getInputStream() );
      
      state = Connection_State.PARTIAL_CONNECTED;

    } catch ( Exception e ) { System.out.println(e); }
  
  }

  public Connection( final IP ip, final Socket connection_socket ) { 
    
    try {

      this.ip = ip; this.connection_socket = connection_socket; 
      output_peer = new ObjectOutputStream( get_connection_socket().getOutputStream() );
      input_peer = new ObjectInputStream( get_connection_socket().getInputStream() );
      
      state = Connection_State.FULL_CONNECTED;

    } catch ( Exception e ) { System.out.println(e); }
  
  }

  @Override
  public boolean equals( final Object other ) {

    if ( this == other ) return true; // Same object in memory
    if ( other == null ) return false; // Null object given
    if ( other.getClass() != getClass() ) return false; // Not an object from type Connection given

    // Convert object into Connection type
    Connection other_connection = ( Connection ) other;

    IP other_ip = other_connection.get_ip();
    IP this_ip = get_ip();

    return other_ip.get_host().equals( this_ip.get_host() ) && ( other_ip.get_port() == this_ip.get_port() );

  }

  public final boolean is_connected() { return state != Connection_State.NOT_CONNECTED; }

  public final Socket get_connection_socket() { return connection_socket; }

  public final Connection_State get_connection_state() { return state; }

  public synchronized void set_connection_state( final Connection_State state ) { this.state = state; } 

  public synchronized final IP get_ip() { return ip; }

  public synchronized final boolean set_ip( final IP ip ) 
    { if( state != Connection_State.PARTIAL_CONNECTED ) return false; this.ip = ip; return true; }

  // Will try to establish a socket connection
  // with target ip information
  public final boolean socket_connection() {

    if ( is_connected() ) return false;

    try {

      // Open a socket connection with "host:port"
      connection_socket = new Socket( get_ip().get_host() , get_ip().get_port() );

      output_peer = new ObjectOutputStream( get_connection_socket().getOutputStream() );
      input_peer = new ObjectInputStream( get_connection_socket().getInputStream() );

      state = Connection_State.FULL_CONNECTED; return true;
    
    } catch ( Exception e ) { System.out.println(e); }

    return false;

  }

  // Will send a request to set this connection
  // as a connection as well in the peer connected
  public boolean connection_request( IP node_ip ) {

    try {

      // Object to hold the request connection
      // data that will be sent to the peer
      NewConnectionRequest connection_request = new NewConnectionRequest( node_ip );

      send_data( connection_request );

      // TODO: wait for peer response to the connection request
      // while();

      return true;

    } catch ( Exception e ) { System.out.println(e); }

    return false;

  }

  public synchronized boolean send_data( Object object_data ) {

    try { output_peer.writeObject( object_data ); return true; } 
    catch ( Exception e ) { System.out.println(e); }

    return false;
    
  }

  @Override
  public void run() {

    try {

      Object object;

      while( ( object = input_peer.readObject() ) != null ) {

          switch ( object ) {
              
              // Connection Request received
              case NewConnectionRequest connection_request -> {

                // Checks if the connection is at 
                // partial connected state
                if( get_connection_state() != Connection_State.PARTIAL_CONNECTED ) { System.out.println("Connection not partial"); return; } // Implement something more useful TODO:
                
                if( ! set_ip( connection_request.get_node_ip() ) ) { System.out.println("IP setting error !! TODO:"); return; }// TODO:

                set_connection_state( Connection_State.FULL_CONNECTED );

                System.out.println( connection_request.get_node_ip() + " connected!");
                System.out.println(Node.get_instance().get_open_connections().get(0));

              }

              // Word Search Request received
              case WordSearchMessage word_search_message -> {

                List< File_Data > list_files = Node.get_instance().get_files_manager().find_files_by_keyword_name( word_search_message.get_key_word() );
                List< FileSearchResult > list_file_search_result = new ArrayList<>();
                
                for( File_Data file : list_files ) {
                  list_file_search_result.add(file.convert_to_file_search_result(word_search_message, Node.get_instance().get_ip()));
                }
                send_data( list_file_search_result );

              }

              // Receives a list of something
              case List< ? > list -> {

                // Empty list
                if(list.isEmpty()) return;

                // Check which type is the list of
                switch ( list.get( 0 ) ) {

                  // List of File Search Results
                  case FileSearchResult ignore -> {

                    // Need to be this way so the compiler dont warn me agains my unsafe matters
                    List< FileSearchResult > list_file_search_result = 
                      list.stream()
                        .filter( FileSearchResult.class::isInstance )
                          .map( FileSearchResult.class::cast )
                            .toList();

                    FileSearchResult file_search_result = list_file_search_result.get( 0 );

                    List<String> names_list = new ArrayList<>();

                    for (FileSearchResult file : list_file_search_result) {
                      System.out.println(file.get_file_name());
                      names_list.add(file.get_file_name());
                    }

                    Gui.update_list(names_list.toArray(new String[0]));

                    Node.get_instance().get_download_task_manager().add_new_download_task(
                      file_search_result.get_file_name(),
                      file_search_result.get_hash(),
                      ( int ) file_search_result.get_file_size(),
                      Node.get_instance().get_open_connections()
                    );

                  }

                  default -> { System.out.println("Nothing"); }
                    
                }

              }

              case FileBlockRequestMessage file_block_request_message -> {

                final byte[] file_block_data = Node.get_instance().get_files_manager().get_file_block( 
                
                  file_block_request_message.get_file_hash() , 
                  file_block_request_message.get_offset(), 
                  file_block_request_message.get_length() 
                
                );

                final FileBlockAnswerMessage file_block_answer_message = new FileBlockAnswerMessage( file_block_request_message.get_file_hash(), file_block_data );

                send_data( file_block_answer_message );

              }
              
              case FileBlockAnswerMessage file_block_answer_message -> {

                Node.get_instance().get_download_task_manager().new_file_block_answer_received( this, file_block_answer_message );

              }
              default -> System.out.println("Command: " + object);

          }

      }

    } catch (Exception e) { System.out.println("Connection closed"); }

    System.out.println("Saiu");

  }

  @Override
  public String toString() {
    return "Connection{" +
            "state=" + state +
            ", ip=" + ip +
            '}';
  }
}
