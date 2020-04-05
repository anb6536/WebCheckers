package com.webcheckers.api;

import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.PlayerLobby;

import spark.Request;
import spark.Response;

public class SignOutRoute implements spark.Route {
    private static final Logger LOG = Logger.getLogger(SignOutRoute.class.getName());
    private PlayerLobby lobby;

    public SignOutRoute(PlayerLobby lobby) {
        this.lobby = Objects.requireNonNull(lobby, "lobby is required");
        LOG.config("SignOutRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        lobby.removePlayer(request.session().attribute("UserAttrib"));
        request.session().removeAttribute("UserAttrib");
        response.redirect("/");
        return null;
    }

}