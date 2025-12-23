package algo;

import model.Cell;
import model.Maze;
import model.PathResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Dijkstra implements PathFinder {
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private static class Node implements Comparable<Node> {
        private final Cell cell;
        private final int cost;

        Node(Cell cell, int cost) {
            this.cell = cell;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    @Override
    public PathResult findPath(Maze maze) {
        long startTime = System.nanoTime();
        List<Cell> visitedOrder = new ArrayList<>();
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Map<Cell, Integer> distance = new HashMap<>();
        Map<Cell, Cell> parent = new HashMap<>();
        Set<Cell> settled = new HashSet<>();

        Cell start = maze.getStart();
        Cell end = maze.getEnd();
        queue.add(new Node(start, 0));
        distance.put(start, 0);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            Cell current = node.cell;
            if (!settled.add(current)) {
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
                int newCost = distance.getOrDefault(current, Integer.MAX_VALUE) + 1;
                if (newCost < distance.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distance.put(neighbor, newCost);
                    parent.put(neighbor, current);
                    queue.add(new Node(neighbor, newCost));
                }
            }
        }

        List<Cell> path = reconstructPath(start, end, parent);
        long elapsed = (System.nanoTime() - startTime) / 1_000_000;
        int cost = distance.getOrDefault(end, 0);
        if (path.isEmpty()) {
            cost = 0;
        }
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
        return "Dijkstra";
    }
}
