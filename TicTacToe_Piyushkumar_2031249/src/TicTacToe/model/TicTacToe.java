package TicTacToe.model;

import TicTacToe.model.Enum.GameType;
import TicTacToe.model.Enum.PlayerPiece;
import TicTacToe.view.BoardView;
import TicTacToe.view.TicTacToeGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class TicTacToe {

    private static final File logFile = new File("TicTacToe.txt");

    private Player player1, player2;
    private GameType gameType;
    private Player currentPlayer;
    private BoardView board;
    private TicTacToeGUI ticTacToeGUI;
    private playerTurnListener playerTurnListeners;
    private int totalGamePlayed;
    Thread markThread;
    private int baordSize;
    ArrayList<Integer> arrayListPlayerPosition = new ArrayList<>();
    ArrayList<PlayerPiece> arrayListPlayerPiece = new ArrayList<>();
    int currentBoardChanges = 0;

    public TicTacToe(TicTacToeGUI ticTacToeGUI, int boardSize, GameType gameType) {
        this.baordSize = boardSize;
        board = new Board(boardSize);
        this.ticTacToeGUI = ticTacToeGUI;
        this.gameType = gameType;
        setPlayers();
        totalGamePlayed++;
        writeGameStartMessage(false);
    }


    public void addListener(playerTurnListener playerTurnListener) {
        playerTurnListeners = playerTurnListener;
        board.addGameEndListener(ticTacToeGUI);
        setRandomPlayerTurn();
    }

    private void notifyPlayerTurnChange() {
//        System.out.println("notifyPlayerTurnChange: " + currentPlayer.getName());
//        for (playerTurnListener playerTurnListener : playerTurnListeners)
        playerTurnListeners.notifyPlayerTurnChanged(currentPlayer);
    }

    private void setRandomPlayerTurn() {
        int randomNumber = (new Random().nextInt(5)) % 2;
        if (randomNumber == 0)
            currentPlayer = player2;
        else
            currentPlayer = player1;
        notifyPlayerTurnChange();
    }

    private void setPlayers() {
        switch (gameType) {
            case PLAY_WITH_FRIEND:
                player1 = new HumanPlayer("Player 1", PlayerPiece.X);
                player2 = new HumanPlayer("Player 2", PlayerPiece.O);
                break;
            case PLAY_WITH_AI:
                player1 = new HumanPlayer("Player 1", PlayerPiece.X);
                player2 = new AIPlayer(PlayerPiece.O);
                break;
        }
    }

    public boolean isValidMove(int position) {
        return board.isValidMove(position);
    }

    public void setPlayerWinCounter(Player player) {
        if (player.getName() == player1.getName()) {
            player1.increaseWinCounter();
            write("Player 1 won!", true);
        } else {
            player2.increaseWinCounter();
            write("Player 1 won!", true);
        }
    }

    public void markPosition(int position, PlayerPiece piece) {
        arrayListPlayerPiece.add(piece);
        arrayListPlayerPosition.add(position);
        if (isValidMove(position)) {
            boolean isSuccess = board.markPosition(position, piece);
            if (isSuccess) {
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
                if (markThread == null || !markThread.isAlive()) {
                    markThread = new Thread(() -> {
                        while (currentBoardChanges < arrayListPlayerPosition.size()) {

                            currentBoardChanges++;
                            try {
                                System.out.println("markPosition()");
//                System.out.println("Before Mark Done: " + currentPlayer.getName());
                                notifyPlayerTurnChange(); // Not Call
//                System.out.println("After Mark Done: " + currentPlayer.getName());
                                Thread.sleep(0);

                            } catch (InterruptedException e) {
                                System.out.println(e.getMessage());
                            }

                            write("Player " + PlayerPiece.getPiece(piece) + " mark at position " + position, true);
                        }
                    });
                    markThread.start();
                }
            } else {
                currentBoardChanges = 999;
            }
        }
    }


    public int getTieGameNumber() {
        return totalGamePlayed - (player2.getWinCounter() + player1.getWinCounter());
    }

    public void resetBoardAndPlayer() {
        board.resetBoard();
        totalGamePlayed++;
        currentBoardChanges = 0;
        arrayListPlayerPosition.clear();
        arrayListPlayerPiece.clear();

        writeGameStartMessage(true);
        setRandomPlayerTurn();
    }

    public int getAIBestPosition() {
        return ((AIPlayer) player2).getNextMovePosition(board,baordSize);
    }


//    Writing Log Stuffs

    private void writeGameStartMessage(boolean append) {
        write("---------------------------------------\n" +
                "             Game " + totalGamePlayed + " Start              \n" +
                "---------------------------------------", append);
    }

    private void writeGameEndMessage() {
        write("---------------------------------------\n" +
                "             Game " + totalGamePlayed + " Ended              \n" +
                "---------------------------------------\n", true);
    }

    public void writeGameScore() {
        write("------ Total Score -------", true);

        write("Total Game Played: " + totalGamePlayed, true);
        write(player1.getName() +" won: " + player1.getWinCounter(), true);
        write(player2.getName() +" won: " + player2.getWinCounter(), true);
        write("draw match: " + getTieGameNumber(), true);
        writeGameEndMessage();
    }

    private void write(String message, boolean append) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(logFile, append))) {
            out.println(message);
        } catch (FileNotFoundException exception) {
            System.out.println("if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason: " + logFile.getAbsolutePath());
        }
    }
}
