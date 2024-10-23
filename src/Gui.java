import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import static javax.swing.GroupLayout.Alignment.*;

public class Gui {
    private final Node node;

    public Gui(Node node) {
        this.node = node;
    }

    public void start() {
        // Creating instance of JFrame
        JFrame frame = new JFrame();
        frame.setTitle(node.toString());


        JLabel label = new JLabel("Texto a procurar:");

        JTextField text_field = new JTextField();

        JList result_list = new JList(new String[]{"Hi", "Hey", "Hello"});
        result_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        // Creating the buttons
        JButton connect_node_button = new JButton("Ligar a Nó");
        JButton download_button = new JButton("Descarregar");
        JButton search_button = new JButton("Procurar");

        Container c = frame.getContentPane();

        Container top_container = new Container();
        top_container.setLayout(new BorderLayout());
        top_container.add(label, BorderLayout.LINE_START);
        top_container.add(text_field, BorderLayout.CENTER);
        top_container.add(search_button, BorderLayout.LINE_END);

        Container buttons_container = new Container();
        buttons_container.setLayout(new GridLayout(2, 1));
        buttons_container.add(download_button);
        buttons_container.add(connect_node_button);

        c.add(top_container, BorderLayout.NORTH);
        c.add(result_list, BorderLayout.CENTER);
        c.add(buttons_container, BorderLayout.EAST);

        // making the frame visible
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Window to connect node to another
        JFrame connect_node = set_connect_node_frame();

        connect_node_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect_node.setVisible(true);

                /*node.start();
                node.connect(8081);*/
            }
        });
    }

    JFrame set_connect_node_frame () {

        JFrame frame = new JFrame();

        frame.setLayout(new GridLayout(1, 6));
        frame.setSize(200, 100);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel host_label = new JLabel("Endereço: ");
        JTextField host = new JTextField();

        JLabel port_label = new JLabel("Porta: ");
        JTextField port = new JTextField();

        JButton cancel_button = new JButton("Cancelar");
        JButton ok_button = new JButton("OK");

        frame.add(host_label);
        frame.add(host);
        frame.add(port_label);
        frame.add(port);
        frame.add(cancel_button);
        frame.add(ok_button);

        cancel_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose();
            }
        });

        ok_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!host.getText().isEmpty() && !port.getText().isEmpty())
                    node.connect(host.getText(), Integer.parseInt(port.getText()));
            }
        });

        return frame;
    }
}
