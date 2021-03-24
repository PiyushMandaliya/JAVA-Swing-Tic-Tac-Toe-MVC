package TicTacToe.controller;

import TicTacToe.model.Enum.GameType;
import TicTacToe.view.GameEndGUI;
import TicTacToe.view.TicTacToeGUI;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        int choice = 0;
        displayMenu();
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n\nEnter your choice: ");
        choice = scanner.nextInt();
        int baordSize;
        switch (choice) {
            case 1:
                System.out.println("Enter boardSize(3 or 4): " );
                baordSize = scanner.nextInt();
                new TicTacToeGUI(baordSize, GameType.PLAY_WITH_FRIEND);
                break;
            case 2:
                System.out.println("Enter boardSize(3 or 4): " );
                baordSize = scanner.nextInt();
                new TicTacToeGUI(baordSize, GameType.PLAY_WITH_AI);
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }

    //Menu that display when the application launch
    private static void displayMenu() {
        System.out.println("\n\n------------------------");
        System.out.println("       Tic Tac Toe      ");
        System.out.println("------------------------");
        System.out.println("\n1 – Play a local game against a friend" +
                "\n2 – Play a local game against an AI player" +
                "\n3 - Exit");
    }
}