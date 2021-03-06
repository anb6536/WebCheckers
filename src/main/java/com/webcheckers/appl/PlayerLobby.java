package com.webcheckers.appl;

import java.util.*;

import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

/**
 * all the players logged in
 */
public class PlayerLobby {
    private List<Player> playersLoggedIn;
    // Synchronization objects to separately be able to modify aspects of the
    // PlayerLobby so more than one action happens at a time, this prevents any data
    // races that may happen happen.
    private Object playerSyncObject = new Object();
    private Object opponentSyncObject = new Object();
    // a one to one map of players
    private Map<Player, Player> opponents;
    private Map<Integer, Player> gameIds;
    private Map<String, Game> allGames;

    // what the next game id should be
    private int id;

    /**
     * create a player lobby with default values
     */
    public PlayerLobby() {
        playersLoggedIn = new ArrayList<>();
        this.opponents = new HashMap<>();
        this.id = 1;
        this.gameIds = new HashMap<>();
        this.allGames = new HashMap<>();
    }

    /**
     * register a game with 2 players
     *
     * @param player1 the first player
     * @param player2 the second player
     */
    public synchronized void addMatch(Player player1, Player player2, Board board) {
        // register that these players are opponents
        synchronized (opponentSyncObject) {
            opponents.put(player1, player2);
            gameIds.put(this.id, player1);
            Game game = new Game(player1, player2, board);
            allGames.put(String.valueOf(this.id), game);
            this.id++;
            player1.joinedGame();
            player2.joinedGame();
        }
    }

    /**
     * register a game with 2 players
     *
     * @param player1 the first player
     * @param player2 the second player
     */
    public synchronized void removeMatch(Player player1, Player player2) {
        // register that these players are opponents
        synchronized (opponentSyncObject) {

            if (opponents.containsKey(player1)) {
                if (opponents.get(player1).equals(player2)) {
                    opponents.remove(player2);
                }
            }
            if (opponents.containsKey(player2)) {
                if (opponents.get(player2).equals(player1)) {
                    opponents.remove(player2);
                }
            }
            List<Player> playersToRemove = new ArrayList<>();
            // remove the vise versa as well
            for (Player key : opponents.keySet()) {
                if (opponents.get(key).equals(player1)) {
                    playersToRemove.add(key);
                }
            }
            for (Player key : opponents.keySet()) {
                if (opponents.get(key).equals(player2)) {
                    playersToRemove.add(key);
                }
            }
            for (Player removeMe : playersToRemove) {
                opponents.remove(removeMe);
            }
            int a = 3;
        }
    }

    /**
     * Determines if a player is in a game with another player
     *
     * @param player1 Any player to test
     * @param player2 Any other player to test
     * @return True if a player is in game with another player
     */
    public boolean isInGameWithPlayer(Player player1, Player player2) {
        synchronized (opponentSyncObject) {
            if (opponents.get(player1) != null && opponents.get(player1).equals(player2)) {
                return true;
            }
            if (opponents.containsKey(player2)) {
                if (opponents.get(player2) == player1) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * get the game id of a player given a player
     *
     * @param player the player we are checking if is in a game
     * @return 0 if player is not in a game || the id of the game the player is in
     */
    public int getId(Player player) {
        Set<Integer> keySet = gameIds.keySet();
        int gameId = 0;
        for (int i : keySet) {
            if ((gameIds.get(i) != null && gameIds.get(i).equals(player))
                    || gameIds.get(i).equals(getOpponent(player))) {
                // get the most recent game
                if (i > gameId /*todo idk if this should be here && !allGames.get(String.valueOf(gameId)).isDone()*/)
                    gameId = i;
            }
        }
        return gameId;
    }

    /**
     * get the game given an id
     *
     * @param gameId the id of the game
     * @return the game corresponding to the unique id
     */
    public Game getGame(String gameId) {
        return this.allGames.get(gameId);
    }

    /**
     * get the opponent Player object of player
     *
     * @param player the player whom we are checking has an opponent
     * @return null if the player has no opponent || the opponent player
     */
    public Player getOpponent(Player player) {
        synchronized (opponentSyncObject) {
            Set<Player> keySet = opponents.keySet();
            if (keySet.contains(player)) {
                // the opponent is the val in the Map so return it
                return opponents.get(player);
            } else {
                // the opponent may be the key in the Map so try to find it
                for (Player key : keySet) {
                    if (opponents.get(key) == player) {
                        // found the opponent of player
                        return key;
                    }
                }
            }
            return null;

        }
    }

    public Map<Player, Player> getOpponents() {
        return this.opponents;
    }

    /**
     * get the player object corresponding to the unique username
     *
     * @param username the username of the player
     * @return null if the username does not have a corresponding player || the
     * player object
     */
    public Player getPlayer(String username) {
        synchronized (playerSyncObject) {
            for (Player player : playersLoggedIn) {
                if (player.getName().equals(username)) {
                    // found the correct player
                    return player;
                }
            }
            // did not find a player matching that username
            return null;
        }

    }

    /**
     * add a player to the list of players
     *
     * @param name the username of the player
     * @return null if the username exists || the player object created for that
     * username
     */
    public Player addPlayer(String name) {
        synchronized (playerSyncObject) {
            for (Player player : playersLoggedIn) {
                if (player.getName().equals(name)) {
                    // the username already exists
                    return null;
                }
            }
            // create the player object because no username exists
            Player player1 = new Player(name);
            playersLoggedIn.add(player1);
            return player1;
        }
    }

    /**
     * remove the player from the list of players
     *
     * @param player the player to remove from the list
     */
    public void removePlayer(Player player) {
        synchronized (playerSyncObject) {
            // if the player exists
            if (playersLoggedIn.contains(player)) {
                // remove the player
                playersLoggedIn.remove(player);
            }
        }
    }

    /**
     * Gets a list of player objects that are logged in
     *
     * @return the list
     */
    public List<Player> getLoggedInPlayers() {
        return this.playersLoggedIn;
    }

    /**
     * get the number of logged in players
     *
     * @return size of the list of players logged in
     */
    public int getNumLoggedInPlayers() {
        return this.playersLoggedIn.size();
    }
}