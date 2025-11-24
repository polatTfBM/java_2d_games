package game.model;

import java.awt.Color;

/**
 * Slow but resilient enemy variant.
 */
public class SlowEnemy extends Enemy {
    private int armor;
    private boolean heavy;
    private float wobble;
    private String title;

    public SlowEnemy(double x, double y) {
        this(x, y, 30, 30, 1.5);
    }

    public SlowEnemy(double x, double y, int width, int height, double speed) {
        super(x, y, width, height, speed, Color.GREEN);
        this.armor = 2;
        this.heavy = true;
        this.wobble = 0.2f;
        this.title = "Slow Guardian";
    }

    @Override
    public void update() {
        super.update();
        wobble += 0.1f;
        setInternalX(getX() + Math.cos(wobble) * 0.5);
    }

    public int getArmor() {
        return armor;
    }

    public boolean isHeavy() {
        return heavy;
    }

    public String getTitle() {
        return title;
    }
}
