package com.webcheckers.appl;

import java.util.*;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import freemarker.ext.beans.HashAdapter;

public class PlayerLobby {
    private List<Player> playersLoggedIn;
    //private Map<String, Player> playerMap;
    private Object syncObject = new Object();
    private Map<Player, Player> opponents;
    private Map<Integer, Player> gameIds;
    private Map<String, Game> allGames;
    private int id;

    public PlayerLobby() {
        playersLoggedIn = new ArrayList<>();
        //playerMap = new HashMap<String, Player>();
        this.opponents = new HashMap<>();
        this.id = 1;
        this.gameIds = new HashMap<>();
        this.allGames = new HashMap<>();
    }

    public void addMatch(Player player1, Player player2){
        opponents.put(player1, player2);
        gameIds.put(this.id, player1);
        Game game = new Game(player1, player2);
        allGames.put(String.valueOf(this.id), game);
        this.id++;
    }

    public int getId(Player player){
        Set<Integer> keySet = gameIds.keySet();
        for (int i : keySet){
            if ( gameIds.get(i) == player || gameIds.get(i) == getOpponent(player)){
                return i;
            }
        }
        return 0;
    }

    public Game getGame( String gameId ){
        return this.allGames.get(gameId);
    }

    public Player getOpponent(Player player){
        Set<Player> keySet = opponents.keySet();
        if ( keySet.contains(player) ){
            return opponents.get(player);
        }
        else{
            for ( Player key : keySet ){
                if ( opponents.get(key) == player){
                    return key;
                }
            }
        }
        return null;
    }

    public Player getPlayer ( String Username ){
        for (Player player : playersLoggedIn){
            if ( player.getname().equals(Username)){
                return player;
            }
        }
        return null;
    }
    public Player addPlayer(String name) {
        synchronized (syncObject) {
            for ( Player player : playersLoggedIn ){
                if ( player.getname().equals(name) ){
                    return null;
                }
            }
            Player player1 = new Player(name);
            playersLoggedIn.add(player1);
            return player1;
//            if (playersLoggedIn.contains(name)) {
//                return null;
//            } else {
//                playersLoggedIn.add(name);
//                Player player = new Player(name);
//                playerMap.put(name, player);
//                return player;
//            }
        }
    }

    public void removePlayer(Player player) {
        synchronized (syncObject) {
            if (playersLoggedIn.contains(player)) {
                playersLoggedIn.remove(player);
//                playerMap.remove(player.getname());
            }
        }
    }

    // Gets the players logged in
    public List<Player> getLoggedInPlayers() {
        return this.playersLoggedIn;
    }

    public int getNumLoggedInPlayers() {
        return this.playersLoggedIn.size();
    }

//    public Map<String, Player> getPlayerObjects() {
//        return playerMap;
//    }


}