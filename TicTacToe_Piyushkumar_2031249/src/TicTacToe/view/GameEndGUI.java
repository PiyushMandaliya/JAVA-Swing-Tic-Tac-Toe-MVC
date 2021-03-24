package TicTacToe.view;

import javax.swing.*;
import java.awt.*;

public class GameEndGUI extends JFrame {

    private TicTacToeGUI ticTacToeGUI;

    public GameEndGUI(TicTacToeGUI ticTacToeGUI,String message){
        setTitle("Tic Tac Toe ");

        this.ticTacToeGUI = ticTacToeGUI;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);    //Centered
        setSize(300, 150);
        //Box Layout (axis: Direction to go)
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        createComponents(message);
        setVisible(true); //Must at end to refresh.

    }

    private void createComponents(String message) {
        JPanel titleMessage = new JPanel();
        titleMessage.setLayout(new BorderLayout());

        titleMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleMessage.setPreferredSize(new Dimension(250, 50));
        titleMessage.setMaximumSize(new Dimension(250, 50));

        titleMessage.add(new JLabel("<html>"+message+"<br/>Do you want to play again?</html>",SwingConstants.CENTER));



        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setBounds(12, 23, 150, 315);

        JButton yesButton = getButton("Yes");
        JButton noButton = getButton("No");


        yesButton.addActionListener(e -> {
            ticTacToeGUI.replayGame();
            dispose();
        });

        noButton.addActionListener(e -> {
            dispose();
        });

        buttonsPanel.add(noButton);
        buttonsPanel.add(yesButton);

        add(titleMessage);
        add(buttonsPanel);
    }

    private JButton getButton(String btnTitle) {
        JButton jButton = new JButton();
        jButton.setText(btnTitle);
        jButton.setPreferredSize(new Dimension(70, 23));
        return jButton;
    }

}
