package com.webcheckers.api;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;

import spark.Request;
import spark.Response;
import spark.TemplateEngine;

class SubmitTurnApiRoute implements spark.Route {
    final Gson gson;
    /**
     * The submitturnApiRoute constructor
     */
    public SubmitTurnApiRoute(final TemplateEngine templateEngine, final Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        request.contentType();
        return null;
    }

}