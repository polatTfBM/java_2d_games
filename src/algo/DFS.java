package algo;

import model.Cell;
import model.Maze;
import model.PathResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class DFS implements PathFinder {
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    @Override
    public PathResult findPath(Maze maze) {
        long startTime = System.nanoTime();
        List<Cell> visitedOrder = new ArrayList<>();
        Stack<Cell> stack = new Stack<>();
        Map<Cell, Cell> parent = new HashMap<>();
        Set<Cell> visited = new HashSet<>();

        Cell start = maze.getStart();
        Cell end = maze.getEnd();
        stack.push(start);

        while (!stack.isEmpty()) {
            Cell current = stack.pop();
            if (!visited.add(current)) {
                continue;
            }
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
                if (neighbor != null && !visited.contains(neighbor)) {
                    if (!parent.containsKey(neighbor)) {
                        parent.put(neighbor, current);
                    }
                    stack.push(neighbor);
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
        return "DFS";
    }
}
