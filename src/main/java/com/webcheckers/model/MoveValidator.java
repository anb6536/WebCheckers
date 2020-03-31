package com.webcheckers.model;

import java.util.logging.Logger;
import java.util.logging.LogManager;

public class MoveValidator {
    private static final Logger LOG = Logger.getLogger(MoveValidator.class.getName());
    public final static int NUM_ROWS = 8;
    public final static int NUM_COLS = 8;

    public static boolean validateMove(Move playerMove, BoardView boardView, boolean whiteMoving) {
        if (whiteMoving) {
            LOG.severe("White moving");
            playerMove = playerMove.invertMove();
        } else {
            LOG.severe("Red moving");
        }
        LOG.severe("\n"+ boardView.toString());
        boolean move_on_board = moveIsOnBoard(playerMove);
        boolean landing_space_available = spaceIsAvailable(boardView, playerMove);
        boolean is_single_proper_diagonal = moveIsSingleProperDiagonal(boardView, playerMove, whiteMoving);
        // If the move is not even on the board, return false immediately
        if (!move_on_board) return false;

        // Represents single move without jump (Open space and a diagonal movement)
        if (landing_space_available && is_single_proper_diagonal) {
            return true;
        } else return isSingularJumpMove(boardView, playerMove, whiteMoving);

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
    private static boolean moveIsSingleProperDiagonal(BoardView boardView, Move move, boolean whiteMove) {
        Position start_position = move.start;
        Position end_position = move.end;
        // Default vertical direction
        int vert_direction = -1;
        if (whiteMove) {
            // Allow white pieces to move down in the board
            vert_direction = 1;
        }
        // Changes in x and y for the position for the move
        int col_offset = 0;
        int row_offset = 0;
        // Get the piece that is making the move
        Piece current_piece = boardView.getSpace(start_position).getPiece();
        if (current_piece == null) {
            LOG.severe("Null piece was somehow moved from:" + move.start + " to " + move.end);
            return false;
        }
        Piece.Type piece_type = current_piece.getType();

        col_offset = (end_position.cell - start_position.cell);
        row_offset = (end_position.row - start_position.row);
        // If both the col offset and the row offset are either 1 or -1 then it is a diagonal move
        if (piece_type == Piece.Type.KING) {
            // Check for diagonal move in any direction
            return Math.abs(col_offset) == 1 && Math.abs(row_offset) == 1;
        } else {
            // Check for diagonal move forward (upwards)
            return Math.abs(col_offset) == 1 && row_offset == vert_direction;
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
    private static boolean moveIsGeneralProperDiagonal(BoardView boardView, Move move, boolean whiteMove) {
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
        boolean right_direction;
        if (whiteMove) {
            right_direction = row_offset > 0;
        } else {
            right_direction = row_offset < 0;
        }


        // Piece was not moved, not a valid move
        if (col_offset == 0 && row_offset == 0) {
            return false;
        }

        if (piece_type == Piece.Type.KING) {
            // Check for diagonal movement in any direction
            return Math.abs(col_offset) == Math.abs(row_offset);
        } else {
            // Check for diagonal movement that is forward
            return ( Math.abs(col_offset) == Math.abs(row_offset) ) && right_direction;
        }
    }

    /**
     * Detects if a move represents a singular jump move
     * @param boardView The boardView that represents the board
     * @param move The move that can be made
     * @param whiteMove
     * @return
     */
    public static boolean isSingularJumpMove(BoardView boardView, Move move, boolean whiteMove) {
        // Impose restrictions on motion (handles difference between King and Single)
        boolean right_motion = moveIsGeneralProperDiagonal(boardView, move, whiteMove);

        // The vertical and horizontal distance are valid
        boolean right_distance;
        boolean onBoard = moveIsOnBoard(move);
        boolean landing_space_available = spaceIsAvailable(boardView, move);
        // If it is not moving in the right direction automatically return false
        if (!right_motion)  return false;
        if (!onBoard) return false;
        if (!landing_space_available) return false;

        // Get change in row and column
        int row_offset = move.end.row - move.start.row;
        int col_offset = move.end.cell - move.start.cell;
        right_distance = (Math.abs(row_offset) == 2 && Math.abs(col_offset) == 2);
        // If you moved more or less than 2 spaces then it cannot be a valid jump
        if (!right_distance) return false;
        // Get the piece to see if King or Single
        Piece current_piece = boardView.getSpace(move.start).getPiece();
        Position position_remove = null;
        Position midpoint = move.getMidpoint();
        // If there is no piece at the midpoint there is nothing
        Piece middle_piece = boardView.getSpace(midpoint).getPiece();
        if (middle_piece == null) return false;

        // Confirming that a piece jumps over opponent piece
        if (whiteMove) {
             if (middle_piece.getColor() == Piece.Color.RED) {
                 position_remove = midpoint;
                 return true;
             } else {
                 return false;
             }
        } else {
            if (middle_piece.getColor() == Piece.Color.WHITE) {
                position_remove = midpoint;
                return true;
            } else {
                return false;
            }
        }
    }


}
