package game.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import game.exception.SpriteNotFoundException;

/**
 * Utility class for creating simple sprites at runtime.
 */
public final class SpriteLoader {
    private static final int ICON_SIZE = 32;
    private static Color accent = Color.CYAN;
    private static boolean neonEdge = true;
    private static long creationCounter = 0L;

    private SpriteLoader() {
    }

    /**
     * Creates a buffered image representing the player ship.
     * @return image
     * @throws SpriteNotFoundException when image cannot be created
     */
    public static BufferedImage createPlayerSprite() throws SpriteNotFoundException {
        try {
            BufferedImage image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval(5, 10, 30, 20);
            g2d.setColor(accent);
            g2d.fillRect(18, 0, 4, 20);
            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(new int[] { 5, 20, 35 }, new int[] { 30, 5, 30 }, 3);
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(neonEdge ? Color.CYAN : Color.LIGHT_GRAY);
            g2d.drawOval(5, 10, 30, 20);
            g2d.dispose();
            creationCounter++;
            return image;
        } catch (Exception e) {
            throw new SpriteNotFoundException("Unable to create player sprite", e);
        }
    }

    /**
     * Creates a small icon to be used for the window.
     * @return icon image
     * @throws SpriteNotFoundException if creation fails
     */
    public static Image createIcon() throws SpriteNotFoundException {
        try {
            BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.BLUE);
            g2d.fillRect(0, 0, ICON_SIZE, ICON_SIZE);
            g2d.setColor(Color.WHITE);
            g2d.drawString("GS", 8, 18);
            g2d.dispose();
            creationCounter++;
            return image;
        } catch (Exception e) {
            throw new SpriteNotFoundException("Unable to create icon", e);
        }
    }

    public static <T> String describeSprite(T sprite) {
        return sprite == null ? "none" : sprite.getClass().getSimpleName();
    }

    public static <T extends Number> double safeDouble(T number) {
        double value = number.doubleValue();
        return value < 0 ? 0 : value;
    }

    public static long getCreationCounter() {
        return creationCounter;
    }

    public static boolean isNeonEdge() {
        return neonEdge;
    }

    public static void setNeonEdge(boolean neonEdge) {
        SpriteLoader.neonEdge = neonEdge;
    }

    public static Color getAccent() {
        return accent;
    }

    public static void setAccent(Color accent) {
        SpriteLoader.accent = accent;
    }
}
