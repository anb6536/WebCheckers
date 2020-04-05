package com.webcheckers.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

public class SignInApiRoute implements Route {
    private PlayerLobby lobby;
    private TemplateEngine engine;

    public SignInApiRoute(PlayerLobby lobby, TemplateEngine engine) {
        this.lobby = Objects.requireNonNull(lobby, "The lobby is required");
        this.engine = Objects.requireNonNull(engine, "The template engine is required");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String username = request.queryParams("username");
        Map<String, Object> vm = new HashMap<>();

        if (username.length() > 20) { // need to change this value here and in signin.ftl for future reference
            vm.put("message", Message.error("Your username must be less than twenty characters long"));
            return engine.render(new ModelAndView(vm, "signin.ftl"));
        }
        if (!username.matches("[a-zA-Z0-9 ]+") || username.charAt(0) == ' '
                || username.charAt(username.length() - 1) == ' ') {
            vm.put("message", Message.error(
                    "You must have a username with at least 1 alphaneumric character and it cannot start or end with a space"));
            return engine.render(new ModelAndView(vm, "signin.ftl"));
        }

        Player player = lobby.addPlayer(username);
        if (player != null) {
            request.session().attribute("UserAttrib", player);
            response.redirect("/");
            return null;
        }
        vm.put("message", Message.error("Username already taken"));
        return engine.render(new ModelAndView(vm, "signin.ftl"));
    }

}