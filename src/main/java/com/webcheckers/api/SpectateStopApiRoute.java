package com.webcheckers.api;

import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.logging.Logger;

public class SpectateStopApiRoute implements Route {
    private static final Logger LOG = Logger.getLogger(SignOutRoute.class.getName());

    public SpectateStopApiRoute() {
        LOG.config("SignOutRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Session session = request.session();

        Player player = session.attribute("UserAttrib");
        player.stoppedSpectating();
        response.redirect("/");
        return null;
    }
}
