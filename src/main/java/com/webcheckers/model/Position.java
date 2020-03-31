package com.webcheckers.model;

/**
 * Helper class for defining a move, is modeled after position.js
 */
public class Position {
    // These are public to facilitate easy GSON stuff, unluckily for us, this is not
    // C# and properties are not a language defined existence.
    public int row;
    public int cell;
    public static Position makePosition(int row, int column) { 
        Position pos = new Position();
        pos.row = row;
        pos.cell = column;
        return pos;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cell;
        result = prime * result + row;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if (cell != other.cell)
            return false;
        if (row != other.row)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Position{" +
                "row=" + row +
                ", cell=" + cell +
                '}';
    }

    public Position flip() {
        return makePosition(7-row,7-cell);
    }
}