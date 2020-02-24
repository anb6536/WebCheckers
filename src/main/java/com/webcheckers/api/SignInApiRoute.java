package com.webcheckers.api;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;

import spark.Request;
import spark.Response;
import spark.Route;

public class SignInApiRoute implements Route {
    PlayerLobby lobby;

    public SignInApiRoute(PlayerLobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String username = request.queryParams("username");
        Player player = lobby.addPlayer(username);
        if (player != null)
            request.session().attribute("UserAttrib", player);
        response.redirect("/");
        // TODO Auto-generated method stub
        return response;
    }

}