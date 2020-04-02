package com.webcheckers.api;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResignApiRoute implements Route {
    private PlayerLobby lobby;
    private Gson gson;

    public ResignApiRoute(Gson gson, PlayerLobby lobby) {
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
        String gameID = urlParameters.get("gameID");
        Player currentPlayer = request.session().attribute("UserAttrib");
        if (currentPlayer == null) {
            return gson.toJson(Message.error("You need to be logged in to perform this action"));
        }
        lobby.getGame(gameID).resign(currentPlayer);


        return gson.toJson(Message.info("You have resigned and lost!"));
    }
}
