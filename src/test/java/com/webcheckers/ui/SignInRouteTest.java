package com.webcheckers.ui;

import org.junit.jupiter.api.BeforeEach;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SignInRouteTest {
    private Request request;
    private Response response;
    private Session session;
    private TemplateEngine templateEngine;
    private SignInRoute signInRoute;

    private static final String VALID_USERNAME = "Player";
    private static final String VALID_USERNAME_2 = "Player 1";
    private static final String NON_ALPHA_USERNAME = "#playerName#";
    private static final String EMPTY_NAME = "";
    private static final String SPACE_START = " player";
    private static final String SPACES = "         ";

    @BeforeEach
    void setup(){
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);

        when(request.session()).thenReturn(session);
        templateEngine = mock(TemplateEngine.class);
        signInRoute = new SignInRoute(templateEngine);
    }

}
