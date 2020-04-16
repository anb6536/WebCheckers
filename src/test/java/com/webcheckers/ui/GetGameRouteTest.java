package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

public class GetGameRouteTest {
    private GetGameRoute CuT;
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    public static final String USER_ATTR = "UserAttrib";
    public static final String PLAYER_NAME = "Kevin";
    public static final String OPP_NAME = "Michael";
    private static final String RED_PLAYER = "redPlayer";
    private static final String WHITE_PLAYER = "whitePlayer";
    private static final String ACTIVE_COLOR = "activeColor";
    private static final Gson gson = new Gson();
    PlayerLobby playerLobby;
    /**
     * Set up mock objects for each test
     */

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        playerLobby = new PlayerLobby();
        CuT = new GetGameRoute(engine, playerLobby, gson);
    }

    @Test
    public void new_game() {
        // Setting up scenario with two players
        Player currentPlayer = new Player(PLAYER_NAME);
        playerLobby.addPlayer(PLAYER_NAME);
        playerLobby.addPlayer(OPP_NAME);
        Player opponent = playerLobby.getPlayer(OPP_NAME);

        when(session.attribute(USER_ATTR)).thenReturn(currentPlayer);
        when(request.queryParams("opponent")).thenReturn(OPP_NAME);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        try {
            CuT.handle(request, response);
        } catch (Exception e) {
           fail(e);
        }

        // Makes sure right players are going against each other
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_USER, currentPlayer);
        testHelper.assertViewModelAttribute(RED_PLAYER, currentPlayer);
        testHelper.assertViewModelAttribute(WHITE_PLAYER, opponent);

    }

    @Test
    public void faultySession() {
        // Arrange Scenario: One of the players in a match is incorrectly null
        // The player (opponent) that will be null is created but not added to the lobby
        Player currentPlayer = new Player(PLAYER_NAME);
        playerLobby.addPlayer(PLAYER_NAME);
        Player opponent = playerLobby.getPlayer(OPP_NAME);

        when(session.attribute(USER_ATTR)).thenReturn(currentPlayer);
        when(request.queryParams("opponent")).thenReturn(OPP_NAME);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        // Since opponent was not in lobby, it cannot be found
        assertThrows(NullPointerException.class, () -> { CuT.handle(request, response);});
    }
}
