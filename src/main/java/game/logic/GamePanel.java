package game.logic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import game.exception.InvalidMovementException;
import game.model.Bullet;
import game.model.Enemy;
import game.model.FastEnemy;
import game.model.GameObject;
import game.model.Player;
import game.model.SlowEnemy;
import game.util.Box;
import game.util.Constants;
import game.util.ScoreManager;

/**
 * Panel responsible for running the game loop and rendering.
 */
public class GamePanel extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;
    private final Player player;
    private final List<GameObject> enemies;
    private final List<Bullet> bullets;
    private final Timer timer;
    private final Random random;
    private int score;
    private int spawnCounter;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private boolean shooting;
    private int[] inputHistory;
    private int[][] tileMap;
    private LinkedList<String> events;
    private byte difficulty;

    public GamePanel() throws InvalidMovementException {
        setBackground(Constants.BACKGROUND_COLOR);
        setFocusable(true);
        addKeyListener(this);
        this.player = new Player(Constants.PLAYER_START_X, Constants.PLAYER_START_Y, 40, 40, 5, 5, "Pilot", 10,
                game.util.SpriteLoader.createPlayerSprite());
        this.enemies = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.random = new Random();
        this.score = 0;
        this.spawnCounter = 0;
        this.left = false;
        this.right = false;
        this.up = false;
        this.down = false;
        this.shooting = false;
        this.inputHistory = new int[] { 0, 1, 2, 3, 4 };
        this.tileMap = new int[5][5];
        this.events = new LinkedList<>();
        this.difficulty = 1;
        this.timer = new Timer(16, e -> loop());
        this.timer.start();
        fillTileMap();
    }

    private void loop() {
        try {
            update();
        } catch (Exception e) {
            events.add("Loop issue: " + e.getMessage());
        }
        repaint();
    }

    private void update() throws InvalidMovementException {
        handleInput();
        spawnCounter++;
        if (spawnCounter % 50 == 0) {
            spawnEnemy();
        }
        if (score >= 100 && difficulty <= 2) {
            difficulty++;
        }
        if (shooting) {
            Bullet b = player.shoot();
            if (b != null) {
                bullets.add(b);
                new Box<>(b, "bullet", 1, false).describe();
            }
        }
        for (GameObject enemy : enemies) {
            enemy.update();
        }
        for (Bullet bullet : bullets) {
            bullet.update();
        }
        checkCollisions();
        cleanUp();
        ScoreManager.saveInfo(java.time.LocalDateTime.now(), score);
    }

    private void handleInput() {
        if (left) {
            player.moveLeft();
        }
        if (right) {
            player.moveRight();
        }
        if (up) {
            player.moveUp();
        }
        if (down) {
            player.moveDown();
        }
    }

    private void spawnEnemy() {
        int x = random.nextInt(Constants.WINDOW_WIDTH - 30);
        boolean fast = random.nextBoolean();
        GameObject enemy;
        if (fast) {
            enemy = new FastEnemy(x, -20);
        } else {
            enemy = new SlowEnemy(x, -30);
        }
        enemies.add(enemy);
    }

    private void checkCollisions() {
        Iterator<GameObject> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            GameObject enemy = enemyIterator.next();
            for (Bullet bullet : bullets) {
                if (enemy.intersects(bullet)) {
                    enemyIterator.remove();
                    bullet.setActive(false);
                    score += 10;
                    break;
                }
            }
            if (enemy.intersects(player)) {
                try {
                    player.takeDamage(1);
                } catch (InvalidMovementException e) {
                    events.add("Damage error" + e.getMessage());
                }
            }
        }
    }

    private void cleanUp() {
        bullets.removeIf(b -> !b.isActive());
        enemies.removeIf(e -> !((Enemy) e).isActive());
        int index = 0;
        do {
            if (index < bullets.size() && bullets.get(index).getY() < 0) {
                bullets.get(index).setActive(false);
            }
            index++;
        } while (index < bullets.size());
    }

    private void fillTileMap() {
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                tileMap[i][j] = (i + j) % 2;
            }
        }
        int counter = 0;
        while (counter < inputHistory.length) {
            inputHistory[counter] = counter * 2;
            counter++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GRAY);
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                int size = 20;
                g2d.drawRect(i * size, j * size, size, size);
            }
        }
        player.draw(g2d);
        for (GameObject enemy : enemies) {
            enemy.draw(g2d);
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g2d);
        }
        drawHud(g2d);
    }

    private void drawHud(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Score: " + score, 10, 20);
        g2d.drawString("Health: " + player.getHealth(), 10, 40);
        g2d.drawString("Enemies: " + enemies.size(), 10, 60);
        g2d.drawString("Date: " + new Date().toString(), 10, 80);
        String debug = Constants.isDebugMode() ? "ON" : "OFF";
        g2d.drawString("Debug: " + debug, 10, 100);
        g2d.drawString("Data: " + ScoreManager.showInfo(), 10, 120);
        g2d.drawString("Analyze: " + ScoreManager.analyzeDates(), 10, 140);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_SPACE:
                shooting = true;
                break;
            case KeyEvent.VK_1:
                difficulty = 1;
                break;
            case KeyEvent.VK_2:
                difficulty = 2;
                break;
            case KeyEvent.VK_3:
                difficulty = 3;
                break;
            case KeyEvent.VK_4:
                difficulty = 4;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_SPACE:
                shooting = false;
                break;
            default:
                break;
        }
    }
}
