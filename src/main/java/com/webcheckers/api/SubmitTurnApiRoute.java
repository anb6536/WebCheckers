package com.webcheckers.api;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Deserialzer;
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
        Map<String, String> urlParameters = Deserialzer.deserialize(request.body());

        String gameID = urlParameters.get("gameID");
        Player currentPlayer = request.session().attribute("UserAttrib");
        if (currentPlayer == null) {
            return gson.toJson(Message.error("You need to be logged in to perform this action"));
        }
        Game thisGame = lobby.getGame(gameID);

        if (thisGame.submitMove(currentPlayer)) {
            return gson.toJson(Message.info("Your move has been made"));
        } else {
            return gson.toJson(Message.error("Your move is invalid"));
        }
    }

}