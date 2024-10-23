public class IscTorrent {
    public IscTorrent() {
    }

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Node node = new Node(host, port);
        node.start();

        System.out.println("\nNew node: " + node);

        Gui gui = new Gui(node);
        gui.start();
    }
}
