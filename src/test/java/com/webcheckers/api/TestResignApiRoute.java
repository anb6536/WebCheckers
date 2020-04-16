package com.webcheckers.api;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Board;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestResignApiRoute {
    private static final String USER_ATTRIB = "UserAttrib";
    private ResignApiRoute route;
    private PlayerLobby playerLobby;
    private Request request;
    private Response response;
    private Session session;
    private Gson gson = new Gson();

    @BeforeEach
    void setupNew() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = spy(PlayerLobby.class);
        route = new ResignApiRoute(gson, playerLobby);
    }

    @Test
    void playerNull() {
        when(session.attribute(USER_ATTRIB)).thenReturn(null);
        when(request.body()).thenReturn("gameID=1");
        Message message;
        try {
            Object returning = route.handle(request, response);
            message = gson.fromJson((String) returning, Message.class); //not attempting check the String conversion
        } catch (Exception e) {
            fail(e.getMessage());
            return;
        }
        assertEquals(message.getType(), Message.Type.ERROR);
        assertEquals("You need to be logged in to perform this action", message.getText());


    }

    @Test
    void badURL() {
        when(session.attribute(USER_ATTRIB)).thenReturn(new Player("player"));
        Message message;
        try {
            Object returning = route.handle(request, response);
            message = gson.fromJson((String) returning, Message.class); //not attempting check the String conversion
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }
        assertEquals(message.getType(), Message.Type.ERROR);
        assertEquals("Your url parameters must include your gameID to perform this action", message.getText());

    }

    @Test
    void urlWithBadGame() {
        when(session.attribute(USER_ATTRIB)).thenReturn(new Player("player"));
        when(request.body()).thenReturn("gameID=1");
        Message message;
        try {
            Object returning = route.handle(request, response);
            message = gson.fromJson((String) returning, Message.class); //not attempting check the String conversion
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }
        assertEquals(message.getType(), Message.Type.ERROR);
        assertEquals("Your url parameters must include a real gameID to perform this action", message.getText());
    }

    @Test
    void playerNotInGame() {
        Player player1 = new Player("player1");
        playerLobby.addMatch(player1, new Player("player2"), Board.makeBoard());
        playerLobby.addMatch(new Player("player3"), new Player("player4"), Board.makeBoard());
        when(session.attribute(USER_ATTRIB)).thenReturn(player1);
        when(request.body()).thenReturn("gameID=2");
        Message message;
        try {
            Object returning = route.handle(request, response);
            message = gson.fromJson((String) returning, Message.class); //not attempting check the String conversion
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }
        assertEquals(message.getType(), Message.Type.ERROR);
        assertEquals("You must be a member of the game to resign from the game!", message.getText());
    }

    @Test
    void playerResign() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Board board = Board.makeBoard();
        board.addPieces();
        playerLobby.addMatch(player1, player2, board);
        when(request.body()).thenReturn("gameID=1");
        when(session.attribute(USER_ATTRIB)).thenReturn(player1);
        Message message;
        try {
            Object returning = route.handle(request, response);
            message = gson.fromJson((String) returning, Message.class); //not attempting check the String conversion
        } catch (Exception e) {
            fail(e.getMessage());
            return;
        }
        assertEquals(Message.Type.INFO, message.getType());
        assertEquals("You have resigned and lost!", message.getText());
    }

    @Test
    void gameAlreadyFinished() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        playerLobby.addMatch(player1, player2, Board.makeBoard());
        playerLobby.removeMatch(player1, player2);
        when(session.attribute(USER_ATTRIB)).thenReturn(player1);
        when(request.body()).thenReturn("gameID=1");
        Message message;
        try {
            Object returning = route.handle(request, response);
            message = gson.fromJson((String) returning, Message.class); //not attempting check the String conversion
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }
        assertEquals(message.getType(), Message.Type.ERROR);
        assertEquals("The game is already over", message.getText());

    }
}
