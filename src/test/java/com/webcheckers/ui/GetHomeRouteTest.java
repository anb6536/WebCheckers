package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    PlayerLobby playerLobby;

    @BeforeEach
    public void setup() {
        // set up for the handle method
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

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
}
