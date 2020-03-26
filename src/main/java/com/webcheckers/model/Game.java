package com.webcheckers.model;

import java.util.Map;

public class Game {
    public enum Mode {
        PLAY, SPECTATOR, REPLAY
    }

    /**
     * The initiating player, who is always red
     */
    private Player player1;
    /**
     * The recieving of the challenge player, who is always white
     */
    private Player player2;
    private Player currentTurn;
    private Board playBoard;

    public Player getRedPlayer() {
        return player1;
    }

    public Player getWhitePlayer() {
        return player2;
    }

    public boolean submitMove(Move move) {
        return playBoard.makeMove(move);
    }

    public boolean validateMove(Move move) {
        return playBoard.validateMove(move);
    }

    public boolean isPlayerTurn(Player player) {
        return currentTurn == player;
    }

    public Player getCurrentPlayerTurn() {
        return currentTurn;
    }

    public void swapPlayerTurn() {
        if (currentTurn == player1) {
            currentTurn = player2;
        } else {
            currentTurn = player1;
        }
    }

    public Game(Player player1, Player player2, Board board) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentTurn = player1;
        this.playBoard = board;
    }

}
