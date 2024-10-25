import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WordSearchMessage {
  public final String keyWord;
  public final Node node;


  public WordSearchMessage(String kw, Node node) {
    System.out.println(kw);
    this.keyWord = kw;
    this.node = node;

    for(Socket socket : node.getConnected_sockets()){

    int folderNumber = node.getPort() - 8080; 
    List<String> fileNames = listFileNames(new File(Constants.FOLDER_PATH + folderNumber).listFiles());
    List<String> filteredList = new ArrayList<>();

    for(String name : fileNames) {
      if(name.contains(kw)){
        System.out.println(name);
        filteredList.add(name);
      }

    }
    }

  }

  private List<String> listFileNames(final File[] files){
    List<String> f = new ArrayList<>();

    for(File file : files)
      f.add(file.getName());

    return f;
    
  }
}
