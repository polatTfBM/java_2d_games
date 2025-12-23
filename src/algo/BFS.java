package algo;

import model.Cell;
import model.Maze;
import model.PathResult;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BFS implements PathFinder {
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    @Override
    public PathResult findPath(Maze maze) {
        long startTime = System.nanoTime();
        List<Cell> visitedOrder = new ArrayList<>();
        Deque<Cell> queue = new ArrayDeque<>();
        Map<Cell, Cell> parent = new HashMap<>();
        Set<Cell> visited = new HashSet<>();

        Cell start = maze.getStart();
        Cell end = maze.getEnd();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            visitedOrder.add(current);
            if (current.equals(end)) {
                break;
            }
            for (int[] d : DIRECTIONS) {
                int nr = current.getRow() + d[0];
                int nc = current.getCol() + d[1];
                if (!maze.isWalkable(nr, nc)) {
                    continue;
                }
                Cell neighbor = maze.getCell(nr, nc);
                if (neighbor != null && visited.add(neighbor)) {
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        List<Cell> path = reconstructPath(start, end, parent);
        long elapsed = (System.nanoTime() - startTime) / 1_000_000;
        int cost = path.isEmpty() ? 0 : path.size() - 1;
        return new PathResult(visitedOrder, path, elapsed, cost);
    }

    private List<Cell> reconstructPath(Cell start, Cell end, Map<Cell, Cell> parent) {
        List<Cell> path = new ArrayList<>();
        if (!end.equals(start) && !parent.containsKey(end)) {
            return path;
        }
        Cell current = end;
        path.add(current);
        while (parent.containsKey(current)) {
            current = parent.get(current);
            path.add(0, current);
        }
        return path;
    }

    @Override
    public String getName() {
        return "BFS";
    }
}
