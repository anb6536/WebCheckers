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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.webcheckers.model.Game.Mode.SPECTATOR;
import static com.webcheckers.model.Piece.Color.RED;
import static com.webcheckers.model.Piece.Color.WHITE;

public class SpectateTurnApiRoute implements Route {
    private PlayerLobby lobby;
    private Gson gson;
    private TemplateEngine templateEngine;

    public SpectateTurnApiRoute(Gson gson, PlayerLobby lobby, TemplateEngine templateEngine) {
        this.gson = Objects.requireNonNull(gson, "gson is required");
        this.lobby = Objects.requireNonNull(lobby, "lobby is required");
        this.templateEngine = templateEngine;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        System.out.println("Spectator is checking turn");
        Map<String, Object> vm = new HashMap<>();
        Session session = request.session();
        Player player = session.attribute("UserAttrib");
        session.attribute(GetGameRoute.SPECTATING, true); //just to double check

        String gameIdString = session.attribute(GetGameRoute.SPECTATING_GAME_ID);
        if (gameIdString == null) {
            System.err.println("The gameId was not in the spectator session");
            // idk how the user got here
            return gson.toJson(Message.error("To spectate, you must have the game id"));
        }
        // this is probably a spectator game
        // if we are spectating a game
        // show the game screen
        Map<String, Object> modeOptions = new HashMap<>();
        modeOptions.put(GetGameRoute.IS_GAME_OVER, false);
        modeOptions.put(GetGameRoute.GAME_OVER_MESSAGE, "");

        Game actualGame = lobby.getGame(gameIdString);
        if (actualGame == null) {
            System.err.println("The acutal game was null in the spectator session");
            return gson.toJson(Message.error("To spectate, you must specify who you're spectating"));
        }
        // if the game is over, put game over message there
        if (actualGame.isDone()) {
            modeOptions.put(GetGameRoute.IS_GAME_OVER, true);
            session.attribute(GetGameRoute.IS_GAME_OVER, vm.get(GetGameRoute.IS_GAME_OVER));
            // if we finished a game, say who won
            Game game = lobby.getGame(gameIdString);
            if (game != null) {
                String whoWon = game.getYouWon(player);
                modeOptions.put(GetGameRoute.GAME_OVER_MESSAGE, whoWon);
            }
            vm.put(GetGameRoute.MODE_OPTIONS, gson.toJson(modeOptions));
            return templateEngine.render(new ModelAndView(vm, "game.ftl"));

        }
        return gson.toJson(Message.info("true"));
    }
}