package com.webcheckers.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Board;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

@TestInstance(Lifecycle.PER_METHOD)
public class TestLoggedInRoutes {
    protected TemplateEngine engine;
    protected PlayerLobby playerLobby;
    protected SignInApiRoute route;
    protected Request request;
    protected Response response;
    protected Session session;
    protected Gson gson;
    public static final String USER_ATTR = "UserAttrib";
    public static final String validName = "Username";
    public static final String validName2 = "Username1";

    public TestLoggedInRoutes() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = spy(PlayerLobby.class);
        engine = mock(TemplateEngine.class);
        gson = new Gson();
    }

    public void testLacksSignIn(Route routeToTest) {
        try {
            Object returning = routeToTest.handle(request, response);
            assertTrue(returning instanceof String);
            if (returning instanceof String) {
                Message message = gson.fromJson((String) returning, Message.class);
                assertTrue(message.getType().equals(Message.Type.ERROR));
                assertTrue(message.getText().equals("You need to be logged in to perform this action"));
            }

        } catch (Exception e) {
        }
    }

    public void testIsInGame(Route routeToTest, PlayerLobby lobby) {
        when(request.session()).thenReturn(session);
        when(session.attribute(USER_ATTR)).thenReturn(new Player("Definitely wrong"));
        Player validPlayer = lobby.addPlayer(validName);
        Player validPlayer2 = lobby.addPlayer(validName2);
        Board board = Board.makeBoard();
        board.addPieces();
        lobby.addMatch(validPlayer, validPlayer2, board);
        try {
            Object returning = routeToTest.handle(request, response);
            assertTrue(returning instanceof String);
            if (returning instanceof String) {
                Message message = gson.fromJson((String) returning, Message.class);
                assertTrue(message.getType().equals(Message.Type.ERROR));
                assertTrue(message.getText().equals("You must be a member of the game to perform this action."));
            }
        } catch (Exception e) {
        }
    }
}