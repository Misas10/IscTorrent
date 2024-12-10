import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DownloadTasksManager extends Thread{

    private final List< Download_Task > tasks;

    public DownloadTasksManager() { tasks = new ArrayList<>(); }

    @Override
    public void run() {
        while (true) {

            try {
                Thread.sleep(1000);

                synchronized ( tasks ) {
                    if(tasks.removeIf(s -> s.is_completed()))
                        System.out.println("Task removed");
                }

            } catch (InterruptedException e) {
                System.out.println("ERRO NO SLEEP");
                e.printStackTrace();

                // throw new RuntimeException(e);
            }
        }
    }

    public void add_new_download_task(final String file_name, final byte[] file_hash, final int file_size, final List< Connection > connections ) {

        synchronized( tasks ) {

            for ( Download_Task task : tasks ) {

                if( Arrays.equals( task.get_file_hash(), file_hash ) ) return;

            }

            Download_Task task = new Download_Task( file_name, file_hash, file_size, connections );
            tasks.add( task );
            task.start();
            // System.exit(0);
        }

    }

    public void add_new_download_tasks() {}

    public void new_file_block_answer_received( final Connection connection, final FileBlockAnswerMessage file_block_answer_message ) {

        synchronized( tasks ) {

            for( Download_Task task : tasks ) {

                if ( ! Arrays.equals( task.get_file_hash(), file_block_answer_message.get_hash() ) ) continue;

                task.receive_new_block_request( connection, file_block_answer_message );

            }

        }

    }

}
