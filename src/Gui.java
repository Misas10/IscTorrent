import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.List;

/*
ALL the GUI from the app is handle in this class
 */

public class Gui {
    private static JList< Rofly > result_list = new JList<>();

    private static final JFrame frame = new JFrame();

    public Gui() {

        // Creating instance of JFrame
        JFrame frame = new JFrame();
        frame.setTitle(Node.get_instance().toString());

        JLabel label = new JLabel("Texto a procurar:");

        JTextField text_field = new JTextField();

        // JList<String> result_list = new JList<String>();
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


        // result_list.addListSelectionListener();

        // making the frame visible
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Window to connect node to another
        JFrame connect_node = set_connect_node_frame();

        connect_node_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect_node.setVisible(true);
            }
        });

        search_button.addActionListener( new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
                Node.get_instance().clear_file_search_list();

              Node.get_instance().search(text_field.getText());

          }
        });

        download_button.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(result_list.getSelectedValuesList().size() == 1)

                for ( Rofly selected_download : result_list.getSelectedValuesList() )

                    Node.get_instance().new_download_request( selected_download );

            }
          });
    }

    JFrame set_connect_node_frame () {

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
                    Node.get_instance().connect(host.getText(), Integer.parseInt(port.getText()));
            }
        });

        return frame;
    }

    public static void update_list( final List< Rofly > new_list ) {

        DefaultListModel< Rofly > dlm = new DefaultListModel<>();

        for (int i = 0; i < new_list.size(); i++) {
            System.out.println(new_list.get(i).get_file_name());
            dlm.add( i, new_list.get( i ) );

        }

        result_list.setModel(dlm);
        
        // System.out.println(new_list.length);
    }

    public static void showInfo(String msg) {
            JOptionPane.showMessageDialog(frame, msg);
    }
}
