package model;

public class Cell {
    private final int row;
    private final int col;
    private final char value;

    public Cell(int row, int col, char value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getValue() {
        return value;
    }

    public boolean isWall() {
        return value == '|' || value == '_';
    }

    public boolean isStart() {
        return value == 'S';
    }

    public boolean isEnd() {
        return value == 'E';
    }
}
