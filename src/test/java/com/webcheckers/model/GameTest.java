package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.webcheckers.model.Game.WhoWon.NOBODY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;
    private Player player1;
    private Player player2;


    @BeforeEach
    void setupGame() {
        player1 = new Player("player1");
        player2 = new Player("player2");
        Board board = Board.makeBoard();
        board.addPieces();
        game = new Game(player1, player2, board);
    }

    @Test
    void badMoveSubmit() {
        assertFalse(game.submitMove(player1));
        assertFalse(game.submitMove(player2));
        Position start = new Position();
        Position end = new Position();


        // try to move on top of yourself
        start.cell = 1;
        start.row = 1;
        end.cell = 1;
        end.row = 1;
        assertFalse(game.validateMove(Move.createMove(start, end), player1));
        assertFalse(game.validateMove(Move.createMove(start, end), player2));

        // try to move on top of another piece
        start.cell = 0;
        start.row = 7;
        end.cell = 1;
        start.row = 6;
        assertFalse(game.validateMove(Move.createMove(start, end), player1));
        assertFalse(game.validateMove(Move.createMove(start, end), player2));

    }

    @Test
    void goodMoveSubmit() {
        Position start = new Position();
        Position end = new Position();

        // try to move on top of another piece
        start.cell = 0;
        start.row = 5;
        end.cell = 1;
        end.row = 4;
        assertTrue(game.isPlayerTurn(player1));
        assertFalse(game.isPlayerTurn(player2));
        assertEquals(game.getCurrentPlayingColor(), Piece.Color.RED);
        assertTrue(game.validateMove(Move.createMove(start, end), player1));
        assertTrue(game.submitMove(player1));
        // turn is flipped for white
        assertTrue(game.isPlayerTurn(player2));
        assertFalse(game.isPlayerTurn(player1));
        assertEquals(game.getCurrentPlayingColor(), Piece.Color.WHITE);
        assertTrue(game.validateMove(Move.createMove(start, end), player2));
        assertTrue(game.submitMove(player2));
        assertEquals(game.getCurrentPlayingColor(), Piece.Color.RED);

        assertTrue(game.isPlayerTurn(player1));
        assertFalse(game.isPlayerTurn(player2));
        assertEquals(game.getCurrentPlayingColor(), Piece.Color.RED);
        game.swapPlayerTurn();
        assertTrue(game.isPlayerTurn(player2));
        assertFalse(game.isPlayerTurn(player1));
        assertEquals(game.getCurrentPlayingColor(), Piece.Color.WHITE);
    }

    @Test
    void getYouWon() {
        assertEquals(" nobody won.", game.getYouWon(player1));
        assertEquals(" nobody won.", game.getYouWon(player2));
        assertFalse(game.isDone());
        game.resign(player1);
        assertTrue(game.isDone());
        assertEquals(" you won!", game.getYouWon(player2));
        assertEquals(" you lost..", game.getYouWon(player1));
    }

    @Test
    void isInGame() {
        assertTrue(game.playerIsInGame(player1));
        assertTrue(game.playerIsInGame(player2));
        assertFalse(game.playerIsInGame(null));
        assertFalse(game.playerIsInGame(new Player("player3")));
    }

    @Test
    void emptyBoard() {
        Board board = Board.makeBoard();
        BoardView boardView = board.getBoardView();
        game = new Game(player1, player2, board);
        assertTrue(game.isDone());
        assertEquals(" nobody won.", game.getYouWon(player1));

        game = new Game(player1, player2, board);
        boardView.getSpace(0, 0).setPiece(Piece.Color.RED);
        assertTrue(game.isDone());
        assertEquals(" you won!", game.getYouWon(player1));

        game = new Game(player1, player2, board);
        boardView.getSpace(0, 0).setPiece(Piece.Color.WHITE);
        assertTrue(game.isDone());
        assertEquals(" you lost..", game.getYouWon(player1));
    }
}
