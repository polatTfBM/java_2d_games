package game.util;

import java.awt.Color;

/**
 * Holds global constants for the game such as window size and game speeds.
 */
public final class Constants {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int PLAYER_START_X = 380;
    public static final int PLAYER_START_Y = 500;
    public static final Color BACKGROUND_COLOR = Color.BLACK;
    public static final String DATA_DIRECTORY = "data" + System.getProperty("file.separator");
    public static final String SCORE_FILE = DATA_DIRECTORY + "highscore.txt";
    public static final String INFO_FILE = DATA_DIRECTORY + "info.txt";

    private static int gamesPlayed = 0;
    private static boolean debugMode = false;

    private Constants() {
    }

    /**
     * Tracks how many games were started.
     * @return incremented counter
     */
    public static int incrementGamesPlayed() {
        gamesPlayed++;
        return gamesPlayed;
    }

    public static int getGamesPlayed() {
        return gamesPlayed;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static void toggleDebugMode() {
        debugMode = !debugMode;
    }
}
