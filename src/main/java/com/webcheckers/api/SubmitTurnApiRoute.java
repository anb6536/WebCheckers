package com.webcheckers.api;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;

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
        // TODO: actual submission of the move
        return null;
    }

}