package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.MalformedParameterizedTypeException;

public class MoveInformationTest {

    @Test
    void getMoveTest(){
        Position start = Position.makePosition(5,0);
        Position end = Position.makePosition(4,1);
        Move move = Move.createMove(start, end);

        MoveInformation info = new MoveInformation(move);

        Assertions.assertEquals(move, info.getMove());
    }

    @Test
    void getRemovedPieceTest(){
        Piece piece = new Piece(Piece.Color.WHITE);
        Position start = Position.makePosition(5,0);
        Position end = Position.makePosition(3, 2);
        Position removed = Position.makePosition(4,1);
        Move move = Move.createMove(start, end);
        MoveInformation info = new MoveInformation(move, piece, removed);

        Assertions.assertEquals(piece, info.getRemovedPiece());
    }

    @Test
    void getRemovedPositionTest(){
        Piece piece = new Piece(Piece.Color.WHITE);
        Position start = Position.makePosition(5,0);
        Position end = Position.makePosition(3, 2);
        Position removed = Position.makePosition(4,1);
        Move move = Move.createMove(start, end);
        MoveInformation info = new MoveInformation(move, piece, removed);

        Assertions.assertEquals(removed, info.getRemovedPosition());
    }

    @Test
    void isJumpMoveTest(){
        Piece piece = new Piece(Piece.Color.WHITE);
        Position start = Position.makePosition(5,0);
        Position end = Position.makePosition(3, 2);
        Position removed = Position.makePosition(4,1);
        Move move = Move.createMove(start, end);

        MoveInformation info1 = new MoveInformation(move);
        MoveInformation info2 = new MoveInformation(move, piece, removed);

        Assertions.assertTrue(info2.isJumpMove());
        Assertions.assertFalse(info1.isJumpMove());

    }
}
