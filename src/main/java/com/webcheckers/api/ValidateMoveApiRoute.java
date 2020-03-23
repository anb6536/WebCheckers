package com.webcheckers.api;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Move;
import com.webcheckers.util.Message;

import org.eclipse.jetty.util.UrlEncoded;

import spark.Request;
import spark.Response;
import spark.TemplateEngine;

public class ValidateMoveApiRoute implements spark.Route {
    private static final Logger LOG = Logger.getLogger(ValidateMoveApiRoute.class.getName());
    final Gson gson;

    /**
     * The submitturnApiRoute constructor
     */
    public ValidateMoveApiRoute(final Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson is required");
        LOG.config("ValidateMoveApiRoute initialized");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String preScreen = request.body();
        String postScreen = "";
        String[] list = preScreen.split("&");
        for (String str : list) {
            if (str.matches("actionData=.*")) {
                postScreen = str.replace("actionData=", "");
                break;
            }
        }

        String s = URLDecoder.decode(postScreen, "UTF-8");
        Move move = gson.fromJson(s, Move.class);
        // TODO: implement ACTUAL VALIDATION and return the message that tells us if it's good or not
        String v = gson.toJson(Message.info("good"));
        // For this you just return a GSON of message of error or info and you'll get it to work.
        return v;
    }
}