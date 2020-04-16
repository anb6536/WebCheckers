package com.webcheckers.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.util.Message;
import com.webcheckers.util.Serializer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spark.Request;
import spark.Response;
import spark.Session;

public class TestBackupMoveApiRoute {
    private BackupMoveApiRoute route;
    private PlayerLobby playerLobby;
    private Request request;
    private Response response;
    private Session session;
    private Gson gson = new Gson();
    TestLoggedInRoutes testRoutes = new TestLoggedInRoutes();
    public static final String USER_ATTR = "UserAttrib";
    public static final String validName = "Username";
    public static final String validName2 = "Username2";

    @BeforeEach
    void setupNew() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = spy(PlayerLobby.class);
        route = new BackupMoveApiRoute(playerLobby, gson);
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
    void testMoveBackupWorks() {
        Player player1 = playerLobby.addPlayer(validName);
        Player player2 = playerLobby.addPlayer(validName2);
        when(session.attribute("UserAttrib")).thenReturn(player1);
        Board board = Board.makeBoard();
        board.addPieces();
        playerLobby.addMatch(player1, player2, board);
        int gameId = playerLobby.getId(player1);
        Game game = playerLobby.getGame(String.valueOf(gameId));
        Position start = Position.makePosition(5, 6);
        Position end = Position.makePosition(4, 7);
        Move move = Move.createMove(start, end);
        game.validateMove(move, player1);
        Map<String, String> thing = new HashMap<>();
        thing.put("gameID", String.valueOf(gameId));
        when(request.body()).thenReturn(Serializer.serialize(thing));
        try {
            Object obj = route.handle(request, response);
            assertTrue(obj instanceof String);
            if (obj instanceof String) {
                Message ret = gson.fromJson((String) obj, Message.class);
                assertTrue(ret.getType().equals(Message.Type.INFO));
                assertTrue(ret.getText().equals("Successfully backed up move"));
            }
        } catch (Exception e) {

        }
    }

    @Test
    void testMoveBackupFailure() {
        Player player1 = playerLobby.addPlayer(validName);
        Player player2 = playerLobby.addPlayer(validName2);
        when(session.attribute("UserAttrib")).thenReturn(player1);
        Board board = Board.makeBoard();
        board.addPieces();
        playerLobby.addMatch(player1, player2, board);
        int gameId = playerLobby.getId(player1);
        Map<String, String> thing = new HashMap<>();
        thing.put("gameID", String.valueOf(gameId));
        when(request.body()).thenReturn(Serializer.serialize(thing));
        try {
            Object obj = route.handle(request, response);
            assertTrue(obj instanceof String);
            if (obj instanceof String) {
                Message ret = gson.fromJson((String) obj, Message.class);
                assertTrue(ret.getType().equals(Message.Type.ERROR));
                assertTrue(ret.getText().equals("Error attempting to back up move"));
            }
        } catch (Exception e) {

        }
    }
}