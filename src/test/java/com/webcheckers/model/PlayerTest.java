package com.webcheckers.model;

import com.webcheckers.model.Player;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class PlayerTest {
    private Player TestPlayer1;
    private Player TestPlayer2;
    private Player TestPlayer1_clone;

    private static final String NEIL_NAME = "Neil";
    private static final String STEVE_NAME = "Steve";
    private static final String CHRIS_NAME = "Chris";
    private static final String EMPTY_NAME = "";

    @BeforeEach
    void initializeValidPlayers() {
        TestPlayer1 = new Player(STEVE_NAME);
        TestPlayer2 = new Player(CHRIS_NAME);
        TestPlayer1_clone = new Player(STEVE_NAME);
    }

    @Test
    void ctor() {
        Player c_torTestPlayer = new Player(NEIL_NAME);
        assertEquals(c_torTestPlayer.getName(), NEIL_NAME);
        // Assert that the unique names in the constructor are unique when retrieved
        assertNotEquals(c_torTestPlayer.getName(), TestPlayer1.getName());

    }

    @Test
    void ctor_EmptyString() {
        Player c_torTestPlayer = new Player(EMPTY_NAME);
        assertEquals(c_torTestPlayer.getName(), EMPTY_NAME);
    }

    @Test
    void inGameTest() {
        assertFalse(TestPlayer1.isInGame());
        assertFalse(TestPlayer2.isInGame());

        // Put players in games
        TestPlayer1.joinedGame();
        TestPlayer2.joinedGame();

        assertTrue(TestPlayer1.isInGame());
        assertTrue(TestPlayer2.isInGame());

        // Take players out of games
        TestPlayer1.leftGame();
        TestPlayer2.leftGame();

        assertFalse(TestPlayer1.isInGame());
        assertFalse(TestPlayer2.isInGame());

    }

    @Test
    void equals() {
        assertEquals(TestPlayer1_clone, TestPlayer1);
        assertNotEquals(TestPlayer1, TestPlayer2);
        assertNotEquals(TestPlayer1_clone, TestPlayer2);
    }

}
