package TicTacToe.model.Enum;

import TicTacToe.model.Player;

public enum PlayerPiece {
    X,O;

    public static char getPiece(PlayerPiece piece){
        switch (piece){
            case X:
                return 'X';
            case O:
                return 'O';
            default:
                return ' ';
        }
    }
}

