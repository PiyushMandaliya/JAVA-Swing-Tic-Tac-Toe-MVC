package TicTacToe.view;

import TicTacToe.model.*;
import TicTacToe.model.Enum.GameState;
import TicTacToe.model.Enum.GameType;
import TicTacToe.model.Enum.PlayerPiece;


import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TicTacToeGUI extends JFrame implements gameEndListener, playerTurnListener {

    private JButton[] boardButtons;
    private int boardSize;

    private JLabel firstPlayer, firstPlayerScore, tie, tieScore, secondPlayer, secondPlayerScore, playerTurnLabel;

    private TicTacToe ticTacToe = null;
    private GameType gameType;
    private JPanel mainPanel;
    private Player currentPlayer;
    Thread markThread;


    public TicTacToeGUI(int boardSize, GameType gameType) {
        setTitle("Tic Tac Toe");
        this.boardSize = boardSize;
        this.gameType = gameType;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 700);

        ticTacToe = new TicTacToe(this, boardSize, gameType);
        setComponent();
        ticTacToe.addListener(this);
        setVisible(true);
    }

    private void setComponent() {

        boardButtons = new JButton[boardSize * boardSize];

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        setTitle();
        mainPanel.add(getGameBoard(), BorderLayout.NORTH);
        //Add Vertical Space | Source StackOver Flow
        //https://stackoverflow.com/questions/8335997/how-can-i-add-a-space-in-between-two-buttons-in-a-boxlayout
        mainPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        setPlayerTurnLabel(mainPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(getStatisticPanel());
        if (ticTacToe == null) {
            System.out.println("Assign ticTacToe");
//            ticTacToe = new TicTacToe(this, boardSize, gameType);
        }
        setLogButton();
        add(mainPanel);
    }

    //Return Game Board
    private JPanel getGameBoard() {
        JPanel gameBoard = new JPanel(new GridLayout(boardSize, boardSize));
        gameBoard.setPreferredSize(new Dimension(340, 340));
        gameBoard.setMaximumSize(new Dimension(340, 340));
        setBoardButtons(gameBoard);
        return gameBoard;
    }

    //Set Main Title
    private void setTitle() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel jLabel = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        jLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        panel.add(jLabel, BorderLayout.NORTH);
        panel.setPreferredSize(new Dimension(340, 50));
        panel.setMaximumSize(new Dimension(340, 50));
        mainPanel.add(panel);
    }

    private void setLogButton() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JButton logButton = new JButton("Show Log");
        panel.add(logButton, BorderLayout.NORTH);
        panel.setPreferredSize(new Dimension(100, 50));
        panel.setMaximumSize(new Dimension(100, 50));

        logButton.addActionListener(e -> {
            File logFile = new File("TicTacToe.txt");

            if (logFile.length() == 0) {
                JOptionPane.showMessageDialog(this, "ERROR: Log File is empty.");
            } else {
                String ticTacToeLog = "";

                try (Scanner scanner = new Scanner(logFile)) {
                    while (scanner.hasNextLine()) {
                        ticTacToeLog += scanner.nextLine() + "\n";
                    }
                    System.out.println(ticTacToeLog);
                    new TicTacToeLogGUI(ticTacToeLog);
                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                }

            }
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(panel);
    }

    private void setBoardButtons(JPanel gameBoard) {
        for (int i = 0; i < boardSize * boardSize; i++) {
            boardButtons[i] = new JButton();
            boardButtons[i].setText("");
            boardButtons[i].setVisible(true);

            gameBoard.add(boardButtons[i]);
            // Uniquely identify which button pressed
            boardButtons[i].setName((i) + "");

            boardButtons[i].addActionListener(new boardButtonClickListener());
            boardButtons[i].addMouseListener(new boardButtonMouseListener());
            boardButtons[i].setFont(new Font("Tahoma", Font.BOLD, 75));
        }
    }

    private void setPlayerTurnLabel(JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        playerTurnLabel = new JLabel("", SwingConstants.CENTER);
        playerTurnLabel.setFont(getStatisticsFont(false));
        panel.add(playerTurnLabel, BorderLayout.NORTH);
        panel.setPreferredSize(new Dimension(340, 20));
        panel.setMaximumSize(new Dimension(340, 20));
        mainPanel.add(panel);
    }


    private JPanel getStatisticPanel() {

        JPanel statisticPanel = new JPanel();
        statisticPanel.setLayout(new GridLayout(2, 3));
        statisticPanel.setPreferredSize(new Dimension(340, 100));
        statisticPanel.setMaximumSize(new Dimension(340, 100));

        Border border = (BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.darkGray)));
        statisticPanel.setBorder(BorderFactory.createTitledBorder(border, "Statistics"));

        firstPlayer = getStatisticsLabel("Player 1(X)");
        tie = getStatisticsLabel("Tie");

        secondPlayer = getStatisticsLabel("Player 2(O)");

        firstPlayerScore = getStatisticsLabel("0");
        tieScore = getStatisticsLabel("0");
        secondPlayerScore = getStatisticsLabel("0");

        statisticPanel.add(firstPlayer);
        statisticPanel.add(tie);
        statisticPanel.add(secondPlayer);
        statisticPanel.add(firstPlayerScore);
        statisticPanel.add(tieScore);
        statisticPanel.add(secondPlayerScore);

        return statisticPanel;
    }

    private JLabel getStatisticsLabel(String title) {
        JLabel jLabel = new JLabel(title, SwingConstants.CENTER);
        jLabel.setFont(getStatisticsFont(true));
        return jLabel;
    }

    private Font getStatisticsFont(boolean isNormal) {
        if (isNormal)
            return new Font(Font.SANS_SERIF, Font.PLAIN, 15);
        else
            return new Font(Font.SANS_SERIF, Font.BOLD, 15);
    }

    private void setCurrentPlayerStatisticHighlight() {
        if (currentPlayer.getPiece() == PlayerPiece.X) { //isFirstPlayerTurn
            firstPlayer.setFont(getStatisticsFont(false));
            firstPlayerScore.setFont(getStatisticsFont(false));

            secondPlayer.setFont(getStatisticsFont(true));
            secondPlayerScore.setFont(getStatisticsFont(true));

        } else {
            firstPlayer.setFont(getStatisticsFont(true));
            firstPlayerScore.setFont(getStatisticsFont(true));

            secondPlayer.setFont(getStatisticsFont(false));
            secondPlayerScore.setFont(getStatisticsFont(false));
        }
    }

    public void replayGame() {
        System.out.println("Game End");
        for (int i = 0; i < boardButtons.length; i++) {
            boardButtons[i].setName(String.valueOf(i));
            boardButtons[i].setIcon(null);
            System.out.println("Image Null: " + i);
        }
        ticTacToe.resetBoardAndPlayer();
    }

    @Override
    public void gameEnd(GameState gameState) {
        if (gameState == GameState.DRAW) {
            System.out.println("Game Draw ");
            tieScore.setText(ticTacToe.getTieGameNumber() + "");
            new GameEndGUI(this, "Game Draw! ");
            playSound("game_draw.wav");
        } else if (gameState == GameState.PLAYER_WON) {
            ticTacToe.setPlayerWinCounter(currentPlayer);
            if (currentPlayer.getPiece() == PlayerPiece.X)
                firstPlayerScore.setText(currentPlayer.getWinCounter() + "");
            else
                secondPlayerScore.setText(currentPlayer.getWinCounter() + "");
            new GameEndGUI(this, currentPlayer.getName() + " won!");
            playSound("game_win.wav");
        }
        ticTacToe.writeGameScore();
    }

    @Override
    public void notifyPlayerTurnChanged(Player player) {
        currentPlayer = player;
        System.out.println("\nnotifyPlayerTurnChanged: " + currentPlayer.getName() + " " + player.getPiece());
        if (gameType == GameType.PLAY_WITH_AI)
            secondPlayer.setText("AI(O)");

        setCurrentPlayerStatisticHighlight();
//        System.out.println("notifyPlayerTurnChanged: " + currentPlayer.playerPiece);
        if (gameType == GameType.PLAY_WITH_FRIEND) {

            if (currentPlayer.getPiece() == PlayerPiece.X)
                playerTurnLabel.setText("Player 1(X) turn.");
            else
                playerTurnLabel.setText("Player 2(0) turn.");
        } else {
            if (currentPlayer.getPiece() == PlayerPiece.X) {
                playerTurnLabel.setVisible(true);
                playerTurnLabel.setText("Your turn (X).");
            } else {
                //todo get best AI Position

                setAIMove();
                playerTurnLabel.setText("AI turn (0).");
                playerTurnLabel.setVisible(false);
            }
        }
    }

    private void setAIMove() {
        int position = ticTacToe.getAIBestPosition();
        System.out.println("setAIMove() AI Mark Position: " + position + " " + currentPlayer.getPiece());
        boardButtons[position].setIcon(getImage(String.valueOf(currentPlayer.getPiece())));
        ticTacToe.markPosition(position, currentPlayer.getPiece());
        repaint();
    }

    //Button Action Listener
    private class boardButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton jButton = (JButton) e.getSource();
            setMark(jButton);
        }
    }

    private void setMark(JButton jButton) {
        int position = Integer.parseInt(jButton.getName());
        if (ticTacToe.isValidMove(position)) {
            playSound("move_sound.wav");
            PlayerPiece piece = currentPlayer.getPiece();

            System.out.println("Player Mark Position: " + position + " " + piece);
            jButton.setIcon(getImage(String.valueOf(piece)));
            ticTacToe.markPosition(position, piece);
            jButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
            jButton.setBackground(null);
            jButton.setFocusPainted(false);
        }
    }

    //Button Mouse Motion Listener
    private class boardButtonMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JButton jButton = (JButton) e.getSource();
            int position = Integer.parseInt(jButton.getName());
//            System.out.println("mouse enter on " + jButton.getName());
            //todo pass Player Piece
            if (ticTacToe.isValidMove(position))
                jButton.setBackground(Color.decode("#80ff80"));
            else
                jButton.setBackground(Color.decode("#ff8080"));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JButton jButton = (JButton) e.getSource();
//            System.out.println("mouseExited " + jButton.getName());
            jButton.setBackground(null);
        }
    }

    private void playSound(String file) {
        try {
            File f = new File(file);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(f));
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ImageIcon getImage(String name) {
        if (name == "X")
            return new ImageIcon("cross.png");
        else
            return new ImageIcon("round.png");
    }

}