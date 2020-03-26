package com.webcheckers.api;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Move;
import com.webcheckers.util.Message;

import spark.Request;
import spark.Response;

public class SubmitTurnApiRoute implements spark.Route {
    private static final Logger LOG = Logger.getLogger(SubmitTurnApiRoute.class.getName());
    private final Gson gson;
    private final PlayerLobby lobby;

    /**
     * The submitturnApiRoute constructor
     */
    public SubmitTurnApiRoute(final Gson gson, final PlayerLobby lobby) {
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
        String postScreen = urlParameters.get("actionData");
        String s = URLDecoder.decode(postScreen, "UTF-8");
        String gameID = urlParameters.get("gameID");
        Move move = gson.fromJson(URLDecoder.decode(gameID, "UTF-8"), Move.class);
        if (lobby.getGame(s).validateMove(move)) {
            lobby.getGame(s).submitMove(move);
            return gson.toJson(Message.info("Your move has been made"));
        } else {
            return gson.toJson(Message.error("Your move is invalid"));
        }
    }

}