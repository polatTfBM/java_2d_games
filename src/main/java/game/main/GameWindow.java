package game.main;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import game.exception.InvalidMovementException;
import game.logic.GamePanel;
import game.util.Constants;
import game.util.SpriteLoader;

/**
 * Simple JFrame wrapper hosting the GamePanel.
 */
public class GameWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private GamePanel panel;
    private boolean centered;
    private long openedAt;
    private String titleSuffix;

    public GameWindow() throws InvalidMovementException {
        this("2D Shooter");
    }

    public GameWindow(String title) throws InvalidMovementException {
        super(title);
        this.centered = false;
        this.openedAt = System.currentTimeMillis();
        this.titleSuffix = "";
        configure();
    }

    private void configure() throws InvalidMovementException {
        setLayout(new BorderLayout());
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setFocusable(true);
        try {
            setIconImage(SpriteLoader.createIcon());
        } catch (Exception e) {
            setTitle(getTitle() + " (icon missing)");
        }
        this.panel = new GamePanel();
        add(panel, BorderLayout.CENTER);
        ensureFocus();
    }

    private void ensureFocus() {
        if (!centered) {
            setLocationByPlatform(true);
            centered = true;
        }
        setVisible(true);
        requestFocus();
    }

    public GamePanel getPanel() {
        return panel;
    }

    public long getOpenedAt() {
        return openedAt;
    }

    public String getTitleSuffix() {
        return titleSuffix;
    }

    public void setTitleSuffix(String titleSuffix) {
        this.titleSuffix = titleSuffix;
    }
}
