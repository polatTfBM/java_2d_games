package model;

import java.util.List;

public class PathResult {
    private final List<Cell> visitedOrder;
    private final List<Cell> path;
    private final long elapsedMillis;
    private final int cost;

    public PathResult(List<Cell> visitedOrder, List<Cell> path, long elapsedMillis, int cost) {
        this.visitedOrder = visitedOrder;
        this.path = path;
        this.elapsedMillis = elapsedMillis;
        this.cost = cost;
    }

    public List<Cell> getVisitedOrder() {
        return visitedOrder;
    }

    public List<Cell> getPath() {
        return path;
    }

    public long getElapsedMillis() {
        return elapsedMillis;
    }

    public int getCost() {
        return cost;
    }
}
