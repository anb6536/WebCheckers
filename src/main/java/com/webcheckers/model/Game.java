package com.webcheckers.model;


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
    private Move moveWaitingForSubmission;
    private Player moveMaker;

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

    public boolean backupMove(Player playerTrying) {
        if (playerTrying.equals(moveMaker) && moveWaitingForSubmission != null) {
            moveWaitingForSubmission = null;
            moveMaker = null;
            return true;
        }
        return false;
    }

    public boolean submitMove(Player movingPlayer) {
        MoveInformation info = null;
        // set the info to the info returned
        // The pair should be set here so that we can test the boolean in the if
        // statement

        Pair<Boolean, MoveInformation> validationInfo = playBoard.validateMove(moveWaitingForSubmission, player2 == movingPlayer);
        info = validationInfo.getValue();
        if (validationInfo.getKey()) {
            // INFO GETS SET TO THE RETURN VALUE IN HERE
            // CHANGE THE TEST TO TEST THE BOOLEAN PORTION OF THE PAIR
            movesMade.push(info);
        } else {
            return false;
        }
        if (playBoard.makeMove(moveWaitingForSubmission, player2 == movingPlayer)) {
            // TODO: Actually remove the pieces or have the board remove them
            moveWaitingForSubmission = null;
            moveMaker = null;
            // TODO: If the piece moved made a jump move, and it has jump moves remaining,
            // then it should not swap the player turn
            boolean more_jump_moves = MoveValidator.hasPossibleSingleJumpMoves(playBoard.getBoardView(), info.getMove().end);
            if (!info.isJumpMove() || !more_jump_moves) {
                swapPlayerTurn();
            }
            return true;
        }
        return false;
    }

    public boolean validateMove(Move move, Player movingPlayer) {
        if (!movingPlayer.equals(currentTurn)) {
            return false;
        }
        if (playBoard.validateMove(move, player2 == movingPlayer).getKey()) {
            moveWaitingForSubmission = move;
            moveMaker = movingPlayer;
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

    /**
     * Gets the color of the player currently playing
     * 
     * @return RED if player1 (the red player), WHITE if player2 (the white player)
     */
    public Piece.Color getCurrentPlayingColor() {
        // Player1 is RED player
        if (currentTurn == player1) {
            System.out.println("Red player playing");
            return Piece.Color.RED;
        } else {
            System.out.println("White player playing");
            return Piece.Color.WHITE;
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
