package com.webcheckers.appl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webcheckers.model.Player;

public class PlayerLobby {
    private List<String> playersLoggedIn;
    private Map<String, Player> playerMap;
    private Object syncObject = new Object();

    public PlayerLobby() {
        playersLoggedIn = new ArrayList<String>();
        playerMap = new HashMap<String, Player>();
    }

    public Player getPlayer ( String Username ){
        if ( playersLoggedIn.contains(Username) ){
            return playerMap.get(Username);
        }
        else {
            return null;
        }
    }
    public Player addPlayer(String name) {
        synchronized (syncObject) {
            if (playersLoggedIn.contains(name)) {
                return null;
            } else {
                playersLoggedIn.add(name);
                Player player = new Player(name);
                playerMap.put(name, player);
                return player;
            }
        }
    }

    public void removePlayer(Player player) {
        synchronized (syncObject) {
            if (playersLoggedIn.contains(player.getname())) {
                playersLoggedIn.remove(player.getname());
                playerMap.remove(player.getname());
            }
        }
    }

    // Gets the players logged in
    public List<String> getLoggedInPlayers() {
        return this.playersLoggedIn;
    }

    public int getNumLoggedInPlayers() {
        return this.playersLoggedIn.size();
    }

    public Map<String, Player> getPlayerObjects() {
        return playerMap;
    }


}