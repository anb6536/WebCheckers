package com.webcheckers.model;

import java.util.Stack;

import com.webcheckers.util.Pair;

public class Game {

    private static final String NOBODY_WON_MESSAGE = "No pieces can move. Nobody won.";
    private static final String NORMAL_FINISH_MESSAGE = "%s has captured all pieces";
    private static final String RESIGNED_MESSAGE = "%s has resigned.";

    public enum Mode {
        PLAY, SPECTATOR, REPLAY
    }

    public enum WhoWon {
        NOBODY, RED, WHITE
    }

    /**
     * The initiating player, who is always red
     */
    private Player redPlayer;
    /**
     * The recieving of the challenge player, who is always white
     */
    private Player whitePlayer;

    private Player currentTurn;
    private Board playBoard;
    private Stack<MoveInformation> movesMade;
    private Move moveWaitingForSubmission;
    private Player moveMaker;

    private boolean isResigned;
    private WhoWon gameWinner;
    private boolean gameAlreadyDone;

    public Board getRedBoard() {
        return playBoard;
    }

    public Board getWhiteBoard() {
        Board whiteBoard = playBoard.flipBoard();
        return whiteBoard;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public boolean playerIsInGame(Player player) {
        return player != null && (player.equals(redPlayer) || player.equals(whitePlayer));
    }

    public boolean backupMove(Player playerTrying) {
        if (playerTrying.equals(moveMaker) && moveWaitingForSubmission != null) {
            moveWaitingForSubmission = null;
            moveMaker = null;
            return true;
        }
        return false;
    }

    /**
     * check if the game has finished (if it has, say so to ourself)
     *
     * @return if the game has finished
     */
    public boolean isDone() {
        if (gameAlreadyDone)
            return true;
        boolean hasRedPieces = playBoard.hasRedPieces();
        boolean hasWhitePieces = playBoard.hasWhitePieces();
        if (hasRedPieces && !hasWhitePieces) {
            gameAlreadyDone = true;
            gameWinner = WhoWon.RED;
            return true;
        }
        if (hasWhitePieces && !hasRedPieces) {
            gameAlreadyDone = true;
            gameWinner = WhoWon.WHITE;
            return true;
        }
        if (!hasRedPieces) { // because has WhitePieces must be true at this point
            gameAlreadyDone = true;
            gameWinner = WhoWon.NOBODY;
            return true;
        }
        return false;
    }

    public void resign(Player currentPlayer) {
        gameAlreadyDone = true;
        isResigned = true;
        if (currentPlayer.equals(redPlayer)) {
            // player is red
            gameWinner = WhoWon.WHITE;
        } else {
            gameWinner = WhoWon.RED;
        }
    }

    public String getYouWon(Player currentPlayer) {
        if (gameWinner == WhoWon.NOBODY)
            return NOBODY_WON_MESSAGE;
        if (isResigned) {
            if (gameWinner == WhoWon.WHITE) {
                return String.format(RESIGNED_MESSAGE, redPlayer.getName());
            } else {
                return String.format(RESIGNED_MESSAGE, whitePlayer.getName());
            }
        } else {
            if (gameWinner == WhoWon.WHITE) {
                return String.format(NORMAL_FINISH_MESSAGE, whitePlayer.getName());
            } else {
                return String.format(NORMAL_FINISH_MESSAGE, redPlayer.getName());
            }
        }


    }

    public boolean submitMove(Player movingPlayer) {
        MoveInformation info = null;
        // set the info to the info returned
        // The pair should be set here so that we can test the boolean in the if
        // statement
        if (moveWaitingForSubmission == null) {
            return false;
        }
        Pair<Boolean, MoveInformation> validationInfo = playBoard.validateMove(moveWaitingForSubmission,
                whitePlayer == movingPlayer);
        info = validationInfo.getValue();
        if (validationInfo.getKey()) {
            // INFO GETS SET TO THE RETURN VALUE IN HERE
            // CHANGE THE TEST TO TEST THE BOOLEAN PORTION OF THE PAIR
            movesMade.push(info);
        } else {
            return false;
        }
        if (playBoard.makeMove(moveWaitingForSubmission, whitePlayer.equals(movingPlayer))) {
            moveWaitingForSubmission = null;
            moveMaker = null;
            // then it should not swap the player turn
            boolean more_jump_moves = MoveValidator.hasPossibleSingleJumpMoves(playBoard.getBoardView(),
                    info.getMove().end);
            if (!info.isJumpMove() || !more_jump_moves) {
                swapPlayerTurn();
            }
            return true;
        }
        return false;
    }

    public boolean validateMove(Move move, Player movingPlayer) {
        if (move == null) {
            return false;
        }
        if (!movingPlayer.equals(currentTurn)) {
            return false;
        }
        if (playBoard.validateMove(move, whitePlayer.equals(movingPlayer)).getKey()) {
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
        if (currentTurn == redPlayer) {
            currentTurn = whitePlayer;
        } else {
            currentTurn = redPlayer;
        }
    }

    /**
     * Gets the color of the player currently playing
     *
     * @return RED if player1 (the red player), WHITE if player2 (the white player)
     */
    public Piece.Color getCurrentPlayingColor() {
        // Player1 is RED player
        if (currentTurn == redPlayer) {
            System.out.println("Red player playing");
            return Piece.Color.RED;
        } else {
            System.out.println("White player playing");
            return Piece.Color.WHITE;
        }
    }

    public Game(Player redPlayer, Player whitePlayer, Board board) {
        this.redPlayer = redPlayer;
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.currentTurn = redPlayer;
        this.playBoard = board;
        this.movesMade = new Stack<MoveInformation>();
        this.gameWinner = WhoWon.NOBODY;
        this.gameAlreadyDone = false;
        this.isResigned = false;
    }

}
