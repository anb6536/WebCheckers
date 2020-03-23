package com.webcheckers.api;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;

import spark.Request;
import spark.Response;
import spark.TemplateEngine;

public class SubmitTurnApiRoute implements spark.Route {
    private static final Logger LOG = Logger.getLogger(SubmitTurnApiRoute.class.getName());
    final Gson gson;
    /**
     * The submitturnApiRoute constructor
     */
    public SubmitTurnApiRoute(final Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String s = request.contentType();
        LOG.fine(s);
        return null;
    }

}