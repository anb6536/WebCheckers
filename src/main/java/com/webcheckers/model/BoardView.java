package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * a board with all the information about board and pieces
 */
public class BoardView {
    private List<Row> rows;
    /**
     * A comparator to keep the rows in order
     */
    private static final RowComparator comparator = new RowComparator();

    /**
     * Creates a view of a board from a list of rows
     * 
     * @param rows the rows in the board
     */
    public BoardView(List<Row> rows) {
        this.rows = rows;
        this.rows.sort(comparator);
    }

    /**
     * Gets a space from a position
     * 
     * @param position The position that you want to get the information of
     * @return the Space associated with the position
     * @throws ArrayIndexOutOfBoundsException if the Position is outside of the
     *                                        board
     */
    public Space getSpace(Position position) throws ArrayIndexOutOfBoundsException {
        return getSpace(position.row, position.cell);
    }

    /**
     * Gets a space from a position
     * 
     * @param row    The row associated with the Space
     * @param column The column associated with the Space
     * @return the Space associated by the row,column index
     * @throws ArrayIndexOutOfBoundsException if the row or the column is outside of
     *                                        the board
     */
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

    /**
     * Clones the View so we don't have to do internal flips all the time
     */
    public BoardView clone() {
        ArrayList<Row> rowList = new ArrayList<Row>();
        rowList.addAll(rows);
        return new BoardView(rowList);
    }

    /**
     * Determines if this boardview has any red pieces left
     * 
     * @return true if it does
     */
    public boolean hasRedPieces() {
        for (Row row : rows) {
            if (row.hasRedPieces())
                return true;
        }
        return false;
    }

    /**
     * Determines if this BoardView has any white pieces left
     * 
     * @return true if it does
     */
    public boolean hasWhitePieces() {
        for (Row row : rows) {
            if (row.hasWhitePieces())
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String buildString = "";
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Space space = getSpace(x, y);
                Piece piece = space.getPiece();
                if (piece == null) {
                    buildString += "*";
                    continue;
                }
                if (piece.getColor() == Piece.Color.WHITE) {
                    buildString += "W";
                } else if (piece.getColor() == Piece.Color.RED) {
                    buildString += "R";
                }
            }
            buildString += "\n";
        }
        return buildString;
    }

}
