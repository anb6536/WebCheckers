package com.webcheckers.api;

import java.util.logging.Logger;

import spark.Request;
import spark.Response;

public class SignOutRoute implements spark.Route {
    private static final Logger LOG = Logger.getLogger(SignOutRoute.class.getName());

    public SignOutRoute() {
        LOG.config("SignOutRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        request.session().removeAttribute("UserAttrib");
        response.redirect("/");
        return null;
    }

}