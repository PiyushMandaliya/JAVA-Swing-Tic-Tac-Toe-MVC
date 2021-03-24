package TicTacToe.model;

import TicTacToe.model.Enum.PlayerPiece;

public abstract class Player {

    private int winCounter = 0;
    private String name ;
    private PlayerPiece playerPiece;

    public Player(String name, PlayerPiece playerPiece){
        this.playerPiece = playerPiece;
        this.name = name;
    }

    public String getName(){return name;}

    public PlayerPiece getPiece(){
        return playerPiece;
    }

    public void increaseWinCounter(){
        winCounter++;
        System.out.println(name + " won total " + winCounter + " match");
    }
    public int getWinCounter(){
        return winCounter;
    }
}
