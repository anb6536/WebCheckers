package com.webcheckers.model;

import javafx.geometry.Pos;

import java.util.Base64;
import java.util.logging.Logger;
import java.util.logging.LogManager;

public class MoveValidator {
    private static final Logger LOG = Logger.getLogger(MoveValidator.class.getName());
    public final static int NUM_ROWS = 8;
    public final static int NUM_COLS = 8;

    public static boolean validateMove(Move playerMove, BoardView boardView) {
        boolean move_on_board = moveIsOnBoard(playerMove);
        boolean landing_space_available = spaceIsAvailable(boardView, playerMove);
        boolean is_single_proper_diagonal = moveIsSingleProperDiagonal(boardView, playerMove);

        // If the move is not even on the board, return false immediately
        if (!move_on_board) return false;

        // Represents single move without jump (Open space and a diagonal movement)
        if (landing_space_available && is_single_proper_diagonal) {
            return true;
        }

        // Stub, will complete soon
        return false;
    }

    /**
     * Check if the start and end positions of a Move is on the board
     * @param playerMove The move to be checked
     * @return true if move is on board, false otherwise
     */
    public static boolean moveIsOnBoard(Move playerMove) {
        Position startPosition = playerMove.start;
        Position endPosition = playerMove.end;
        boolean startOnBoard = positionIsOnBoard(startPosition);
        boolean endOnBoard = positionIsOnBoard(endPosition);
        if (!startOnBoard) {
            LOG.finer("Starting position of the move was off the board");
            return false;
        }
        if (!endOnBoard) {
            LOG.finer("Ending position of the move was off the board");
            return false;
        }
        return true;
    }

    /**
     * Checks if a given position occupies a cell on the board, see if it occupies a space on the board
     * @param position the position to be checked
     * @return true if the position represents a space on board, false otherwise
     */
    private static boolean positionIsOnBoard(Position position) {
        boolean positionOnBoard = true;
        if ( !(position.cell >= 0 && position.cell < NUM_COLS) ) {
            LOG.finer("Column position is off board");
            positionOnBoard = false;
        }
        if ( !(position.row >= 0 && position.row < NUM_ROWS) ) {
            LOG.finer("Row position is off board");
            positionOnBoard = false;
        }
        return positionOnBoard;
    }

    /**
     * Checks if the ending position is a valid position to land on
     * @param boardView The boardView for which the move is being made on
     * @param move The move being checked
     * @return True if the space is unoccupied, false if otherwise
     */
    private static boolean spaceIsAvailable(BoardView boardView, Move move) {
        Position end_position = move.end;
        Space moveSpace = boardView.getSpace(end_position);
        return moveSpace.isValid();
    }

    /**
     * Checks if a move is properly diagonal going forward for a regular piece (single step)
     * Checks if properly diagonal forward or backward for a king piece (single step)
     * ( A single diagonal move )
     * @param boardView The board view that represents the board
     * @param move The move being checked
     * @return Checks if the change in position done by the move is diagonal
     */
    private static boolean moveIsSingleProperDiagonal(BoardView boardView, Move move) {
        Position start_position = move.start;
        Position end_position = move.end;
        // Changes in x and y for the position for the move
        int col_offset = 0;
        int row_offset = 0;
        // Get the piece that is making the move
        Piece current_piece = boardView.getSpace(start_position).getPiece();
        Piece.Type piece_type = current_piece.getType();

        col_offset = (end_position.cell - start_position.cell);
        row_offset = (end_position.row - start_position.row);
        // If both the col offset and the row offset are either 1 or -1 then it is a diagonal move
        if (piece_type == Piece.Type.KING) {
            // Check for diagonal move in any direction
            return Math.abs(col_offset) == 1 && Math.abs(row_offset) == 1;
        } else {
            // Check for diagonal move forward (upwards)
            return Math.abs(col_offset) == 1 && row_offset == -1;
        }
    }

    /**
     * Checks if the move being made is going properly diagonally forward (Any amount of steps) (any piece)
     * Checks if the move being made is going properly diagonally any direction (Any amount of steps) (king)
     * Will be used to validate jump moves
     * @param move The move being made
     * @param boardView  The boardView that represents the board
     * @return true if going diagonally as
     */
    private static boolean moveIsGeneralProperDiagonal(BoardView boardView, Move move) {
        Position start_position = move.start;
        Position end_position = move.end;
        // Changes in x and y for the position for the move
        int col_offset = 0;
        int row_offset = 0;
        // Get the piece that is making the move
        Piece current_piece = boardView.getSpace(start_position).getPiece();
        Piece.Type piece_type = current_piece.getType();
        col_offset = (end_position.cell - start_position.cell);
        row_offset = (end_position.row - start_position.row);

        // Piece was not moved, not a valid move
        if (col_offset == 0 && row_offset == 0) {
            return false;
        }

        if (piece_type == Piece.Type.KING) {
            // Check for diagonal movement in any direction
            return Math.abs(col_offset) == Math.abs(row_offset);
        } else {
            // Check for diagonal movement that is forward
            return ( Math.abs(col_offset) == Math.abs(row_offset) ) && row_offset < 0;
        }

    }
}
