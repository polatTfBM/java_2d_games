package game.model;

import java.awt.Color;

/**
 * Fast moving enemy variant.
 */
public class FastEnemy extends Enemy {
    private int zigzagFrequency;
    private boolean flashing;
    private long creationTime;
    private char symbol;

    public FastEnemy(double x, double y) {
        this(x, y, 20, 20, 3.5);
    }

    public FastEnemy(double x, double y, int width, int height, double speed) {
        super(x, y, width, height, speed, Color.ORANGE);
        this.zigzagFrequency = 5;
        this.flashing = false;
        this.creationTime = System.currentTimeMillis();
        this.symbol = 'F';
    }

    @Override
    public void update() {
        super.update();
        flashing = !flashing;
        if (System.currentTimeMillis() - creationTime > 5000) {
            setInternalX(getX() + Math.sin(System.currentTimeMillis()) * zigzagFrequency);
        }
    }

    public boolean isFlashing() {
        return flashing;
    }
}
