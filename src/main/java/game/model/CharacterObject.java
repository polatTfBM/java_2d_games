package game.model;

import java.awt.Graphics2D;

import game.exception.InvalidMovementException;

/**
 * Represents a game object with health.
 */
public abstract class CharacterObject extends GameObject {
    private int health;
    private int maxHealth;
    private String name;
    private Integer scoreBonus;
    private boolean invulnerable;

    public CharacterObject(double x, double y, int width, int height, int health, int maxHealth, String name, Integer scoreBonus) {
        super(x, y, width, height);
        this.health = health;
        this.maxHealth = maxHealth;
        this.name = name;
        this.scoreBonus = scoreBonus;
        this.invulnerable = false;
    }

    public CharacterObject(double x, double y, int width, int height) {
        this(x, y, width, height, 1, 1, "Unknown", Integer.valueOf(0));
    }

    public abstract void takeDamage(int damage) throws InvalidMovementException;

    public abstract boolean isAlive();

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public void drawHealthBar(Graphics2D g2d) {
        int barWidth = getWidth();
        int barHeight = 5;
        int healthWidth = (int) ((double) health / (double) maxHealth * barWidth);
        g2d.fillRect((int) getX(), (int) getY() - barHeight - 2, healthWidth, barHeight);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) throws InvalidMovementException {
        if (health < 0 || health > maxHealth) {
            throw new InvalidMovementException("Health out of range", getX(), getY());
        }
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) throws InvalidMovementException {
        if (maxHealth <= 0) {
            throw new InvalidMovementException("Max health must be positive", getX(), getY());
        }
        this.maxHealth = maxHealth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidMovementException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidMovementException("Name cannot be empty", getX(), getY());
        }
        this.name = name;
    }

    public Integer getScoreBonus() {
        return scoreBonus;
    }

    public void setScoreBonus(Integer scoreBonus) throws InvalidMovementException {
        if (scoreBonus == null || scoreBonus < 0) {
            throw new InvalidMovementException("Score bonus invalid", getX(), getY());
        }
        this.scoreBonus = scoreBonus;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }
}
