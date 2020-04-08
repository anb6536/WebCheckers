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

public class BackupMoveApiRoute implements Route {
    private PlayerLobby lobby;
    private Gson gson;

    public BackupMoveApiRoute(PlayerLobby lobby, Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson is required");
        this.lobby = Objects.requireNonNull(lobby, "lobby is required");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, String> urlParameters = Deserialzer.deserialize(request.body());
        String gameID = urlParameters.get("gameID");
        Game game = lobby.getGame(gameID);
        Player currentPlayer = request.session().attribute("UserAttrib");

        if (currentPlayer == null) {
            return gson.toJson(Message.error("You need to be logged in to perform this action"));
        }
        if (!game.playerIsInGame(currentPlayer)) {
            return gson.toJson(Message.error("You are not a movable member of the game!"));
        }
        if (game.backupMove(currentPlayer)) {
            return gson.toJson(Message.info("Successfully backed up move"));
        }
        return gson.toJson(Message.error("Error attempting to back up move"));
    }

}