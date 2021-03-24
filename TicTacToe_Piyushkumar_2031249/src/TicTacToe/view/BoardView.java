package TicTacToe.view;

import TicTacToe.model.Enum.PlayerPiece;
import TicTacToe.model.TicTacToe;
import TicTacToe.model.gameEndListener;
import TicTacToe.model.playerTurnListener;

public interface BoardView {
    void playAgain();
    int checkWin();
    boolean markPosition(int position, PlayerPiece piece);
    boolean isValidMove(int position);
    void addGameEndListener(gameEndListener observer);
    void resetBoard();
}
