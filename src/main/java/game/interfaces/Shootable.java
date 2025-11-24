package game.interfaces;

import game.model.Bullet;

/**
 * Represents objects that can shoot bullets.
 */
public interface Shootable {
    Bullet shoot();
    boolean canShoot();
    int reloadTime();
}
