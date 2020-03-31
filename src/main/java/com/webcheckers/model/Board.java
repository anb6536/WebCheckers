package com.webcheckers.model;

import static com.webcheckers.model.Piece.Color.RED;
import static com.webcheckers.model.Piece.Color.WHITE;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private BoardView view;

    public Board(BoardView view) {
        this.view = view;
    }

    public Board(List<Row> rows) {
        this.view = new BoardView(rows);
    }

    public BoardView getBoardView() {
        return view;
    }
    public boolean backupMove(MoveInformation moveInformation) {
        // TODO: Actually backup the move
        return true;
    }
    public boolean validateMove(Move move, boolean whiteMove) {
        return MoveValidator.validateMove(move, view, whiteMove);
    }

    public boolean makeMove(Move move, boolean whiteMove) {
        if (!validateMove(move, whiteMove)) {
            return false;
        }
        // Manually invert the move so it goes to right place on board
        if (whiteMove) { move = move.invertMove(); }
        setPiece(move.end, view.getSpace(move.start).getPiece());
        removePiece(move.start);
        return true;
    }

    private void setPiece(Position position, Piece piece) {
        setPiece(position.row, position.cell, piece);
    }

    private void setPiece(int row, int column, Piece piece) {
        view.getSpace(row, column).setPiece(piece);
    }

    private void removePiece(Position position) {
        removePiece(position.row, position.cell);
    }

    private void removePiece(int row, int column) {
        setPiece(row, column, null);
    }

    /**
     * create a BoardView with correct starting positions
     * 
     * @return the start of a board
     */
    public static Board makeBoard() {

        // create the list of rows
        List<Row> rows = new ArrayList<>();

        // add a row to the list 8 times
        for (int i = 0; i < 8; i++) {

            // create the list of spaces for this row
            List<Space> spaces = new ArrayList<>();

            // add a space to the row 8 times
            for (int j = 0; j < 8; j++) {
                Space space = new Space(j);

                // if it is an even row
                if (i % 2 == 0) {

                    // if it is an even row and a even column
                    if (j % 2 == 0) {
                        // set the color to light
                        space.setColor(Space.Color.LIGHT);
                    }
                    // if it is an even row and an odd column
                    else {
                        // set the color to dark
                        space.setColor(Space.Color.DARK);
                    }
                }
                // if it is an odd row
                else {
                    // if it is an odd row and an odd column
                    if (j % 2 != 0) {
                        // set the color to light
                        space.setColor(Space.Color.LIGHT);
                    }
                    // if it is an odd row and an even column
                    else {
                        // set the color to dark
                        space.setColor(Space.Color.DARK);
                    }
                }
                // add the list of spaces to the row
                spaces.add(space);
            }
            // add the row to the list of rows
            Row row = new Row(i, spaces);
            rows.add(row);
        }
        // return the boardview of the board
        return new Board(rows);
    }

    /**
     *
     * @param boardView the reference of the board filled with spaces
     */
    public void addPieces() {
        // get the rows
        List<Row> rows = view.getRows();
        int i = 0;

        // for every row, add pieces to every proper space
        for (Row row : rows) {
            // get the spaces for this row
            List<Space> spaces = row.getSpaces();
            for (Space space : spaces) {
                // if the row should be filled with red pieces
                if (i >= 5) {
                    // if the space is a dark square
                    if (space.isValid()) {
                        // add a red piece to the space on the board
                        space.setPiece(RED);
                    }
                }
                // if the row should be filled with white pieces
                else if (i <= 2) {
                    // if the space is a dark square
                    if (space.isValid()) {
                        // add a white piece to the space on the board
                        space.setPiece(WHITE);
                    }
                }
            }
            i++;
        }
    }

    /**
     * flip the board so that the other player has a correct facing board
     * 
     * @param boardView the filled out board for a game that is facing the original
     *                  way
     * @return the flipped board
     */
    public Board flipBoard() {
        Row[] rows = new Row[8];
        List<Row> rows1 = view.getRows();
        List<Row> newRow = new ArrayList<>();

        // start at the other end of the board
        int i = 7;
        // for every row in the original board
        for (Row row : rows1) {
            Space[] spaces = new Space[8];
            List<Space> spaces1 = row.getSpaces();
            List<Space> newSpaces = new ArrayList<>();
            int j = 7;

            // for every space in the original row
            for (Space space : spaces1) {
                Space newSpace = new Space(j);
                newSpace.setColor(space.getColor());
                newSpace.setPiece(space.getPiece());
                spaces[j] = newSpace;
                j--;
            }
            for (int k = 0; k < 8; k++) {
                newSpaces.add(spaces[k]);
            }
            // make a new row with the correct index
            Row newRow1 = new Row(i, newSpaces);
            rows[i] = newRow1;
            i--;
        }
        for (int j = 0; j < 8; j++) {
            newRow.add(rows[j]);
        }

        // return the flipped board
        return new Board(newRow);
    }
}
