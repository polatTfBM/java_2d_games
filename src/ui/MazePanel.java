package ui;

import model.Cell;
import model.Maze;
import model.PathResult;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

public class MazePanel extends JPanel {
    private static final int CELL_SIZE = 25;
    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color PATH_COLOR = Color.WHITE;
    private static final Color START_COLOR = Color.GREEN;
    private static final Color END_COLOR = Color.RED;
    private static final Color VISITED_COLOR = Color.BLUE;
    private static final Color FINAL_PATH_COLOR = Color.ORANGE;

    private final Maze maze;
    private final boolean[][] visited;
    private final boolean[][] finalPath;

    private Timer timer;
    private List<Cell> visitedOrder;
    private List<Cell> path;
    private int visitIndex;
    private int pathIndex;
    private Runnable onAnimationComplete;

    public MazePanel(Maze maze) {
        this.maze = maze;
        this.visited = new boolean[maze.getHeight()][maze.getWidth()];
        this.finalPath = new boolean[maze.getHeight()][maze.getWidth()];
        setPreferredSize(new Dimension(maze.getWidth() * CELL_SIZE, maze.getHeight() * CELL_SIZE));
    }

    public void resetAnimation() {
        if (timer != null) {
            timer.stop();
        }
        for (int r = 0; r < visited.length; r++) {
            for (int c = 0; c < visited[r].length; c++) {
                visited[r][c] = false;
                finalPath[r][c] = false;
            }
        }
        visitedOrder = null;
        path = null;
        visitIndex = 0;
        pathIndex = 0;
        repaint();
    }

    public void animate(PathResult result, Runnable onComplete) {
        resetAnimation();
        this.onAnimationComplete = onComplete;
        this.visitedOrder = result.getVisitedOrder();
        this.path = result.getPath();
        this.visitIndex = 0;
        this.pathIndex = 0;

        timer = new Timer(20, e -> stepAnimation());
        timer.start();
    }

    private void stepAnimation() {
        if (visitedOrder != null && visitIndex < visitedOrder.size()) {
            Cell cell = visitedOrder.get(visitIndex);
            markVisited(cell);
            visitIndex++;
            repaint();
            return;
        }
        if (path != null && pathIndex < path.size()) {
            Cell cell = path.get(pathIndex);
            markPath(cell);
            pathIndex++;
            repaint();
            return;
        }
        if (timer != null) {
            timer.stop();
        }
        if (onAnimationComplete != null) {
            onAnimationComplete.run();
        }
    }

    private void markVisited(Cell cell) {
        if (cell == null) {
            return;
        }
        if (cell.isStart() || cell.isEnd()) {
            return;
        }
        visited[cell.getRow()][cell.getCol()] = true;
    }

    private void markPath(Cell cell) {
        if (cell == null) {
            return;
        }
        if (cell.isStart() || cell.isEnd()) {
            return;
        }
        finalPath[cell.getRow()][cell.getCol()] = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < maze.getHeight(); r++) {
            for (int c = 0; c < maze.getWidth(); c++) {
                Cell cell = maze.getCell(r, c);
                Color color = PATH_COLOR;
                if (cell == null || cell.isWall()) {
                    color = WALL_COLOR;
                } else if (cell.isStart()) {
                    color = START_COLOR;
                } else if (cell.isEnd()) {
                    color = END_COLOR;
                }
                if (visited[r][c]) {
                    color = VISITED_COLOR;
                }
                if (finalPath[r][c]) {
                    color = FINAL_PATH_COLOR;
                }
                g.setColor(color);
                g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.GRAY);
                g.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}
