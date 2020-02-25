package com.webcheckers.appl;

import java.util.ArrayList;
import java.util.List;

import com.webcheckers.model.Player;

public class PlayerLobby {
    List<String> playersLoggedIn;
    Object syncObject = new Object();

    public PlayerLobby() {
        playersLoggedIn = new ArrayList<String>();
    }

    public Player addPlayer(String name) {
        synchronized (syncObject) {
            if (playersLoggedIn.contains(name)) {
                return null;
            } else {
                playersLoggedIn.add(name);
                return new Player(name);
            }
        }
    }

    public void removePlayer(Player player) {
        synchronized (syncObject) {
            if (playersLoggedIn.contains(player.name)) {
                playersLoggedIn.remove(player.name);
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


}