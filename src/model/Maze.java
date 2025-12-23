package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Maze {
    private final List<List<Cell>> grid;
    private final int width;
    private final int height;
    private final Cell start;
    private final Cell end;

    public Maze(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Maze file is empty");
        }
        int maxWidth = 0;
        for (String line : lines) {
            if (line.length() > maxWidth) {
                maxWidth = line.length();
            }
        }
        this.width = maxWidth;
        this.height = lines.size();
        this.grid = new ArrayList<>();

        Cell foundStart = null;
        Cell foundEnd = null;

        for (int r = 0; r < height; r++) {
            List<Cell> row = new ArrayList<>();
            String line = lines.get(r);
            for (int c = 0; c < width; c++) {
                char value = c < line.length() ? line.charAt(c) : ' ';
                Cell cell = new Cell(r, c, value);
                row.add(cell);
                if (cell.isStart()) {
                    if (foundStart != null) {
                        throw new IllegalArgumentException("Maze must contain exactly one start");
                    }
                    foundStart = cell;
                }
                if (cell.isEnd()) {
                    if (foundEnd != null) {
                        throw new IllegalArgumentException("Maze must contain exactly one end");
                    }
                    foundEnd = cell;
                }
            }
            grid.add(row);
        }
        if (foundStart == null || foundEnd == null) {
            throw new IllegalArgumentException("Maze must contain start and end");
        }
        this.start = foundStart;
        this.end = foundEnd;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= height || col < 0 || col >= width) {
            return null;
        }
        return grid.get(row).get(col);
    }

    public Cell getStart() {
        return start;
    }

    public Cell getEnd() {
        return end;
    }

    public boolean isWalkable(int row, int col) {
        Cell cell = getCell(row, col);
        return cell != null && !cell.isWall();
    }
}
