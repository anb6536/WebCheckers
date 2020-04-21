package com.webcheckers.api;

import java.util.logging.Logger;

import com.webcheckers.model.Player;
import com.webcheckers.ui.GetGameRoute;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

public class SpectateStopApiRoute implements Route {
    private static final Logger LOG = Logger.getLogger(SignOutRoute.class.getName());

    public SpectateStopApiRoute() {
        LOG.config("SignOutRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Session session = request.session();

        Player player = session.attribute("UserAttrib");
        request.session().attribute(GetGameRoute.SPECTATING, false);
        request.session().attribute(GetGameRoute.SPECTATING_GAME_ID, null);
        player.stoppedSpectating();
        response.redirect("/");
        return null;
    }
}
