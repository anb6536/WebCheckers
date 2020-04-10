package com.webcheckers.api;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

public class TestValidateMoveApiRoute {
    private ValidateMoveApiRoute route;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private Request request;
    private Response response;
    private Session session;
    private Gson gson = new Gson();
    TestLoggedInRoutes testRoutes = new TestLoggedInRoutes();

    @BeforeEach
    void setupNew() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = spy(PlayerLobby.class);
        engine = mock(TemplateEngine.class);
        route = new ValidateMoveApiRoute(gson, playerLobby);
    }

    @Test
    void verifySignedIn() {
        testRoutes.testLacksSignIn(route);
    }

    @Test
    void verifyIsInGame() {
        testRoutes.testIsInGame(route, playerLobby);
    }
}