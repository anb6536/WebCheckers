package com.webcheckers.api;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.ui.GetGameRoute;
import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Deserialzer;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Map;
import java.util.Objects;

public class SpectateApiRoute implements Route {
    private PlayerLobby lobby;
    private Gson gson;

    public SpectateApiRoute(Gson gson, PlayerLobby lobby) {
        this.gson = Objects.requireNonNull(gson, "gson is required");
        this.lobby = Objects.requireNonNull(lobby, "lobby is required");

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        Session session = request.session();
        Map<String, String> body = Deserialzer.deserialize(request.body());
        if (!body.containsKey("spectate")) {
            return gson.toJson(Message.error("To spectate, you must specify who you're spectating"));
        }
        String[] playersString = body.get("spectate").split(" vs ");
        if (playersString.length != 2) {
            return gson.toJson(Message.error("To spectate, you must specify who you're spectating"));
        }
        String player1String = playersString[0];
        String player2String = playersString[1];
        Player player = session.attribute("UserAttrib");
        player.startSpectating();
        Player player1 = lobby.getPlayer(player1String);
        if (player1 == null) {
            return gson.toJson(Message.error("To spectate, you must specify who you're spectating"));
        }
        int gameId = lobby.getId(player1);
        Game game = lobby.getGame(String.valueOf(gameId));
        if (game == null) {
            return gson.toJson(Message.error("To spectate, you must specify a valid game"));
        }
        // just checking to make sure this game is valid
        Player redPlayer = game.getRedPlayer();
        Player whitePlayer = game.getWhitePlayer();
        String redPlayerString = redPlayer.getName();
        String whitePlayerString = whitePlayer.getName();

        // redirect them according to whether this is a valid combination of players
        if (redPlayerString.equals(player1String)) {
            if (whitePlayerString.equals(player2String)) {
                // this is good. redirect them! :D
                request.session().attribute(GetGameRoute.SPECTATING, true);
                request.session().attribute(GetGameRoute.SPECTATING_GAME_ID, String.valueOf(gameId));
                response.redirect(WebServer.GAME_URL);
                return gson.toJson(Message.info("you are now spectating"));
            }
        } else {
            if (whitePlayerString.equals(player1String) && redPlayerString.equals(player2String)) {
                // this is good. redirect them! :D
                request.session().attribute(GetGameRoute.SPECTATING, true);
                request.session().attribute(GetGameRoute.SPECTATING_GAME_ID, String.valueOf(gameId));
                response.redirect(WebServer.GAME_URL);
                return gson.toJson(Message.info("you are now spectating"));
            }
        }
        return gson.toJson(Message.error("To spectate, you must specify valid players"));
    }
}
