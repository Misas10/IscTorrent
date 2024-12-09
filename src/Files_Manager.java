import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Files_Manager extends Thread {

    private final List< File_Data > files = new ArrayList<>(); // Files this node have access to
    private final String dir_path;

    public Files_Manager( final String dir_path ) { this.dir_path = dir_path; set_files( dir_path ); }

    public final String get_dir_path() { return dir_path; }

    @Override
    public void run() {

        final int loop_milli = 5 * 1000;

        while( true ) {

            try { Thread.sleep( loop_milli ); } 
            catch( Exception e ) { System.out.println("File manager check error, leaving"); return; }

            synchronized( files ) { files.clear(); set_files( dir_path ); }            

        }

    }

    // It gets all the files from the default folder 
    private void set_files( final String dir_path ) {

        File folder = new File( dir_path );

        if(!folder.exists()){
            System.out.println("Folder does not exists!");

            if(folder.mkdir())
                System.out.println("Folder created");

            else
                System.out.println("Error trying to create the folder!");

            return;
        }

        File[] filesList = folder.listFiles();

        if( files == null ) return;

        for( File file : filesList ) { 
            
            if ( file.isFile() ) {

                File_Data file_data = new File_Data( file.toPath() );

                if ( ! file_data.is_valid() || files.contains( file_data ) ) continue;

                synchronized( files ) { files.add( file_data ); }

            } else if ( file.isDirectory() ) set_files( file.getAbsolutePath() );
        
        }

    }

    public List< File_Data > find_files_by_keyword_name( String keyword ) {

        List< File_Data > list_files = new ArrayList<>();

        synchronized( files ) {
                
            for( File_Data file : files ) {

                if( ! file.get_file_name().contains( keyword ) ) continue;

                list_files.add( file );

            }

        }

        return list_files;

    }
    
    public byte[] get_file_block( final byte[] file_hash, final int offset, final int length ) {

        File_Data file_target = null;

        synchronized( files ) {

            for( File_Data file : files ) {

                if( ! Arrays.equals( file.get_hash(), file_hash ) ) continue;

                file_target = file;

            }

        }

        if ( file_target == null ) return null;

        return Arrays.copyOfRange( file_target.get_data(), offset, offset + length );

    }

    public void add_new_file( final String file_name, final byte[] file_data ) {

        try ( FileOutputStream fos = new FileOutputStream( get_dir_path() + file_name ) ) { fos.write( file_data ); } 
        catch ( IOException e ) { System.err.println("Error writing to file: " + e.getMessage()); } 

    }

}
 