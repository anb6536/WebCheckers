package com.webcheckers.appl;

import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    void testPlayerThere() {
        assertEquals(playerLobby.getPlayer("Player"), null);
        Player player = playerLobby.addPlayer("Player");
        assertEquals(playerLobby.getPlayer("Player"), player);
    }

    @Test
    void testGet() {
        Player player1 = playerLobby.addPlayer("player1");
        Player player2 = playerLobby.addPlayer("player2");
        playerLobby.addMatch(player1, player2, Board.makeBoard());

        assertTrue(playerLobby.isInGameWithPlayer(player1,player2));

        int gameId = playerLobby.getId(player1);
        Game game1 = playerLobby.getGame(String.valueOf(gameId));

        assertEquals(1,gameId); // because this was the first game we made
        assertNotNull(game1);

        playerLobby.removeMatch(player1, player2);

        playerLobby.addMatch(player1, player2, Board.makeBoard());

        assertTrue(playerLobby.isInGameWithPlayer(player2,player1));

        gameId = playerLobby.getId(player1);
        Game game2 = playerLobby.getGame(String.valueOf(gameId));

        assertEquals(gameId, 2); // because this was the second game we made
        assertNotNull(game1);
        assertNotEquals(game1,game2);

        gameId = playerLobby.getId(player2);
        Game game3 = playerLobby.getGame(String.valueOf(gameId));

        assertEquals(gameId, 2); // because this was the second game we made
        assertNotNull(game3);
        assertEquals(game3,game2);

        playerLobby.removeMatch(player2, player1);


    }
}