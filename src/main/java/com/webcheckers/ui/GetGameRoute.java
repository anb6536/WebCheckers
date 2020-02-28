package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import static com.webcheckers.model.Game.Mode.PLAY;
import static com.webcheckers.model.Piece.Color.*;
public class GetGameRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final TemplateEngine templateEngine;
    private Gson gson;

    private static final String TITLE = "title";
    private static final String CURRENT_USER = "currentUser";
    private static final String GAME_ID = "gameID";
    private static final String VIEW_MODE = "viewMode";
    private static final String MODE_OPTIONS = "modeOptions";
    private static final String RED_PLAYER = "redPlayer";
    private static final String WHITE_PLAYER = "whitePlayer";
    private static final String ACTIVE_COLOR = "activeColor";

    public GetGameRoute(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.gson = new Gson();
        LOG.config("GetGameRoute is Initialized");
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, Object> vm = new HashMap<>();
        Session session = request.session();
        vm.put(TITLE, "Welcome to the Game of WEBCHECKERS");

        Player player = session.attribute(CURRENT_USER);
        vm.put(CURRENT_USER, player);
        String gameId = session.attribute(GAME_ID);
        vm.put(GAME_ID, gameId);
        vm.put(VIEW_MODE, PLAY);
        vm.put(RED_PLAYER, session.attribute(WHITE_PLAYER));
        vm.put(WHITE_PLAYER, session.attribute(RED_PLAYER));
        vm.put(ACTIVE_COLOR, RED);

        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}
