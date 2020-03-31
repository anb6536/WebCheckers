package com.webcheckers.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * a board with all the information about board and pieces
 */
public class BoardView {
    private List<Row> rows;
    private static final RowComparator comparator = new RowComparator();

    // create an already created board
    public BoardView(List<Row> rows) {
        this.rows = rows;
        this.rows.sort(comparator);
    }
    public Space getSpace(Position position) throws ArrayIndexOutOfBoundsException { 
        return getSpace(position.row, position.cell);
    }
    public Space getSpace(int row, int column) throws ArrayIndexOutOfBoundsException {
        if (row >= rows.size()) {
            throw new ArrayIndexOutOfBoundsException("Row index out of bounds");
        }
        if (rows.get(row).getIndex() != row) {
            rows.sort(comparator);
            Row rowWanted = rows.stream().filter((i) -> {
                return i.getIndex() == row;
            }).findFirst().get();
            return rowWanted.getSpace(column);
        }
        Row rowWanted = rows.get(row);
        return rowWanted.getSpace(column);
    }

    /**
     * simple getter
     * 
     * @return the reference to the rows
     */
    public List<Row> getRows() {
        return this.rows;
    }

    /**
     * iterator so that we can iterate through the rows of the board
     * 
     * @return an iterator through the rows
     */
    public Iterator<Row> iterator() {
        return this.rows.iterator();
    }

    public BoardView clone() {
        ArrayList<Row> rowList = new ArrayList<Row>();
        rowList.addAll(rows);
        return new BoardView(rowList);
    }

    @Override
    public String toString() {
        String buildString = "";
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Space space = getSpace(x,y);
                Piece piece = space.getPiece();
                if (piece == null) { buildString += "*"; continue;}
                if (piece.getColor() == Piece.Color.WHITE) { buildString += "W"; }
                else if (piece.getColor() == Piece.Color.RED)  { buildString += "R"; }
            }
            buildString += "\n";
        }
        return buildString;
    }

}
