package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.webcheckers.model.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPlayerLobby {
    private PlayerLobby playerLobby = new PlayerLobby();

    @BeforeEach
    void clearLobby() {
        playerLobby = new PlayerLobby();
    }

    @Test
    void AddPlayer() {
        Player player = playerLobby.addPlayer("Hello");

        assertEquals(player.getName(), "Hello");
        assertEquals(playerLobby.getPlayer("Hello"), player);
        assertEquals(playerLobby.getPlayer("Hello").getName(), "Hello");
    }

    @Test
    void RemovePlayer() {
        Player player = playerLobby.addPlayer("PlayerName");

        assertEquals(player.getName(), "PlayerName");
        playerLobby.removePlayer(player);

        Player player2 = playerLobby.getPlayer("PlayerName");
        assertEquals(player2, null);
    }

    @Test
    void TestLoggedInPlayers() {
        assertEquals(playerLobby.getNumLoggedInPlayers(), 0);
        Player player = playerLobby.addPlayer("Hello");
        assertEquals(playerLobby.getNumLoggedInPlayers(), 1);
        playerLobby.removePlayer(player);
        assertEquals(playerLobby.getNumLoggedInPlayers(), 0);
    }

    @Test
    void TestPlayerThere() {
        assertEquals(playerLobby.getPlayer("Player"), null);
        Player player = playerLobby.addPlayer("Player");
        assertEquals(playerLobby.getPlayer("Player"), player);
    }
}