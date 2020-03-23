package com.webcheckers.model;

/**
 * a player object that has a name and whether they are in game
 */
public class Player {
    private final String name;
    private boolean inGame;

    /**
     * create a player with this unique username
     * 
     * @param name the username
     */
    public Player(String name) {
        this.name = name;
        this.inGame = false;
    }

    /**
     * simple getter
     * 
     * @return the username of the player
     */
    public String getName() {
        return name;
    }

    /**
     * set inGame to true
     */
    public void joinedGame() {
        this.inGame = true;
    }

    /**
     * set inGame to false
     */
    public void leftGame() {
        this.inGame = false;
    }

    /**
     * get whether the player is in a game
     * 
     * @return true if the player is in a game, otherwise false
     */
    public boolean isInGame() {
        return this.inGame;
    }

    /**
     * see if two players are the same player
     * 
     * @param player the player being compared against
     * @return true if the unique usernames are the same
     */
    @Override
    public boolean equals(Object player) {
        if (player instanceof Player) {
            Player nPlayer = (Player) player;
            return this.getName().equals(nPlayer.getName());
        } else {
            return false;
        }
    }
}