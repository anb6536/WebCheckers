package com.webcheckers.api;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

public class SpectateStopApiRoute implements Route {
    private static final Logger LOG = Logger.getLogger(SignOutRoute.class.getName());

    public SpectateStopApiRoute() {
        LOG.config("SignOutRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.redirect("/");
        return null;
    }
}
