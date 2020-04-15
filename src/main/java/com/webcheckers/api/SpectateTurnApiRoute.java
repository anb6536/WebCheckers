package com.webcheckers.api;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.ui.GetGameRoute;
import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Deserialzer;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.webcheckers.model.Game.Mode.PLAY;
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
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetGameRoute.TITLE, "GAME");

        Session session = request.session();
        Player player = session.attribute("UserAttrib");
        session.attribute(GetGameRoute.CURRENT_USER, player);
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
            String whoWon = actualGame.getYouWon(player);
            modeOptions.put(GetGameRoute.GAME_OVER_MESSAGE, whoWon);

            session.attribute(GetGameRoute.IS_GAME_OVER, true);
            session.attribute(GetGameRoute.GAME_OVER_MESSAGE, actualGame.getYouWon(player));
            vm.put(GetGameRoute.BOARD, actualGame.getRedBoard().getBoardView());
            vm.put(GetGameRoute.VIEW_MODE, SPECTATOR);
            vm.put(GetGameRoute.MODE_OPTIONS, gson.toJson(modeOptions));
            vm.put(GetGameRoute.CURRENT_USER, player);
            vm.put(GetGameRoute.RED_PLAYER, actualGame.getRedPlayer());
            vm.put(GetGameRoute.WHITE_PLAYER, actualGame.getWhitePlayer());
            vm.put(GetGameRoute.ACTIVE_COLOR, actualGame.getCurrentPlayerTurn().equals(actualGame.getRedPlayer()) ? Piece.Color.RED : Piece.Color.WHITE);
            vm.put(GetGameRoute.GAME_ID, gameIdString);
            session.attribute(GetGameRoute.IS_GAME_OVER, vm.get(GetGameRoute.IS_GAME_OVER));

            return gson.toJson(Message.info(whoWon));
        }
        return gson.toJson(Message.info("true"));
    }
}