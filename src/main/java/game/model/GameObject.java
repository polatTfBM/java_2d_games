package game.model;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import game.exception.InvalidMovementException;

/**
 * Base abstract class for all game objects providing position and dimension data.
 */
public abstract class GameObject {
    private double x;
    private double y;
    private int width;
    private int height;
    private double speed;

    public GameObject(double x, double y, int width, int height) {
        this(x, y, width, height, 0.0);
    }

    public GameObject(double x, double y, int width, int height, double speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public abstract void update();

    public abstract void draw(Graphics2D g2d);

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public boolean intersects(GameObject other) {
        return other != null && getBounds().intersects(other.getBounds());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) throws InvalidMovementException {
        if (x < 0 || Double.isNaN(x)) {
            throw new InvalidMovementException("Invalid x position", x, y);
        }
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) throws InvalidMovementException {
        if (y < 0 || Double.isNaN(y)) {
            throw new InvalidMovementException("Invalid y position", x, y);
        }
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) throws InvalidMovementException {
        if (width <= 0) {
            throw new InvalidMovementException("Width must be positive", x, y);
        }
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) throws InvalidMovementException {
        if (height <= 0) {
            throw new InvalidMovementException("Height must be positive", x, y);
        }
        this.height = height;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) throws InvalidMovementException {
        if (speed < 0) {
            throw new InvalidMovementException("Speed cannot be negative", x, y);
        }
        this.speed = speed;
    }
}
