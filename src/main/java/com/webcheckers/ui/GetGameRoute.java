package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    private static final String USERATTRIB = "UserAttrib";
    private PlayerLobby playerLobby;

    public GetGameRoute(TemplateEngine templateEngine, PlayerLobby lobby) {
        this.templateEngine = templateEngine;
        this.playerLobby = lobby;
        this.gson = new Gson();
        LOG.config("GetGameRoute is Initialized");
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, Object> vm = new HashMap<>();
        Session session = request.session();

        Player player = session.attribute(USERATTRIB);
        vm.put(CURRENT_USER, player);
        String gameId = session.attribute(GAME_ID);

        vm.put(GAME_ID, gameId);
        vm.put(VIEW_MODE, PLAY);
        vm.put(RED_PLAYER, player); // TODO figure out who is white player
        Map<String, Player> playerObjects = playerLobby.getPlayerObjects();
        String opponentName = request.queryParamsValues("opponent")[0];
        vm.put(WHITE_PLAYER, playerObjects.get(opponentName)); // TODO figure out who is red player
        vm.put(MODE_OPTIONS, this.gson);
        vm.put(ACTIVE_COLOR, "Blue"); // TODO figure out what active color is
        vm.put(TITLE, "descriptvie title");


        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}
