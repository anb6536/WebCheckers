package com.webcheckers.api;

import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Deserialzer;
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
        Map<String, String> urlParameters = Deserialzer.deserialize(request.body());
        Player currentPlayer = request.session().attribute("UserAttrib");
        Game currentGame = lobby.getGame(urlParameters.get("gameID"));

        if ( currentGame!=null&& currentGame.isPlayerTurn(currentPlayer)) {
            return gson.toJson(Message.info("true"));
        } else {
            return gson.toJson(Message.info("false"));
        }
    }

}