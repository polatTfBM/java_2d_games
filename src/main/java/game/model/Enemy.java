package game.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * Represents a basic enemy.
 */
public class Enemy extends GameObject {
    private Color color;
    private int damage;
    private boolean active;
    private short reward;
    private Random random;

    public Enemy(double x, double y, int width, int height) {
        this(x, y, width, height, 2.0, Color.RED);
    }

    public Enemy(double x, double y, int width, int height, double speed, Color color) {
        super(x, y, width, height, speed);
        this.color = color;
        this.damage = 1;
        this.active = true;
        this.reward = (short) (width % 5 + 1);
        this.random = new Random();
    }

    @Override
    public void update() {
        setInternalY(getY() + getSpeed());
        if (getY() > 620) {
            active = false;
        }
        if (random.nextInt(1000) % 7 == 0) {
            setInternalX(getX() + (random.nextBoolean() ? 1 : -1) * getSpeed());
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
    }

    protected void setInternalX(double newX) {
        try {
            setX(newX);
        } catch (Exception e) {
            active = false;
        }
    }

    protected void setInternalY(double newY) {
        try {
            setY(newY);
        } catch (Exception e) {
            active = false;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public short getReward() {
        return reward;
    }
}
