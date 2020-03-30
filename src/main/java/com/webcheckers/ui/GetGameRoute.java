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
     * @param gson           a reference to the GSON object used to serialize
     *                       information
     */
    public GetGameRoute(final TemplateEngine templateEngine, PlayerLobby lobby, Gson gson) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
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
        if (opponentName != null && opponent.isInGame() && !lobby.isInGameWithPlayer(player, opponent)) {
            player.leftGame();
            response.redirect("?error=true");
            halt();
        }

        // if the opponent
        if (opponent != null && !player.isInGame() && !opponent.isInGame()
                && !lobby.isInGameWithPlayer(player, opponent)) {
            // Make the board if just creating it.
            Board board = Board.makeBoard();
            board.addPieces();
            lobby.addMatch(player, opponent, board);
        }

        // attribute information about this game to the session
        String sGameId = String.valueOf(lobby.getId(player));
        Game actualGame = lobby.getGame(sGameId);
        vm.put(VIEW_MODE, PLAY); // we currently only support the play viewmode
        vm.put(ACTIVE_COLOR, actualGame.getCurrentPlayerTurn() == actualGame.getRedPlayer() ? RED : WHITE);
        vm.put(WHITE_PLAYER, actualGame.getWhitePlayer());
        vm.put(RED_PLAYER, actualGame.getRedPlayer());
        vm.put(GAME_ID, sGameId);
        vm.put(CURRENT_USER, player);
        if (actualGame.getRedPlayer() == player) {
            vm.put(BOARD, actualGame.getRedBoard().getBoardView());
        } else {
            vm.put(BOARD, actualGame.getWhiteBoard().getBoardView());
        }
        session.attribute(CURRENT_USER, player);
        session.attribute(RED_PLAYER, vm.get(RED_PLAYER));
        session.attribute(WHITE_PLAYER, vm.get(WHITE_PLAYER));
        session.attribute(ACTIVE_COLOR, vm.get(ACTIVE_COLOR));
        session.attribute(GAME_ID, sGameId);

        // show the game screen
        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}
