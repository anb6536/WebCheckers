package com.webcheckers.model;

import static com.webcheckers.model.Piece.Color.RED;
import static com.webcheckers.model.Piece.Color.WHITE;

import java.util.ArrayList;
import java.util.List;

import com.webcheckers.util.Pair;

public class Board {
    private BoardView view;

    /**
     * Creates a board from a view
     * 
     * @param view A boardview for the internal board
     */
    public Board(BoardView view) {
        this.view = view;
    }

    /**
     * Creates a board from a list of rows
     * 
     * @param rows The rows used in the BoardView
     */
    public Board(List<Row> rows) {
        this.view = new BoardView(rows);
    }

    /**
     * Gets the view associated with this board
     * 
     * @return A BoardView representing this board
     */
    public BoardView getBoardView() {
        return view;
    }

    /**
     * Validates a move given
     * 
     * @param move      The move to validate
     * @param whiteMove True if the white player is moving
     * @return A pair<Boolean, MoveInformation> representing if this move was
     *         validated successfully and if it was, what move was made.
     */
    public Pair<Boolean, MoveInformation> validateMove(Move move, boolean whiteMove) {
        return MoveValidator.validateMove(move, view, whiteMove);
    }

    /**
     * Actually makes a move given
     * 
     * @param move      The move to make
     * @param whiteMove True if the moving player is White, if it is it flips the
     *                  move to properly make it
     * @return true if the move was successfully made
     */
    public boolean makeMove(Move move, boolean whiteMove) {
        Pair<Boolean, MoveInformation> validationInfo = validateMove(move, whiteMove);
        MoveInformation moveInfo = validationInfo.getValue();
        if (!validationInfo.getKey()) {
            return false;
        }
        // Manually invert the move so it goes to right place on board
        if (whiteMove) {
            move = move.invertMove();
        }
        setPiece(move.end, view.getSpace(move.start).getPiece());
        removePiece(move.start);
        if (moveInfo.isJumpMove()) {
            removePiece(moveInfo.removedPosition);
        }

        return true;
    }

    /**
     * Sets a position to have a piece
     * 
     * @param position the position of the piece to set
     * @param piece    the piece to set
     */
    private void setPiece(Position position, Piece piece) {
        setPiece(position.row, position.cell, piece);
    }

    /**
     * Sets a location to have a piece
     * 
     * @param row    the row of the piece
     * @param column the column of the piece
     * @param piece  the piece you want to set
     */
    private void setPiece(int row, int column, Piece piece) {
        view.getSpace(row, column).setPiece(piece);
    }

    private void removePiece(Position position) {
        removePiece(position.row, position.cell);
    }

    /**
     * 
     * Remove a piece at a row and column
     * 
     * @param row    the row of the piece to remove
     * @param column the column of the piece to remove
     */
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
     * Adds the default pieces to their starting locations
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

    /**
     * Determines if this board has any red pieces
     * 
     * @return true if it has red pieces
     */
    public boolean hasRedPieces() {
        return view.hasRedPieces();
    }

    /**
     * Determines if this board has any white pieces
     * 
     * @return true if it has any white pieces
     */
    public boolean hasWhitePieces() {
        return view.hasWhitePieces();

    }
}
