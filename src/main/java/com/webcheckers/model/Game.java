package com.webcheckers.model;

import java.util.Map;

public class Game {
    public enum Mode {
        PLAY, SPECTATOR, REPLAY
    }

    private Player player1;
    private Player player2;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }
}
