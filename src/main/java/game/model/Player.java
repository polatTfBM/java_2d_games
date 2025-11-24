package game.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Objects;

import game.exception.InvalidMovementException;
import game.interfaces.Movable;
import game.interfaces.Shootable;
import game.util.SpriteLoader;

/**
 * Represents the player spaceship.
 */
public class Player extends CharacterObject implements Movable, Shootable {
    private Image sprite;
    private int speedBoost;
    private int reloadCounter;
    private Boolean ready;
    private String motto;

    public Player() throws InvalidMovementException {
        this(0, 0, 40, 40, 3, 3, "Hero", 0, SpriteLoader.createPlayerSprite());
    }

    public Player(double x, double y, int width, int height, int health, int maxHealth, String name, Integer bonus, Image sprite)
            throws InvalidMovementException {
        super(x, y, width, height, health, maxHealth, name, bonus);
        setSprite(sprite);
        this.speedBoost = 4;
        this.reloadCounter = 0;
        this.ready = Boolean.TRUE;
        this.motto = "Courage above all";
    }

    @Override
    public void update() {
        if (reloadCounter > 0) {
            reloadCounter--;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(sprite, (int) getX(), (int) getY(), getWidth(), getHeight(), null);
        drawHealthBar(g2d);
    }

    @Override
    public void moveLeft() {
        try {
            double next = getX() - (getSpeed() + speedBoost);
            if (next < 0) {
                throw new InvalidMovementException("Left boundary reached", next, getY());
            }
            setX(next);
        } catch (InvalidMovementException e) {
            setInternalPosition(0, getY());
        }
    }

    @Override
    public void moveRight() {
        try {
            double next = getX() + (getSpeed() + speedBoost);
            if (next + getWidth() > 800) {
                throw new InvalidMovementException("Right boundary reached", next, getY());
            }
            setX(next);
        } catch (InvalidMovementException e) {
            setInternalPosition(800 - getWidth(), getY());
        }
    }

    @Override
    public void moveUp() {
        try {
            double next = getY() - (getSpeed() + 1);
            if (next < 0) {
                throw new InvalidMovementException("Top boundary reached", getX(), next);
            }
            setY(next);
        } catch (InvalidMovementException e) {
            setInternalPosition(getX(), 0);
        }
    }

    @Override
    public void moveDown() {
        try {
            double next = getY() + (getSpeed() + 1);
            if (next + getHeight() > 600) {
                throw new InvalidMovementException("Bottom boundary reached", getX(), next);
            }
            setY(next);
        } catch (InvalidMovementException e) {
            setInternalPosition(getX(), 600 - getHeight());
        }
    }

    @Override
    public Bullet shoot() {
        if (canShoot()) {
            reloadCounter = reloadTime();
            return new Bullet(getX() + getWidth() / 2 - 2, getY(), 4, 10);
        }
        return null;
    }

    @Override
    public boolean canShoot() {
        return reloadCounter <= 0 && ready.booleanValue();
    }

    @Override
    public int reloadTime() {
        return 12;
    }

    @Override
    public void takeDamage(int damage) throws InvalidMovementException {
        if (isInvulnerable()) {
            return;
        }
        setHealth(getHealth() - damage);
    }

    @Override
    public boolean isAlive() {
        return getHealth() > 0;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) throws InvalidMovementException {
        if (sprite == null) {
            throw new InvalidMovementException("Sprite cannot be null", getX(), getY());
        }
        this.sprite = sprite;
    }

    public int getSpeedBoost() {
        return speedBoost;
    }

    public void setSpeedBoost(int speedBoost) throws InvalidMovementException {
        if (speedBoost < 0) {
            throw new InvalidMovementException("Speed boost must be positive", getX(), getY());
        }
        this.speedBoost = speedBoost;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) throws InvalidMovementException {
        if (Objects.isNull(motto) || motto.trim().isEmpty()) {
            throw new InvalidMovementException("Motto cannot be empty", getX(), getY());
        }
        this.motto = motto;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) throws InvalidMovementException {
        if (ready == null) {
            throw new InvalidMovementException("Ready state required", getX(), getY());
        }
        this.ready = ready;
    }

    private void setInternalPosition(double x, double y) {
        try {
            setX(x);
            setY(y);
        } catch (InvalidMovementException e) {
            // ignore as bounds are already enforced
        }
    }
}
