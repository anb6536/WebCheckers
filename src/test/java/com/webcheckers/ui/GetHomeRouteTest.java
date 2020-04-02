package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Board;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

public class GetHomeRouteTest {
    private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
    private static final String PLAYER_A = "aA";
    private static final String PLAYER_B = "bB";
    private static final String USER_ATTR = "UserAttrib";
    private GetHomeRoute CuT;
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private Board board;
    PlayerLobby playerLobby;

    @BeforeEach
    public void setup() {
        // set up for the handle method
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        board = mock(Board.class);
        playerLobby = new PlayerLobby();
        CuT = new GetHomeRoute(engine, playerLobby);
    }

    @Test
    public void welcome_message() {
        Player currentPlayer = new Player(PLAYER_A);
        playerLobby.addPlayer(PLAYER_A);
        // when the class gets the session, return the mock session
        when(request.session()).thenReturn(session);
        when(session.attribute(USER_ATTR)).thenReturn(currentPlayer);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        // handle the request
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            fail(e);
        }

        // check that everything is good
        testHelper.assertViewModelAttribute("title", "Welcome!");
        testHelper.assertViewModelAttribute("numLoggedIn", 1);
        testHelper.assertViewModelAttribute("signedIn", true);
        testHelper.assertViewModelAttribute("opponent", null);
        testHelper.assertViewModelAttribute("currentUser", currentPlayer);
    }

    @Test
    public void twoSignedIn() {
        Player currentPlayer = new Player(PLAYER_A);
        Player otherPlayer = new Player(PLAYER_B);
        playerLobby.addPlayer(PLAYER_A);
        playerLobby.addPlayer(PLAYER_B);
        // when the class gets the session, return the mock session
        when(request.session()).thenReturn(session);
        when(session.attribute(USER_ATTR)).thenReturn(currentPlayer);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        // handle the request
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            fail(e);
        }

        // check that everything is good
        testHelper.assertViewModelAttribute("title", "Welcome!");
        testHelper.assertViewModelAttribute("numLoggedIn", 2);
        testHelper.assertViewModelAttribute("signedIn", true);
        testHelper.assertViewModelAttribute("opponent", null);
        testHelper.assertViewModelAttribute("currentUser", currentPlayer);
    }

    @Test
    public void startedGame() {
        Player currentPlayer = new Player(PLAYER_A);
        Player opponent = new Player(PLAYER_B);
        playerLobby.addPlayer(PLAYER_A);
        playerLobby.addPlayer(PLAYER_B);
        playerLobby.addMatch(opponent, currentPlayer, board);
        // when the class gets the session, return the mock session
        when(request.session()).thenReturn(session);
        when(session.attribute(USER_ATTR)).thenReturn(currentPlayer);
        when(request.queryParams("opponent")).thenReturn(PLAYER_B);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        // handle the request
        try {
            assertNull(CuT.handle(request, response));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void inGame() {
        Player currentPlayer = new Player(PLAYER_A);
        Player opponent = new Player(PLAYER_B);
        // simulate the other player starting the game
        playerLobby.addPlayer(PLAYER_A);
        playerLobby.addPlayer(PLAYER_B);
        playerLobby.addMatch(opponent, currentPlayer, board);
        // when the class gets the session, return the mock session
        when(request.session()).thenReturn(session);
        when(session.attribute(USER_ATTR)).thenReturn(currentPlayer);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        // handle the request
        try {
            // make sure this starts a game
            Assertions.assertNull(CuT.handle(request, response));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void notSignedIn() {
        // Player currentPlayer = new Player(PLAYER_A);
        // playerLobby.addPlayer(PLAYER_A);
        // when the class gets the session, return the mock session
        when(request.session()).thenReturn(session);
        // when(session.attribute(USER_ATTR)).thenReturn(currentPlayer);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        // handle the request
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            fail(e);
        }

        // check that everything is good
        testHelper.assertViewModelAttribute("title", "Welcome!");
        testHelper.assertViewModelAttribute("signedIn", false);
        testHelper.assertViewModelAttribute("numLoggedIn", 0);
    }
}
