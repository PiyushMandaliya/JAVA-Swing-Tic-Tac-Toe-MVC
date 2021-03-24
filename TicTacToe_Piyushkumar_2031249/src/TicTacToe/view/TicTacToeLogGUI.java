package TicTacToe.view;

import javax.swing.*;
import java.awt.*;

public class TicTacToeLogGUI extends JFrame{

    private TextArea textArea;

    public TicTacToeLogGUI(String message){
        setTitle("TicTacToe Log");
        setSize(400, 400);
        setLocationRelativeTo(null);    //Centered

        setLayout(new FlowLayout());
        textArea = new TextArea(30,40);
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        add(scroll);

        textArea.setText(message);


        scroll.setSize( 100, 100 );
        textArea.setBackground(Color.LIGHT_GRAY);

        setVisible(true);
    }
}
