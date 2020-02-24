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
}