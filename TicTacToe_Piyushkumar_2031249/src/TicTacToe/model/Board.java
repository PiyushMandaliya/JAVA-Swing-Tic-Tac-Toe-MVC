package TicTacToe.model;

import TicTacToe.model.Enum.GameState;
import TicTacToe.model.Enum.PlayerPiece;
import TicTacToe.view.BoardView;

import java.util.ArrayList;

public class Board implements BoardView {
    private int boardSize;
    int BOARD_NUMBERS[];
    private GameState gameState;

    private ArrayList<gameEndListener> gameEndObserver = new ArrayList<>();
    private ArrayList<playerTurnListener> playerTurnListener = new ArrayList<>();

    public Board(int boardSize) {
        this.boardSize = boardSize;
        BOARD_NUMBERS = new int[(boardSize * boardSize)];
        resetBoard();
    }

    @Override
    public void addGameEndListener(gameEndListener observer) {
        gameEndObserver.add(observer);
    }

    @Override
    public void resetBoard() {
        gameState = GameState.CONTINUE;
//        for (int i = 0; i < BOARD_NUMBERS.length; i++)
        BOARD_NUMBERS = getDefaultArray();
    }

    public void notifyGameEndObserver() {
        for (gameEndListener observer : gameEndObserver) {
            observer.gameEnd(gameState);
        }
    }

    @Override
    public boolean isValidMove(int position) {
        for (int i = 0; i < position + 1; i++) {
//            System.out.println(position + "==" + i + " && " + BOARD_NUMBERS[i] + " == " + i);
            //Check the the position valid for mark
			/* If the position number and the mark on that position both are same
				than the mark is possible */
            if (position == i && BOARD_NUMBERS[i] == i) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean markPosition(int position, PlayerPiece piece) {
        BOARD_NUMBERS[position] = piece.getPiece(piece);
        //Check Winner And Notify Observer
        int gameCheckNumber = checkWin();
//        System.out.println("\nGame Checker: " + gameCheckNumber);
        if (gameCheckNumber == 1) {
            System.out.println(position + " Marked : " + BOARD_NUMBERS[position]);
            System.out.println("notifyGameEndObserver" + gameEndObserver.size() + " " + gameCheckNumber);
            gameState = GameState.PLAYER_WON;
            notifyGameEndObserver();
            return false;
        } else if (gameCheckNumber == 0) {
            gameState = GameState.DRAW;
            notifyGameEndObserver();
            return false;
        } else
            return true;
    }

    @Override
    public int checkWin() {
        if (boardSize == 3)
            return checkThreeBoardSizeWin();
        else
            return checkFourBoardSizeWin();
    }

    private int checkFourBoardSizeWin() {

        //Horizontal Check
        for (int i = 0; i <= 12; i = i + 4)
            if (BOARD_NUMBERS[i + 0] == BOARD_NUMBERS[i + 1] && BOARD_NUMBERS[i + 1] == BOARD_NUMBERS[i + 2] && BOARD_NUMBERS[i + 2] == BOARD_NUMBERS[i + 3])
                return 1;

        //Vertical Check
        for (int i = 0; i < 4; i++)
            if (BOARD_NUMBERS[(0 * 4) + i] == BOARD_NUMBERS[(1 * 4) + i] && BOARD_NUMBERS[(1 * 4) + i] == BOARD_NUMBERS[(2 * 4) + i] && BOARD_NUMBERS[(2 * 4) + i] == BOARD_NUMBERS[(3 * 4) + i])
                return 1;

        if (BOARD_NUMBERS[0] == BOARD_NUMBERS[5] && BOARD_NUMBERS[5] == BOARD_NUMBERS[10] &&
                BOARD_NUMBERS[10] == BOARD_NUMBERS[15])
            return 1;
        if (BOARD_NUMBERS[3] == BOARD_NUMBERS[6] && BOARD_NUMBERS[6] == BOARD_NUMBERS[9] &&
                BOARD_NUMBERS[9] == BOARD_NUMBERS[12])
            return 1;
        else if (BOARD_NUMBERS[0] != 0 && BOARD_NUMBERS[1] != 1 && BOARD_NUMBERS[2] != 2 && BOARD_NUMBERS[3] != 3 &&
                BOARD_NUMBERS[4] != 4 && BOARD_NUMBERS[5] != 5 && BOARD_NUMBERS[6] != 6 &&
                BOARD_NUMBERS[7] != 7 && BOARD_NUMBERS[8] != 8 && BOARD_NUMBERS[9] != 9 && BOARD_NUMBERS[10] != 10
                && BOARD_NUMBERS[11] != 11 && BOARD_NUMBERS[12] != 12 && BOARD_NUMBERS[13] != 13 && BOARD_NUMBERS[14] != 14
                && BOARD_NUMBERS[15] != 15)
            return 0;
        else
            return -1;
    }

    /*Check the Winner - Horizontally 1,2,3 / 4,5,6 / 7,8,9
					 Vertically 1,4,7 / 2,5,8 / 3,6,9
					 Diagonally 1,5,9 / 3,5,7
				Same mark found than declare winner */

    public int checkThreeBoardSizeWin() {

        //Horizontal Check
        for (int i = 0; i <= 6; i = i + 3)
            if (BOARD_NUMBERS[i + 0] == BOARD_NUMBERS[i + 1] && BOARD_NUMBERS[i + 1] == BOARD_NUMBERS[i + 2])
                return 1;

        //Vertical Check
        for (int i = 0; i < 3; i++)
            if (BOARD_NUMBERS[(0 * 3) + i] == BOARD_NUMBERS[(1 * 3) + i] && BOARD_NUMBERS[(1 * 3) + i] == BOARD_NUMBERS[(2 * 3) + i])
                return 1;


        if (BOARD_NUMBERS[0] == BOARD_NUMBERS[4] && BOARD_NUMBERS[4] == BOARD_NUMBERS[8])
            return 1;
        else if (BOARD_NUMBERS[2] == BOARD_NUMBERS[4] && BOARD_NUMBERS[4] == BOARD_NUMBERS[6])
            return 1;
        else if (BOARD_NUMBERS[0] != 0 && BOARD_NUMBERS[1] != 1 && BOARD_NUMBERS[2] != 2 && BOARD_NUMBERS[3] != 3 &&
                BOARD_NUMBERS[4] != 4 && BOARD_NUMBERS[5] != 5 && BOARD_NUMBERS[6] != 6 &&
                BOARD_NUMBERS[7] != 7 && BOARD_NUMBERS[8] != 8)
            return 0;
        else
            return -1;
    }

    @Override
    public void playAgain() {
//        for (int i = 0; i < BOARD_NUMBERS.length; i++)
        BOARD_NUMBERS = getDefaultArray();
    }

    private int[] getDefaultArray() {
        if (boardSize == 3)
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        else
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    }
}