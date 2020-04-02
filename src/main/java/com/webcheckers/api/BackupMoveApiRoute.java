package com.webcheckers.api;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import spark.Request;
import spark.Response;
import spark.Route;

public class BackupMoveApiRoute implements Route {
    private PlayerLobby lobby;
    private Gson gson;

    public BackupMoveApiRoute(PlayerLobby lobby, Gson gson) {
        this.lobby = lobby;
        this.gson = gson;
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
        Game game = lobby.getGame(gameID);
        Player currentPlayer = request.session().attribute("UserAttrib");

        if (currentPlayer == null) {
            return gson.toJson(Message.error("You need to be logged in to perform this action"));
        }
        if (game.backupMove(currentPlayer)) {
            return gson.toJson(Message.info("Successfully backed up move"));
        }
        return gson.toJson(Message.error("Error attempting to back up move"));
    }

}