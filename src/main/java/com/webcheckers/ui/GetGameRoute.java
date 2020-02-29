package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.*;
import java.util.logging.Logger;
import static com.webcheckers.model.Game.Mode.PLAY;
import static com.webcheckers.model.Piece.Color.*;
import static com.webcheckers.appl.PlayerLobby.*;
import static spark.Spark.halt;

public class GetGameRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final TemplateEngine templateEngine;
    private PlayerLobby lobby;
    private Gson gson;

    private static final String TITLE = "title";
    public static final String CURRENT_USER = "currentUser";
    private static final String GAME_ID = "gameID";
    private static final String VIEW_MODE = "viewMode";
    private static final String MODE_OPTIONS = "modeOptions";
    private static final String RED_PLAYER = "redPlayer";
    private static final String WHITE_PLAYER = "whitePlayer";
    private static final String ACTIVE_COLOR = "activeColor";

    public GetGameRoute(TemplateEngine templateEngine, PlayerLobby lobby) {
        this.templateEngine = templateEngine;
        this.lobby = lobby;
        this.gson = new Gson();
        LOG.config("GetGameRoute is Initialized");
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, Object> vm = new HashMap<>();
        Session session = request.session();
        vm.put(TITLE, "GAME");

        LOG.finer("GetGameRoute is invoked");
        Player player = session.attribute("UserAttrib");
        String opponentName = request.queryParams("opponent");
        Player opponent = lobby.getPlayer(opponentName);
        if ( opponentName!=null && opponent.isInGame() ){
            vm.put("message", Message.error("This player is already in a Game"));
            vm.put(TITLE, "Welcome!");
            List<Player> newPlayerList = lobby.getLoggedInPlayers();
            newPlayerList.remove(player);
            vm.put("readyPlayers", newPlayerList);
            vm.put("currentUser", player);
            vm.put("numLoggedIn", lobby.getNumLoggedInPlayers());
            return templateEngine.render(new ModelAndView(vm, "home.ftl"));
//            response.redirect(WebServer.HOME_URL);
//            halt();
        }
        vm.put(VIEW_MODE, PLAY);
        vm.put(ACTIVE_COLOR, RED);
        String gameId = String.valueOf(lobby.getId(player));
        vm.put(GAME_ID, gameId);
        vm.put(CURRENT_USER, player);
        if ( opponent != null ){
            lobby.addMatch(player, opponent);
            player.joinedGame();
            opponent.joinedGame();
//            lobby.removePlayer(player);
//            lobby.removePlayer(opponent);
            vm.put(RED_PLAYER, player);
            vm.put(WHITE_PLAYER, opponent);
        }
        else{
            vm.put(RED_PLAYER, lobby.getOpponent(player));
            vm.put(WHITE_PLAYER, player);
            //return templateEngine.render(new ModelAndView(vm, "game.ftl"));
        }
        session.attribute(CURRENT_USER, vm.get(CURRENT_USER));
        session.attribute(RED_PLAYER, vm.get(RED_PLAYER));
        session.attribute(WHITE_PLAYER, vm.get(WHITE_PLAYER));
        session.attribute(ACTIVE_COLOR, vm.get(ACTIVE_COLOR));
        session.attribute(GAME_ID, gameId);

//        if ( request.requestMethod().equals("GET")){
//
//        }
        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}
