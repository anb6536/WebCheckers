package com.webcheckers.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.ui.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

@Tag("Api-Tier")
class TestSignInApiRoute {
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private SignInApiRoute route;
    private Request request;
    private Response response;
    private Session session;
    public static final String USER_ATTR = "UserAttrib";

    @BeforeEach
    void clearLobby() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = spy(PlayerLobby.class);
        engine = mock(TemplateEngine.class);
        route = new SignInApiRoute(playerLobby, engine);
    }

    @Test
    void testSignInValid() {
        final String validName = "Username";
        when(request.queryParams("username")).thenReturn(validName);
        when(session.attribute(USER_ATTR)).thenReturn(new Player(validName));
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        try {
            assertNull(route.handle(request, response));
        } catch (Exception e) {
            fail(e);
        }
        Player newPlayer = playerLobby.getPlayer(validName);
        assertEquals(request.session().attribute(USER_ATTR), newPlayer);
        try {
            route.handle(request, response);
        } catch (Exception e) {
            fail(e);
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("signin.ftl");
    }

    @Test
    void testSignInTooLong() {
        final String invalidName4 = "UUUUUUUUUUUUUUUUUsername";
        when(request.queryParams("username")).thenReturn(invalidName4);
        when(session.attribute(USER_ATTR)).thenReturn(new Player(invalidName4));
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        try {
            route.handle(request, response);
        } catch (Exception e) {
            fail(e);
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("signin.ftl");
    }

    @Test
    void testSignInRejects() {
        final String invalidName1 = "[Username";
        final String invalidName2 = " Username";
        final String invalidName3 = "Username ";
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(request.queryParams("username")).thenReturn(invalidName1);
        when(session.attribute(USER_ATTR)).thenReturn(new Player(invalidName1));
        try {
            route.handle(request, response);
        } catch (Exception e) {
            fail(e);
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("signin.ftl");
        when(request.queryParams("username")).thenReturn(invalidName2);
        when(session.attribute(USER_ATTR)).thenReturn(new Player(invalidName2));
        try {
            route.handle(request, response);
        } catch (Exception e) {
            fail(e);
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("signin.ftl");
        when(request.queryParams("username")).thenReturn(invalidName3);
        when(session.attribute(USER_ATTR)).thenReturn(new Player(invalidName3));
        try {
            route.handle(request, response);
        } catch (Exception e) {
            fail(e);
        }
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("signin.ftl");
    }
}