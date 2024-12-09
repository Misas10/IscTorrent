import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Download_Task extends Thread {

    private class Data_Block {

        private enum Data_Block_Status { NOT_RESOLVED, RESOLVED, WAITING, PROCESSING };

        private Data_Block_Status status;
        private Connection connection;
        private final int offset;
        private final int length;
        private long time_stamp;

        public Data_Block( final int offset, final int length ) 
            { this.status = Data_Block_Status.NOT_RESOLVED; this.offset = offset; this.length = length; }

        public synchronized final Data_Block_Status get_status() { return status; }

        public synchronized void set_status( final Data_Block_Status status ) { this.status = status; }

        public synchronized final long get_time_stamp() { return time_stamp; }

        public synchronized void set_time_stamp( final long time_stamp ) { this.time_stamp = time_stamp; }

        public synchronized final Connection get_connection() { return connection; }

        public synchronized void set_connection( final Connection connection ) { this.connection = connection; }

        public final int get_offset() { return offset; }

        public final int get_length() { return length; }

        public final FileBlockRequestMessage get_file_block_request_message() 
            { return new FileBlockRequestMessage( get_file_hash(), length, offset ); }
        
    };

    private final String file_name;
    private final List< Data_Block > blocks = new ArrayList<>();
    private final byte[] file_hash;
    private final byte[] file_data;
    private final long start_time;
    private double final_time;
    private List<Connection> connections;

    public Download_Task( final String file_name, final byte[] file_hash, final int file_size, final List< Connection > connections ) { 

        start_time = System.currentTimeMillis();
        
        this.file_name = file_name; file_data = new byte[ ( int ) file_size ]; this.file_hash = file_hash; set_data_blocks();

        this.connections = connections;
        
        for( Connection connection : connections ) send_new_block_request( connection ); 

    }

    private void set_data_blocks() {

        final int file_size = file_data.length;
        int current_offset = 0;

        while( current_offset != file_size ) {
            
            final int new_offset = current_offset + Constants.BLOCK_SIZE > file_size ? file_size : current_offset + Constants.BLOCK_SIZE;

            blocks.add(
                
                new Data_Block( current_offset, new_offset - current_offset )

            );

            current_offset = new_offset;

        }

    }

    public byte[] get_file_hash() { return file_hash; }

    public String get_file_name() { return file_name; }

    private synchronized final byte[] get_file_data() { return file_data; }

    private synchronized void set_file_data( final int offset, final int length, final byte[] data ) 
        { System.arraycopy( data, 0, file_data, offset, length ); }

    private boolean is_completed() {

        boolean is_completed = true;

        synchronized( blocks ) {

            for ( Data_Block data_block : blocks ) {

                if( data_block.get_status() == Data_Block.Data_Block_Status.RESOLVED ) continue;
                
                is_completed = false;

                break;

            }

        }

        return is_completed;

    }

    // Checks periodicly? if any of the request
    // have timeout
    @Override
    public void run() {

        final int timeout_check_milli = 5 * 1000;

        while( ! is_completed() ) {

            try { Thread.sleep( timeout_check_milli ); } 
            catch( Exception e ) { System.out.println("Download task timeout check error, leaving"); return; }

            synchronized( blocks ) {

                for ( Data_Block data_block : blocks ) {

                    if ( 

                        data_block.get_status() != Data_Block.Data_Block_Status.WAITING ||
                        System.currentTimeMillis() - data_block.get_time_stamp() < timeout_check_milli

                    ) continue;
    
                    // Timeout
                    data_block.set_status( Data_Block.Data_Block_Status.NOT_RESOLVED ); break;

                }
    
            }

        }

        Node.get_instance().get_files_manager().add_new_file( file_name, get_file_data() );

    }

    private void send_new_block_request( final Connection connection ) {

        FileBlockRequestMessage file_block_request_message = null;

        synchronized( blocks ) {

            for ( Data_Block data_block : blocks ) {

                if( data_block.get_status() != Data_Block.Data_Block_Status.NOT_RESOLVED ) continue;

                data_block.set_status( Data_Block.Data_Block_Status.WAITING );
                data_block.set_time_stamp( System.currentTimeMillis() );
                data_block.set_connection( connection );

                file_block_request_message = data_block.get_file_block_request_message();

                break;

            }

        }

        if( file_block_request_message == null ) return; // TODO:

        connection.send_data( file_block_request_message );

    }

    public void receive_new_block_request( final Connection connection, final FileBlockAnswerMessage file_block_answer_message ) {

        Data_Block data_block_target = null;

        synchronized( blocks ) {

            for ( Data_Block data_block : blocks ) {

                // Get the specific block for the connection answer
                if( 
                    ! data_block.get_connection().equals( connection ) || 
                    data_block.get_status() != Data_Block.Data_Block_Status.WAITING
                ) continue;

                // Check if the length is correct
                if(
                    data_block.get_length() != file_block_answer_message.get_data().length
                ) { data_block.set_status( Data_Block.Data_Block_Status.NOT_RESOLVED ); continue; } // TODO: remove faulty connection

                // Set the status as in processing 
                // to sinalize that answer was received 
                // but logic is still being executed
                data_block.set_status( Data_Block.Data_Block_Status.PROCESSING );

                data_block_target = data_block;

                break;

            }

        }

        if( data_block_target == null ) return; // TODO: remove faulty connection

        set_file_data( data_block_target.get_offset(), data_block_target.get_length(), file_block_answer_message.get_data() );        

        data_block_target.set_status( Data_Block.Data_Block_Status.RESOLVED ); 

        if( is_completed() ) {
            final_time = (( double ) System.currentTimeMillis() - start_time ) / 1000;

            Gui.showInfo("Download Succecfully! \n\n" + finishedMessage());
        }

        send_new_block_request( connection );
        
    }

    private String finishedMessage() {
        String str = "";
        HashMap<Connection, Integer> map = new HashMap<>();

        // Count the numbers of blocks/threads
        for(Data_Block data_block : blocks) {
            int count = map.get(data_block.connection) == null ? 0 : map.get(data_block.connection) + 1;

            map.put(data_block.connection, count);
        }

        // Add the number of thread of every connection
        for(Connection connection : map.keySet())
            str += connection.get_ip() + ":" + map.get(connection) + "\n";


        // Add the total time
        str += "Total time: " + final_time + "s";

        return str;
    }
}
