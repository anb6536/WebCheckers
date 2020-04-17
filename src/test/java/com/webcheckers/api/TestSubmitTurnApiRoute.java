package com.webcheckers.api;

import com.webcheckers.model.*;
import com.webcheckers.util.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Deserialzer;
import com.webcheckers.util.Message;

import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

class TestSubmitTurnApiRoute {
    private SubmitTurnApiRoute route;
    private PlayerLobby playerLobby;
    private Gson gson = new Gson();
    private Request request;
    private Response response;
    private Session session;
    TestLoggedInRoutes testRoutes = new TestLoggedInRoutes();
    public static final String USER_ATTR = "UserAttrib";
    public static final String GAME_ID = "gameID";
    public static final String validName1 = "Username1";
    public static final String validName2 = "Username2";

    @BeforeEach
    void setupNew() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = spy(PlayerLobby.class);
        route = new SubmitTurnApiRoute(gson, playerLobby);
    }

    @Test
    void verifySignedIn() {
        testRoutes.testLacksSignIn(route);
    }

    @Test
    void verifyIsInGame() {
        testRoutes.testIsInGame(route, playerLobby);
    }

    @Test
    void verifyValidSubmit() {
        Player player1 = playerLobby.addPlayer(validName1);
        Player player2 = playerLobby.addPlayer(validName2);
        when(request.session().attribute(USER_ATTR)).thenReturn(player1);
        Board board = Board.makeBoard();
        board.addPieces();
        playerLobby.addMatch(player1, player2, board);
        int gameId = playerLobby.getId(player1);
        Position start = Position.makePosition(5, 6);
        Position end = Position.makePosition(4, 7);
        Move move = Move.createMove(start, end);
        Map<String, String> thing = new HashMap<>();
        thing.put(GAME_ID, String.valueOf(gameId));
        when(request.body()).thenReturn(Serializer.serialize(thing));
        
        try {
            Object obj = route.handle(request, response);
            assertTrue(obj instanceof String);
            if (obj instanceof String) {
                Message ret = gson.fromJson((String) obj, Message.class);
                assertTrue(ret.getType().equals(Message.Type.INFO));
                assertTrue(ret.getText().equals("Your move has been made"));
            }
        } catch (Exception e) {

        }
    }
    @Test
    void verifyInvalidSubmit() {
        Player player1 = playerLobby.addPlayer(validName1);
        Player player2 = playerLobby.addPlayer(validName2);
        when(request.session().attribute(USER_ATTR)).thenReturn(player1);
        Board board = Board.makeBoard();
        board.addPieces();
        playerLobby.addMatch(player1, player2, board);
        int gameId = playerLobby.getId(player1);
        Position start = Position.makePosition(5, 6);
        Position end = Position.makePosition(12, 12);
        Move move = Move.createMove(start, end);
        Map<String, String> thing = new HashMap<>();
        thing.put(GAME_ID, String.valueOf(gameId));
        thing.put("actionData", gson.toJson(move));
        when(request.body()).thenReturn(Serializer.serialize(thing));
        try {
            Object obj = route.handle(request, response);
            assertTrue(obj instanceof String);
            if (obj instanceof String) {
                Message ret = gson.fromJson((String) obj, Message.class);
                assertTrue(ret.getType().equals(Message.Type.ERROR));
                assertTrue(ret.getText().equals("Your move is invalid"));
            }
        } catch (Exception e) {

        }
    }
}
