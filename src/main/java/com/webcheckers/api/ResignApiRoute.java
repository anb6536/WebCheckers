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
import spark.Route;

public class ResignApiRoute implements Route {
    private PlayerLobby lobby;
    private Gson gson;

    public ResignApiRoute(Gson gson, PlayerLobby lobby) {
        this.gson = Objects.requireNonNull(gson, "gson is required");
        this.lobby = Objects.requireNonNull(lobby, "lobby is required");

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, String> urlParameters = Deserialzer.deserialize(request.body());
        String gameID = urlParameters.get("gameID");
        Player currentPlayer = request.session().attribute("UserAttrib");
        if (currentPlayer == null) {
            return gson.toJson(Message.error("You need to be logged in to perform this action"));
        }
        Game game = lobby.getGame(gameID);
        if (!game.playerIsInGame(currentPlayer)) {
            return gson.toJson(Message.error("You must be a member of the game to resign from the game!"));
        }
        game.resign(currentPlayer);
        return gson.toJson(Message.info("You have resigned and lost!"));
    }
}
