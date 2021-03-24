package TicTacToe.model;

import TicTacToe.model.Enum.GameState;

public interface gameEndListener {
    void gameEnd(GameState gameState);
}
