package IscTorrent;

public class IscTorrent {
    public IscTorrent() {
    }

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        System.out.println("\nNew session with at: " + host + ":" + port);

        if (args.length > 2)
            new Exception("ERROR: Arguments (host or port) were not passed");

    }
}
