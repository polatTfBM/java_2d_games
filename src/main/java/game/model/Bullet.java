package game.model;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Represents a bullet shot by the player.
 */
public class Bullet extends GameObject {
    private int damage;
    private Color color;
    private boolean active;
    private Double acceleration;
    private byte variant;

    public Bullet(double x, double y, int width, int height) {
        this(x, y, width, height, 6, Color.YELLOW);
    }

    public Bullet(double x, double y, int width, int height, int damage, Color color) {
        super(x, y, width, height, 8);
        this.damage = damage;
        this.color = color;
        this.active = true;
        this.acceleration = Double.valueOf(1.0);
        this.variant = (byte) (damage % 2);
    }

    @Override
    public void update() {
        setInternalY(getY() - getSpeed());
        if (getY() < -getHeight()) {
            active = false;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
    }

    private void setInternalY(double newY) {
        try {
            setY(newY);
        } catch (Exception e) {
            active = false;
        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    public byte getVariant() {
        return variant;
    }
}
