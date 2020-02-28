package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerTypes;
import com.webcheckers.model.UserModel;
import spark.*;

import java.util.*;
import java.util.logging.Logger;

import static com.webcheckers.model.Game.Mode.PLAY;

public class GetGameRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final TemplateEngine templateEngine;
    private Gson gson;

    private static final String CURRENT_USER = "currentUser";
    private static final String GAME_ID = "gameID";
    private static final String VIEW_MODE = "viewMode";
    private static final String MODE_OPTIONS = "modeOptions";
    private static final String RED_PLAYER = "redPlayer";
    private static final String WHITE_PLAYER = "whitePlayer";
    private static final String ACTIVE_COLOR = "activeColor";
    private static final String TITLE = "title";
    public static final String USERATTRIB = "UserAttrib";
    private PlayerLobby playerLobby;

    public GetGameRoute(TemplateEngine templateEngine, PlayerLobby lobby) {
        this.templateEngine = templateEngine;
        this.playerLobby = lobby;
        this.gson = new Gson();
        LOG.config("GetGameRoute is Initialized");
    }


    /**
     * handle all requests where we are in the game
     *
     * @param request  idek TODO
     * @param response idek TODO
     * @return the templateEngine? TODO
     * @throws Exception TODO why?
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // get the current session
        Session session = request.session();

        // get the current player object and opponent name
        Player player = session.attribute(USERATTRIB);
        //TODO error checking (if someone types in the url ?opponent=null)
        String opponentName = request.queryParamsValues("opponent")[0];
        Map<String, Player> playerObjects = playerLobby.getPlayerObjects();
        Player opponent = playerObjects.get(opponentName);

        // make sure this opponent playerName pair are registered
        if (!GetHomeRoute.playersInGame.containsKey(player.getname())) {
            if (!UserModel.playersWithTypes.containsKey(player.getname())) {
                // decide who is red and who is white
                if (new Random().nextDouble() < 0.5) {
                    UserModel.playersWithTypes.put(player.getname(), PlayerTypes.types.RED);
                    UserModel.playersWithTypes.put(opponentName, PlayerTypes.types.WHITE);
                }
            }
            GetHomeRoute.playersInGame.put(player.getname(), opponentName);
        }

        // check who is red and who is white
        Player redPlayer;
        Player whitePlayer;
        if (0 == UserModel.playersWithTypes.get(player.getname()).compareTo(PlayerTypes.types.RED)) {
            redPlayer = player;
            whitePlayer = opponent;
        } else {
            whitePlayer = player;
            redPlayer = opponent;
        }


        // make a new vm to put values in and populate the values
        Map<String, Object> vm = new HashMap<>();
        vm.put(CURRENT_USER, player);
        String gameId = session.attribute(GAME_ID);
        vm.put(GAME_ID, gameId);
        vm.put(VIEW_MODE, PLAY);
        vm.put(RED_PLAYER, redPlayer);
        vm.put(WHITE_PLAYER, whitePlayer);
        vm.put(MODE_OPTIONS, this.gson);
        vm.put(ACTIVE_COLOR, "Blue"); // TODO figure out what active color is
        vm.put(TITLE, "descriptvie title");


        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}
