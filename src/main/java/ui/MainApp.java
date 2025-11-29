package ui;

import javax.swing.SwingUtilities;

/**
 * Entry point for the application.
 */
public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
