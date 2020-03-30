package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Stack;

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
    private Stack<MoveInformation> movesMade;
    public Move moveWaitingForSubmission;

    public Board getRedBoard() {
        return playBoard;
    }

    public Board getWhiteBoard() {
        Board whiteBoard = playBoard.flipBoard();
        return whiteBoard;
    }

    public Player getRedPlayer() {
        return player1;
    }

    public Player getWhitePlayer() {
        return player2;
    }

    public boolean backupMove(Player backingUp) {
        if (movesMade.peek() != null) {
            MoveInformation moveInfo = movesMade.peek();
            if (playBoard.backupMove(moveInfo)) {
                movesMade.pop();
                return true;
            }
        }
        return false;
    }

    public boolean submitMove(Player movingPlayer) {
        Space removedPieceSpace = null;
        if (playBoard.validateMove(moveWaitingForSubmission, player2 == movingPlayer)) {
            // TODO: Get the pieces removed via a Board function
        }
        if (playBoard.makeMove(moveWaitingForSubmission, player2 == movingPlayer)) {
            // TODO: Actually remove the pieces or have the board remove them
            moveWaitingForSubmission = null;
            swapPlayerTurn();
            return true;
        }
        return false;
    }

    public boolean validateMove(Move move, Player movingPlayer) {
        if (playBoard.validateMove(move, player2 == movingPlayer)) {
            moveWaitingForSubmission = move;
            return true;
        }
        return false;
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
        this.movesMade = new Stack<MoveInformation>();
    }

}
