package com.webcheckers.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class SignInRouteTest {
    private Request request;
    private Response response;
    private Session session;
    private TemplateEngine templateEngine;
    private SignInRoute signInRoute;

    @BeforeEach
    void setup(){
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);

        when(request.session()).thenReturn(session);
        templateEngine = mock(TemplateEngine.class);
        signInRoute = new SignInRoute(templateEngine);
    }

    @Test
    void getRequest(){
        final TemplateEngineTester tester = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(tester.makeAnswer());
        when(request.requestMethod()).thenReturn("GET");
        signInRoute.handle(request, response);
        tester.assertViewModelExists();
        tester.assertViewModelIsaMap();
        tester.assertViewName("signin.ftl");
    }
}
