package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import spark.*;

import java.util.*;
import java.util.logging.Logger;
import static com.webcheckers.model.Game.Mode.PLAY;
import static com.webcheckers.model.Piece.Color.RED;
import static com.webcheckers.model.Piece.Color.WHITE;
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
    private static final String BOARD = "board";

    /**
     * instantiates the GetGameRoute
     * 
     * @param templateEngine used to render the page
     * @param lobby          a reference of the player lobby
     */
    public GetGameRoute(TemplateEngine templateEngine, PlayerLobby lobby, Gson gson) {
        this.templateEngine = templateEngine;
        this.lobby = lobby;
        this.gson = gson;
        LOG.config("GetGameRoute is Initialized");
    }

    /**
     * handles any request to view /game
     * 
     * @param request  the request made to view the page
     * @param response our response to that request
     * @return the templateEngines view
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

        // create the vm that we give to the template engine
        Map<String, Object> vm = new HashMap<>();
        Session session = request.session();
        vm.put(TITLE, "GAME");

        LOG.finer("GetGameRoute is invoked");

        // get data about the request
        Player player = session.attribute("UserAttrib");
        String opponentName = request.queryParams("opponent");
        Player opponent = lobby.getPlayer(opponentName);

        // if the opponent is in the gmae, return to the home page with an error
        // parameter
        if (opponentName != null && opponent.isInGame()) {
            player.leftGame();
            response.redirect("?error=true");
            halt();
        }
        // put stuff in the vm
        String gameId = String.valueOf(lobby.getId(player));
        vm.put(VIEW_MODE, PLAY);
        vm.put(ACTIVE_COLOR, RED);
        vm.put(GAME_ID, gameId);
        vm.put(CURRENT_USER, player);

        // create the board to give to both players
        Board board = new Board();
        BoardView boardView = board.makeBoard();
        board.addPiece(boardView);
        BoardView opponentBoard = board.flipBoard(boardView);

        // if the opponent
        if (opponent != null) {
            lobby.addMatch(player, opponent);
            player.joinedGame();
            opponent.joinedGame();
            vm.put(RED_PLAYER, player);
            vm.put(WHITE_PLAYER, opponent);
            vm.put(BOARD, boardView);
        } else {
            vm.put(RED_PLAYER, lobby.getOpponent(player));
            vm.put(WHITE_PLAYER, player);
            vm.put(BOARD, opponentBoard);
        }

        // attribute information about this game to the session
        session.attribute(CURRENT_USER, vm.get(CURRENT_USER));
        session.attribute(RED_PLAYER, vm.get(RED_PLAYER));
        session.attribute(WHITE_PLAYER, vm.get(WHITE_PLAYER));
        session.attribute(ACTIVE_COLOR, vm.get(ACTIVE_COLOR));
        session.attribute(GAME_ID, gameId);

        // show the game screen
        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}
