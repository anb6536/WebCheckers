package com.webcheckers.model;



import java.util.ArrayList;
import java.util.logging.Logger;

public class MoveValidator {
    private static final Logger LOG = Logger.getLogger(MoveValidator.class.getName());
    public final static int NUM_ROWS = 8;
    public final static int NUM_COLS = 8;
    // Variable to enforce a jump
    public final static boolean FORCE_JUMP = true;
    // Move information that can be accessed upon validation (initialized as empty)
    // This gets updated to the most recent moves that involves the capturing of a piece
    private static ArrayList<MoveInformation> captureMoves = new ArrayList<>();
    public static Pair<Boolean, MoveInformation> validateMove(Move playerMove, BoardView boardView, boolean whiteMoving) {
        // Make it empty every time we validate a move
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
        /* Pair to represent invalidMove */
        Pair<Boolean, MoveInformation> failPair = new Pair<>(false, null);
        // If the move is not even on the board, return false immediately
        if (!move_on_board) return failPair;

        MoveInformation simpleMoveInfo = null;
        // Represents single move without jump (Open space and a diagonal movement)
        if (isSimpleMove(boardView, playerMove, whiteMoving)) {
            // Force a jump if the FORCE Flag is on and cause this to be invalid
            if (FORCE_JUMP && anyJumpMoves(boardView, whiteMoving)) {
                return failPair;
            }


            // Move info with nothing removed
            simpleMoveInfo = new MoveInformation(playerMove);
            return new Pair<>(true, simpleMoveInfo);
        }
        Pair<Boolean, MoveInformation> singleJumpPair = isSingularJumpMove(boardView, playerMove, whiteMoving);
        if (singleJumpPair.getKey()) {
            return singleJumpPair ;
        }

        return failPair;
    }

    /**
     * Helper function to determine if a board is a simple move
     * @param boardView The view of the board
     * @param move The move being checked
     * @param whiteMove Whether or not the white player is move
     * @return True if the move counts as a simple move, false otherwise
     */
    public static boolean isSimpleMove(BoardView boardView, Move move, boolean whiteMove) {
        boolean move_on_board = moveIsOnBoard(move);
        boolean landing_space_available = spaceIsAvailable(boardView, move);
        boolean is_single_proper_diagonal = moveIsSingleProperDiagonal(boardView, move, whiteMove);
        return (move_on_board && landing_space_available && is_single_proper_diagonal);
    }


    /**
     * Check if the start and end positions of a Move is on the board
     *
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
     *
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
     *
     * @param boardView The boardView for which the move is being made on
     * @param move      The move being checked
     * @return True if the space is unoccupied, false if otherwise
     */
    private static boolean spaceIsAvailable(BoardView boardView, Move move) {
        Position end_position = move.end;
        if (!moveIsOnBoard(move)) return false;
        Space moveSpace = boardView.getSpace(end_position);
        return moveSpace.isValid();
    }

    /**
     * Checks if a move is properly diagonal going forward for a regular piece (single step)
     * Checks if properly diagonal forward or backward for a king piece (single step)
     * ( A single diagonal move )
     *
     * @param boardView The board view that represents the board
     * @param move      The move being checked
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
     *
     * @param move      The move being made
     * @param boardView The boardView that represents the board
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
        if (current_piece == null) {
            LOG.severe("Player illegally tried to move twice in once turn");
            return false;
        }
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
     *
     * @return
     */
    public static Pair<Boolean, MoveInformation> isSingularJumpMove(BoardView boardView, Move move, boolean whiteMove) {
        // Impose restrictions on motion (handles difference between King and Single)
        boolean right_motion = moveIsGeneralProperDiagonal(boardView, move, whiteMove);
        // The vertical and horizontal distance are valid
        boolean right_distance;
        boolean onBoard = moveIsOnBoard(move);
        boolean landing_space_available = spaceIsAvailable(boardView, move);

        Pair<Boolean, MoveInformation> failPair = new Pair<>(false, null);
        // Move to be returned along with a boolean
        MoveInformation captureMove = null;
        // If it is not moving in the right direction automatically return false
        if (!right_motion)  return failPair;
        if (!onBoard) return failPair;
        if (!landing_space_available) return failPair;

        // Get change in row and column
        int row_offset = move.end.row - move.start.row;
        int col_offset = move.end.cell - move.start.cell;
        right_distance = (Math.abs(row_offset) == 2 && Math.abs(col_offset) == 2);
        // If you moved more or less than 2 spaces then it cannot be a valid jump
        if (!right_distance) return failPair;
        // Get the piece to see if King or Single
        Piece current_piece = boardView.getSpace(move.start).getPiece();
        Position midpoint = move.getMidpoint();
        // If there is no piece at the midpoint there is nothing
        Piece middle_piece = boardView.getSpace(midpoint).getPiece();
        if (middle_piece == null) return failPair;
        // Confirming that a piece jumps over opponent piece
        if (whiteMove) {
             if (middle_piece.getColor() == Piece.Color.RED) {
                 // Position that will be removed
                 captureMove = new MoveInformation(move, middle_piece, midpoint);
                 return new Pair<>(true, captureMove);
             } else {
                 return failPair;
             }
        } else {
            if (middle_piece.getColor() == Piece.Color.WHITE) {
                // Position that will be removed
                captureMove = new MoveInformation(move, middle_piece, midpoint);
                return new Pair<>(true, captureMove);
            } else {
                return failPair;
            }
        }
    }

    /**
     * Get the capture moves made by the most recent move
     * @return the most recent moves made by capture move
     */
    public static ArrayList<MoveInformation> getCaptureMoves() {
        return captureMoves;
    }

    // Not used
    public static boolean isMultipleJumpMove(BoardView boardView, Move move, boolean whiteMove) {
        // Impose restrictions on motion (handles difference between King and Single)
        boolean right_motion = moveIsGeneralProperDiagonal(boardView, move, whiteMove);
        ArrayList<MoveInformation> captureMovesMade = new ArrayList<>();
        // The vertical and horizontal distance are valid
        boolean right_distance;
        boolean onBoard = moveIsOnBoard(move);
        boolean landing_space_available = spaceIsAvailable(boardView, move);
        // If it is not moving in the right direction automatically return false
        if (!right_motion)  return false;
        if (!onBoard) return false;
        if (!landing_space_available) return false;
        int row_offset = move.end.row - move.start.row;
        int col_offset = move.end.cell - move.start.cell;

        // Make sure the total distance is greater than 2
        // If it was equal to two, it would just be a single jump move
        // If it was less than two, it would be a simple move
        right_distance = (Math.abs(row_offset ) > 2 && Math.abs(col_offset) > 2);
        if (!right_distance) return false;

        // Quick sgn(row_offset) and sgn(col_offset) for iterating
        // Kinda like unit vectors pointing from move.start to move.end
        int row_inc = row_offset/Math.abs(row_offset);
        int col_inc = col_offset/Math.abs(col_offset);

        // Iterate along the "vectors" looking for a repeating pattern of [enemy_piece, blank_space]
        int cur_row = move.start.row + row_inc; // Start on the first space along that path
        int cur_col = move.start.cell + col_inc; // Start on the first space along that path
        Piece.Color colorToCapture = Piece.Color.WHITE;
        if (whiteMove) colorToCapture = Piece.Color.RED;
        // Index to cycle for the pattern
        int cycleIndex = 0;
        // Declare array with move information to capt
        ArrayList<MoveInformation> mjCaptureMoves = new ArrayList<>();
        while (cur_row != move.end.row && cur_col != move.end.cell) {
            Piece cur_piece = boardView.getSpace(cur_row, cur_col).getPiece();

            // Keep iterating until we reach the end
            // Break if conditions violated
            if (cycleIndex == 0) {
                if (cur_piece == null) return false;
                if (cur_piece.getColor() != colorToCapture) return false;
                MoveInformation moveInformation = new MoveInformation(move, cur_piece, Position.makePosition(cur_row, cur_col));
                mjCaptureMoves.add(moveInformation);
            } else if (cycleIndex == 1) {
                // After hopping over an enemy piece there should be a space to land on
                if (cur_piece != null) return false;
            }

            cur_row += row_inc;
            cur_col += col_inc;
            cycleIndex = (cycleIndex + 1) % 2;
        }
        // Return true if we reach the end
        // Return the MoveInformation with all the removed pieces with in the move
        captureMoves = new ArrayList<>(mjCaptureMoves);
        return true;
    }

    /**
     * Check for if there are any simple moves in the game
     * @param boardView The boardview that represents the board
     * @param current_position The current position of the board
     * @return Return true if there are available moves, false if there are none
     */
    public boolean hasPossibleSimpleMoves(BoardView boardView, Position current_position) {
        Piece current_piece = boardView.getSpace(current_position).getPiece();
        // Don't waste time looking for moves if there is no piece here and make it known
        if (current_piece == null) {
            LOG.severe("Checked an empty space for moves");
            return false;
        }

        // See if this is a white move
        boolean whiteMove = (current_piece.getColor() == Piece.Color.WHITE);
        // Search for a maximum of one space around any move
        for (int row_offset = -1; row_offset <= 1; row_offset++) {
            for (int col_offset = -1; col_offset <= 1; col_offset++) {
                // Create moves and validate if they are single jump moves
                Position artificial_ending_position = Position.makePosition(current_position.row, current_position.cell);
                Move move_to_check = Move.createMove(current_position, artificial_ending_position);
                if (!moveIsOnBoard(move_to_check)) { continue; }
                boolean is_valid_simple_move = isSimpleMove(boardView, move_to_check, whiteMove);

                if (is_valid_simple_move) return true;
            }
        }
        return false;
    }

    /**
     * Sees if there are any single jump moves for a given piece
     * @param boardView The boardview representing the board
     * @param current_position The current position (row and cell)
     * @return True, if there is an available single jump move for the piece at this position
     */
    public static boolean hasPossibleSingleJumpMoves(BoardView boardView, Position current_position) {
        Piece current_piece = boardView.getSpace(current_position).getPiece();
        // Don't waste time looking for moves if there is no piece here and make it known
        if (current_piece == null) {
            LOG.severe("Checked an empty space for moves");
            return false;
        }
        boolean whiteMove = (current_piece.getColor() == Piece.Color.WHITE);
        // Search for a maximum of one space around any move
        for (int row_offset = -2; row_offset <= 2; row_offset++) {
            for (int col_offset = -2; col_offset <= 2; col_offset++) {
                // Create moves and validate if they are single jump moves
                Position artificial_ending_position = Position.makePosition(current_position.row + row_offset, current_position.cell + col_offset);
                Move move_to_check = Move.createMove(current_position, artificial_ending_position);
                Pair<Boolean, MoveInformation> jumpPair = isSingularJumpMove(boardView, move_to_check, whiteMove);
                // If you have a jump move return true
                if (jumpPair.getKey()) {
                    LOG.severe(" CURRENT PIECE HAS JUMP MOVE");
                    return true;
                }
            }
        }
        // Return false if we fail to inside
        return false;
    }

    /**
     * Checks if there are any jump moves for a given color
     * @param boardView The board view representing the board
     * @param whiteMove Whether or not if we are checking the white pieces
     * @return True if there is a jump move for a piece of that color
     */
    public static boolean anyJumpMoves(BoardView boardView, boolean whiteMove) {
        Piece.Color color_to_check = (whiteMove) ? Piece.Color.WHITE : Piece.Color.RED;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Position current_position = Position.makePosition(row, col);
                Piece current_piece = boardView.getSpace(current_position).getPiece();
                // If the current piece is null don't even bother checking and move on to next piece
                if (current_piece == null) {
                    continue;
                }
                // If not the color we're interested for then skip that piece
                if (current_piece.getColor() != color_to_check) {
                    continue;
                }
                boolean piece_has_jump_moves = hasPossibleSingleJumpMoves(boardView, current_position);
                if (piece_has_jump_moves) return true;
            }
        }
        return false;
    }
}
