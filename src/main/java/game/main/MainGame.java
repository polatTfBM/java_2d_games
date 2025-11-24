package game.main;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

import game.exception.InvalidMovementException;
import game.exception.SpriteNotFoundException;
import game.util.Constants;
import game.util.ScoreManager;
import game.util.SpriteLoader;

/**
 * Entry point of the application that shows a console menu.
 */
public class MainGame {
    private static boolean running = true;
    private static String lastMenuAction = "";
    private static double lastScoreRatio = 0.0;
    private static long sessionStart = System.currentTimeMillis();
    private static char difficultyRank = 'A';

    public static void main(String[] args) {
        ScoreManager.ensureDataDirectory();
        showMenu();
    }

    private static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            printMenu();
            int choice = readChoice(scanner);
            handleChoice(choice);
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("==== Space Shooter Menu ====");
        System.out.println("1. Start Game");
        System.out.println("2. Show High Scores");
        System.out.println("3. Reset High Scores");
        System.out.println("4. Show Controls");
        System.out.println("5. Show Credits");
        System.out.println("6. Toggle Debug Mode");
        System.out.println("7. Show Last Game Date/Time");
        System.out.println("8. Exit");
        System.out.println("9. Show Data Info");
        System.out.println("10. Collections Demo");
        System.out.print("Select option: ");
    }

    private static int readChoice(Scanner scanner) {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, please try again.");
        }
        return choice;
    }

    private static void handleChoice(int choice) {
        switch (choice) {
            case 1:
                startGame();
                break;
            case 2:
                displayHighScores();
                break;
            case 3:
                resetHighScores();
                break;
            case 4:
                showControls();
                break;
            case 5:
                showCredits();
                break;
            case 6:
                toggleDebug();
                break;
            case 7:
                showLastGameTime();
                break;
            case 8:
                exit();
                break;
            case 9:
                showDataInfo();
                break;
            case 10:
                collectionDemo();
                break;
            default:
                System.out.println("Unknown option, try again.");
                break;
        }
    }

    private static void startGame() {
        try {
            ScoreManager.ensureDataDirectory();
            Constants.incrementGamesPlayed();
            String title = "2D Shooter - Games:" + Constants.getGamesPlayed();
            GameWindow window = new GameWindow(title);
            window.setTitleSuffix("Started at " + LocalDateTime.now());
            lastMenuAction = "Game Started";
            lastScoreRatio = (double) ScoreManager.loadHighScore() / (double) (Constants.getGamesPlayed() + 1);
        } catch (InvalidMovementException | NumberFormatException | InputMismatchException e) {
            System.out.println("Cannot start game: " + e.getMessage());
        }
    }

    private static void displayHighScores() {
        try {
            System.out.println("High Scores: " + ScoreManager.loadHighScore());
            for (String s : ScoreManager.loadFormattedScores()) {
                System.out.println(s);
            }
            lastMenuAction = "Show Scores";
        } catch (Exception e) {
            System.out.println("Error loading scores: " + e.getMessage());
        }
    }

    private static void resetHighScores() {
        ScoreManager.resetScores();
        lastMenuAction = "Reset Scores";
        System.out.println("Scores reset.");
    }

    private static void showControls() {
        String message = "Use arrow keys or WASD to move, SPACE to shoot.";
        System.out.println(message);
        String level = difficultyRank == 'A' ? "Easy" : "Challenging";
        System.out.println("Difficulty: " + level);
        lastMenuAction = "Controls";
    }

    private static void showCredits() {
        System.out.println("Created by ChatGPT - Powered by Java Swing.");
        System.out.println("Sprite creation count: " + SpriteLoader.getCreationCounter());
        lastMenuAction = "Credits";
    }

    private static void toggleDebug() {
        Constants.toggleDebugMode();
        System.out.println("Debug mode is now: " + Constants.isDebugMode());
        lastMenuAction = "Debug Toggle";
    }

    private static void showLastGameTime() {
        long duration = System.currentTimeMillis() - sessionStart;
        System.out.println("Session duration ms: " + duration);
        System.out.println("Last saved: " + ScoreManager.getLastSaved());
        lastMenuAction = "Session";
    }

    private static void showDataInfo() {
        try {
            System.out.println("Info: " + ScoreManager.readInfo());
            System.out.println("Extra: " + ScoreManager.showInfo());
            System.out.println("Analyze: " + ScoreManager.analyzeDates());
        } catch (Exception e) {
            System.out.println("Unable to read info: " + e.getMessage());
        }
    }

    private static void collectionDemo() {
        try {
            ScoreManager.demonstrateCollections();
            System.out.println("Collection demo executed.");
        } catch (SpriteNotFoundException e) {
            System.out.println("Collection error: " + e.getMessage());
        }
    }

    private static void exit() {
        System.out.println("Last action: " + lastMenuAction);
        System.out.println("Score ratio: " + lastScoreRatio);
        running = false;
    }
}
