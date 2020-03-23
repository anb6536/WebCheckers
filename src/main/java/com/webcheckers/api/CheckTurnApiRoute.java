package com.webcheckers.api;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import spark.Request;
import spark.Response;

public class CheckTurnApiRoute implements spark.Route {
    private final Gson gson;
    private PlayerLobby lobby;

    public CheckTurnApiRoute(final Gson gson, final PlayerLobby lobby) {
        this.gson = Objects.requireNonNull(gson, "gson is required");
        this.lobby = Objects.requireNonNull(lobby, "lobby is required");

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, String> urlParameters = new HashMap<String, String>();
        String[] kvPairs = request.body().split("&");
        for (String str : kvPairs) {
            String[] splitted = str.split("=");
            urlParameters.put(splitted[0], URLDecoder.decode(splitted[1], "UTF-8"));
        }
        Player currentPlayer = request.session().attribute("UserAttrib");
        Game currentGame = lobby.getGame(urlParameters.get("gameID"));

        if (currentGame.isPlayerTurn(currentPlayer)) {
            return gson.toJson(Message.info("true"));
        } else {
            return gson.toJson(Message.info("false"));
        }
    }

}