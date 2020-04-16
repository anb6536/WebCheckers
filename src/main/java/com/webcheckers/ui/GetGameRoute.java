package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import spark.*;

import java.util.*;
import java.util.logging.Logger;

import static com.webcheckers.model.Game.Mode.PLAY;
import static com.webcheckers.model.Game.Mode.SPECTATOR;
import static com.webcheckers.model.Piece.Color.RED;
import static com.webcheckers.model.Piece.Color.WHITE;
import static spark.Spark.halt;

public class GetGameRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final TemplateEngine templateEngine;
    private PlayerLobby lobby;
    private Gson gson;

    public static final String TITLE = "title";
    public static final String CURRENT_USER = "currentUser";
    public static final String GAME_ID = "gameID";
    public static final String VIEW_MODE = "viewMode";
    public static final String MODE_OPTIONS = "modeOptionsAsJSON";
    public static final String RED_PLAYER = "redPlayer";
    public static final String WHITE_PLAYER = "whitePlayer";
    public static final String ACTIVE_COLOR = "activeColor";
    public static final String BOARD = "board";
    public static final String SPECTATING = "isSpectating";
    public static final String SPECTATING_GAME_ID = "spectatingGameId";
    public static final String IS_GAME_OVER = "isGameOver";
    public static final String GAME_OVER_MESSAGE = "gameOverMessage";

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
        if (opponentName == null && session.attribute(SPECTATING) != null && session.attribute(SPECTATING).equals(true)) {
            String gameIdString = session.attribute(SPECTATING_GAME_ID);
            if (gameIdString == null) {
                response.redirect("/home?error=true");
                halt();
                return null;
            }
            // this is probably a spectator game
            // if we are spectating a game
            LOG.finer("entering spectator");
            vm.put(VIEW_MODE, SPECTATOR); // we currently only support the play viewmode
            // show the game screen

            Game actualGame = lobby.getGame(gameIdString);
            if (actualGame == null) {
                response.redirect("/home?error=true");
                halt();
                return null;
            }
            vm.put(ACTIVE_COLOR, actualGame.getCurrentPlayerTurn().equals(actualGame.getRedPlayer()) ? RED : WHITE);
            vm.put(WHITE_PLAYER, actualGame.getWhitePlayer());
            vm.put(RED_PLAYER, actualGame.getRedPlayer());
            vm.put(GAME_ID, gameIdString);
            vm.put(CURRENT_USER, player);
            vm.put(BOARD, actualGame.getRedBoard().getBoardView());
            Map<String, Object> modeOptions = new HashMap<>();
            if (actualGame.isDone()) {
                modeOptions.put(IS_GAME_OVER, true);
                session.attribute(IS_GAME_OVER, vm.get(IS_GAME_OVER));
                String whoWon = actualGame.getYouWon(player);
                modeOptions.put(GAME_OVER_MESSAGE, whoWon);
            } else {
                modeOptions.put(IS_GAME_OVER, false);
                modeOptions.put(GAME_OVER_MESSAGE, "");
            }
            vm.put(MODE_OPTIONS, gson.toJson(modeOptions));
            session.attribute(GetGameRoute.SPECTATING_GAME_ID, gameIdString);
            return templateEngine.render(new ModelAndView(vm, "game.ftl"));
        }
        Player opponent = lobby.getPlayer(opponentName);

        // if the opponent is in the gmae, return to the home page with an error
        // parameter
        if (opponentName != null && opponent.isInGame() && !lobby.isInGameWithPlayer(player, opponent)) {
            player.leftGame();
            response.redirect("?error=true");
            halt();
        }

        // if we should start a game
        if (opponent != null && !player.isInGame() && !opponent.isInGame()
                && !lobby.isInGameWithPlayer(player, opponent)) {
            // Make the board if just creating it.
            Board board = Board.makeBoard();
            board.addPieces();
            lobby.addMatch(player, opponent, board);
        }
        String sGameId = String.valueOf(lobby.getId(player));
        Game actualGame = lobby.getGame(sGameId);

        // attribute information about this game to the session
        vm.put(VIEW_MODE, PLAY);
        vm.put(ACTIVE_COLOR, actualGame.getCurrentPlayerTurn().equals(actualGame.getRedPlayer()) ? RED : WHITE);
        vm.put(WHITE_PLAYER, actualGame.getWhitePlayer());
        vm.put(RED_PLAYER, actualGame.getRedPlayer());
        vm.put(GAME_ID, sGameId);
        vm.put(CURRENT_USER, player);
        Map<String, Object> modeOptions = new HashMap<>();
        modeOptions.put(IS_GAME_OVER, false);
        modeOptions.put(GAME_OVER_MESSAGE, "");


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

        // if the game is over, put game over message there
        if (actualGame.isDone()) {
            opponent = lobby.getPlayer(actualGame.getRedPlayer().getName());
            if (opponent == null) {
                opponent = lobby.getPlayer(actualGame.getWhitePlayer().getName());

            }
            player.leftGame();
            lobby.removeMatch(player, opponent);
            modeOptions.put(IS_GAME_OVER, true);
            session.attribute(IS_GAME_OVER, vm.get(IS_GAME_OVER));
            // if we finished a game, say who won
            String whoWon = actualGame.getYouWon(player);
            modeOptions.put(GAME_OVER_MESSAGE, whoWon);
            vm.put(MODE_OPTIONS, gson.toJson(modeOptions));
            return templateEngine.render(new ModelAndView(vm, "game.ftl"));

        }
        vm.put(MODE_OPTIONS, gson.toJson(modeOptions));
        // show the game screen
        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}