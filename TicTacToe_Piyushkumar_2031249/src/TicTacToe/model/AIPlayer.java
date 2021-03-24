package TicTacToe.model;

import TicTacToe.model.Enum.PlayerPiece;
import TicTacToe.view.BoardView;

import java.util.ArrayList;
import java.util.Arrays;

public class AIPlayer extends Player {

    private ArrayList<Integer> priorityPosition = new ArrayList(Arrays.asList(4, 0, 2, 6, 8, 1, 3, 5, 7));
    private ArrayList<Integer> priorityPositionForBigSizeBoard = new ArrayList(Arrays.asList(0, 5, 10, 15, 3, 6, 9, 12, 1, 2, 4, 7, 8, 11, 13, 14));

    public AIPlayer(PlayerPiece playerPiece) {
        super("AI", playerPiece);
    }

    public int getNextMovePosition(BoardView board, int size) {
        ArrayList<Integer> boardArray;
        if (size == 3)
            boardArray = priorityPosition;
        else
            boardArray = priorityPositionForBigSizeBoard;

        for (int i = 0; i < boardArray.size(); i++)
            if (board.isValidMove(boardArray.get(i)))
                return boardArray.get(i);
        return 0;
    }
}
