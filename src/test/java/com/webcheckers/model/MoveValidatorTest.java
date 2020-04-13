package com.webcheckers.model;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.webcheckers.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveValidatorTest {
    // Starting a game with presumably friendly classes
    Board basicBoard = Board.makeBoard();
    BoardView basicBoardView = basicBoard.getBoardView();

    // Basic pair for failure to find a move
    final Pair<Boolean, MoveInformation> failPair = new Pair<>(false, null);


    @BeforeEach
    void addBoardPieces() {
        basicBoard.addPieces();
    }
    /**
     * Tests a red moving a piece directly to it's right
     */
    @Test
    public void testValidateSimpleMove_WrongHorizontalMove() {
        Position startPosition = Position.makePosition(5,0);
        Position endPosition = Position.makePosition(5,1);
        Move move = Move.createMove(startPosition, endPosition);
        Pair<Boolean, MoveInformation> resultPair = MoveValidator.validateMove(move, basicBoardView, false);
        assertFalse(resultPair.getKey());
        assertNull(resultPair.getValue());
    }

    @Test
    public void testValidateSimpleMove_WrongTooFarDiagonal() {
        Position startPosition = Position.makePosition(5,0);
        Position endPosition = Position.makePosition(3,2);
        Move move = Move.createMove(startPosition, endPosition);
        Pair<Boolean, MoveInformation> resultPair = MoveValidator.validateMove(move, basicBoardView, false);
        assertFalse(resultPair.getKey());
        assertNull(resultPair.getValue());
    }

    @Test
   public void testValidateSimpleMove_WrongBackwardsOccupied() {
       Position startPosition = Position.makePosition(5,0);
       Position endPosition = Position.makePosition(6,1);
       Move move = Move.createMove(startPosition, endPosition);
       Pair<Boolean, MoveInformation> resultPair = MoveValidator.validateMove(move, basicBoardView, false);
       assertFalse(resultPair.getKey());
       assertNull(resultPair.getValue());
   }
    @Test
   public void testValidateSimpleMove_WhiteCorrectMove() {
       Position startPosition = Position.makePosition(5, 0);
       Position endPosition = Position.makePosition(4,1);
       Move move = Move.createMove(startPosition, endPosition);
       Pair<Boolean, MoveInformation> resultPair = MoveValidator.validateMove(move, basicBoardView, true);
       assertTrue(resultPair.getKey());
       assertNull(resultPair.getValue().removedPosition);
       // Test for equality with the inverted move
       assertEquals(resultPair.getValue().move, move.invertMove());
   }

    @Test
    public void testValidateSimpleMove_WhiteWrongHorizontal() {
        Position startPosition = Position.makePosition(5, 0);
        Position endPosition = Position.makePosition(5,1);
        Move move = Move.createMove(startPosition, endPosition);
        Pair<Boolean, MoveInformation> resultPair = MoveValidator.validateMove(move, basicBoardView, true);
        assertFalse(resultPair.getKey());
        assertNull(resultPair.getValue());
    }

    @Test
    public void testValidateSimpleMove_CorrectSingleDiagonal() {
        Position startPosition = Position.makePosition(5, 0);
        Position endPosition = Position.makePosition(4,1);
        Move move = Move.createMove(startPosition, endPosition);
        Pair<Boolean, MoveInformation> resultPair = MoveValidator.validateMove(move, basicBoardView, false);
        // Assert that this move is correct
        assertTrue(resultPair.getKey());
        // Assert no piece is removed by this move
        assertNull(resultPair.getValue().removedPosition);
        // Assert that the move stored is the correct move
        assertEquals(resultPair.getValue().move, move);
    }

    @Test
    void testMoveOutOfBounds() {
        Position startPosition = Position.makePosition(5, 0);
        Position endPosition = Position.makePosition(100, -1);
        Move outOfBoundsMove = Move.createMove(startPosition, endPosition);
        assertFalse(MoveValidator.moveIsOnBoard(outOfBoundsMove));
        Pair<Boolean, MoveInformation> resultPair = MoveValidator.validateMove(outOfBoundsMove, basicBoardView, false);
        assertFalse(resultPair.getKey());
        assertNull(resultPair.getValue());
    }

    @Test
    void testJumpMove() {
        // Artificially create scenario such that jump move exists
        Position startRedPos1 = Position.makePosition(5,2);
        Position endPosition = Position.makePosition(4,3);
        Move move1 = Move.createMove(startRedPos1, endPosition);
        basicBoard.makeMove(move1, false);
        basicBoard.makeMove(move1, true);

        // Test the acutal jump move being made
        Position startRed = Position.makePosition(4,3);
        Position endRed = Position.makePosition(2,5);
        Move move2 = Move.createMove(startRed, endRed);
        Pair<Boolean, MoveInformation> resultPair = MoveValidator.validateMove(move2, basicBoardView, false);
        assertTrue(resultPair.getKey());
        assertEquals(resultPair.getValue().move, move2);
        assertEquals(resultPair.getValue().removedPosition, Position.makePosition(3,4));
        assertEquals(resultPair.getValue().removed.getColor(), Piece.Color.WHITE);
    }

    @Test
    void testForceJumpMove() {
        // Artificially create scenario such that jump move exists
        Position startRedPos1 = Position.makePosition(5,2);
        Position endPosition = Position.makePosition(4,3);
        Move move1 = Move.createMove(startRedPos1, endPosition);
        basicBoard.makeMove(move1, false);
        basicBoard.makeMove(move1, true);

        Position startBlocked = Position.makePosition(6,1);
        Position endBlocked = Position.makePosition(5,2);
        Move blockedMove = Move.createMove(startBlocked, endBlocked);

        // Valid diagonal blocked because jump move exists elsewhere
        Pair<Boolean, MoveInformation> resultPair = MoveValidator.validateMove(blockedMove, basicBoardView, false);
        assertFalse(resultPair.getKey());
        assertNull(resultPair.getValue());
    }

    @Test
    void testJumpMove_WhiteMove() {
        // Artificially create scenario such that jump move exists
        Position startRedPos1 = Position.makePosition(5,2);
        Position endPosition = Position.makePosition(4,3);
        Move move1 = Move.createMove(startRedPos1, endPosition);
        basicBoard.makeMove(move1, false);
        basicBoard.makeMove(move1, true);

        // Test the actual jump move being made
        Position startRed = Position.makePosition(4,3);
        Position endRed = Position.makePosition(2,5);
        Move move2 = Move.createMove(startRed, endRed);
        Pair<Boolean, MoveInformation> resultPair = MoveValidator.validateMove(move2, basicBoardView, true);
        assertTrue(resultPair.getKey());
        assertEquals(resultPair.getValue().move, move2.invertMove());
        assertEquals(resultPair.getValue().removedPosition, Position.makePosition(3,4).invertPosition());
        assertEquals(resultPair.getValue().removed.getColor(), Piece.Color.RED);
    }

}

