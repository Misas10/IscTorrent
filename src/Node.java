import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
I mean... is the node!

Extends a Thread, so it can waint other connections on another thread

Implements a Serializable to be able to send Nodes tru the sockets
(Only accepts Serializable classes, with Serializable parameters)
 */

public class Node extends Thread implements Serializable {

    private static Node instance;

    private final IP node_ip; // Ip in used by this Node
    private final List< Connection > open_connections = new ArrayList<>(); // List of open connections
    private final List< Rofly > files_search_results = new ArrayList<>(); // Data about files that were searched
    private final DownloadTasksManager download_task_manager; // Manager for downloading files
    private final Files_Manager files_manager; // Files manager

    public Node( String host, int port ) { node_ip = new IP( host, port ); download_task_manager = new DownloadTasksManager(); files_manager = new Files_Manager( getFolderPath() ); files_manager.start(); }

    static public boolean set_instance( String host, int port ) { if ( instance != null ) return false; instance = new Node( host, port ); return true; }

    public static Node get_instance() { return instance; }


    @Override
    public void run() {

        try {
            new Server(this).listen();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    public final IP get_ip() { return node_ip; } // No need for sync is final

    public final List< Connection > get_open_connections() { return open_connections; }

    public final DownloadTasksManager get_download_task_manager() { return download_task_manager; }

    public final Files_Manager get_files_manager() { return files_manager; }

    public void remove_open_connection_to_list( final Connection connection ) 
        { synchronized( open_connections ) { open_connections.remove( connection ); } }

    public void add_new_open_connection_to_list( final Connection open_connection ) 
        { synchronized( open_connections ) { open_connections.add( open_connection ); } }

    public Connection get_connection_by_ip( final IP connection_ip ) {

        synchronized( open_connections ) {

            for( Connection connection : open_connections ) {

                System.out.println(connection.get_ip().get_host() + " " + connection.get_ip().get_port());
                System.out.println(connection_ip.get_host() + " " + connection_ip.get_port());
                System.out.println("----------SEARCH-------------------");
                if( connection.get_ip().equals( connection_ip ) ) return connection;

            }

        }

        return null;

    }

    public void connect(String host, int port) {

        Connection new_connection = new Connection( new IP( host, port ) );

        if( ! open_connections.contains( new_connection ) ) {

            if ( new_connection.socket_connection() && new_connection.connection_request( get_ip() ) ) {
        
                add_new_open_connection_to_list( new_connection ); new_connection.start();

                return;
            
            }
        
        }

        System.out.println(port + " is already connected!\n");
        
    }

    public void search( String text ) {

        WordSearchMessage word_search_message = new WordSearchMessage( text );

        if(word_search_message.get_key_word().isEmpty()){
            Gui.showInfo("The search is empty, try again!");
            return;
        }

        if(open_connections.isEmpty()){
            Gui.showInfo("There's no nodes connected, try connecting to one!");
            return;
        }

        System.out.println("Searching: " + word_search_message.get_key_word());

        synchronized( open_connections ) {

            for( Connection connection : open_connections ) connection.send_data( word_search_message );

        }

    }

    public void new_download_request( final Rofly new_download_request ) {

        download_task_manager.add_new_download_task(
            new_download_request.get_file_name(),
            new_download_request.get_hash(),
            ( int ) new_download_request.get_file_size(),
            new_download_request.get_connections()
        );

    }

    public void clear_file_search_list() {

        synchronized( files_search_results ) {
        
            files_search_results.clear();

             Gui.update_list(files_search_results);
        
        }

    }

    public void add_new_file_search( final FileSearchResult file_search_result ) {

        Connection connection_file_search_result = get_connection_by_ip( file_search_result.get_node_ip() );
        if( connection_file_search_result == null ) { System.out.println("File search received from not connected connection error"); return; } // Connection is no longer available

        synchronized( files_search_results ) {

            for( Rofly file : files_search_results ) {

                if( Arrays.equals( file.get_hash(), file_search_result.get_hash() ) ) {

                    file.add_new_connection( connection_file_search_result );

                    Gui.update_list( files_search_results );

                    return;

                }

            }

            List< Connection > connections = new ArrayList<>();
            connections.add( connection_file_search_result );

            files_search_results.add(
                new Rofly( 
                    file_search_result.get_hash(), 
                    file_search_result.get_file_size(), 
                    file_search_result.get_file_name(), 
                    connections
                )
            );

            Gui.update_list( files_search_results );

        }

    }

    public String getFolderPath() { return Constants.FOLDER_PATH + ( get_ip().get_port() - 8080) + "/"; }

    @Override
    public String toString() {
        return "Node [Address="
                + this.node_ip.get_host() +
                ", port= " + this.node_ip.get_port() +
                "]";
    }
}
