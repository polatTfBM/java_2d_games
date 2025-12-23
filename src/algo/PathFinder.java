package algo;

import model.Maze;
import model.PathResult;

public interface PathFinder {
    PathResult findPath(Maze maze);
    String getName();
}
