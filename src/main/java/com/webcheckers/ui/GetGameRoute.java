package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import spark.*;

import java.util.*;
import java.util.logging.Logger;
import static com.webcheckers.model.Game.Mode.PLAY;
import static com.webcheckers.model.Piece.color.RED;
import static com.webcheckers.model.Piece.color.WHITE;
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
            player.leftGame();
            vm.put("message", Message.error("This player is already in a Game"));
            vm.put(TITLE, "Welcome!");
            List<Player> newPlayerList = lobby.getLoggedInPlayers();
            List<Player> deepCopy = new ArrayList<>();
            for ( Player player1 : newPlayerList ){
                if ( !player1.equals(player) ){
                    deepCopy.add(player1);
                }
            }
            vm.put("readyPlayers", deepCopy);
            vm.put("currentUser", player);
            vm.put("numLoggedIn", lobby.getNumLoggedInPlayers());
            vm.put("isReturned", true);
            response.redirect("?error=true");
            halt();
        }
        vm.put(VIEW_MODE, PLAY);
        vm.put(ACTIVE_COLOR, RED);
        String gameId = String.valueOf(lobby.getId(player));
        vm.put(GAME_ID, gameId);
        vm.put(CURRENT_USER, player);
        BoardView boardView = makeBoard();
        addPiece(boardView);
        BoardView opponentBoard = flipBoard(boardView);

        if ( opponent != null ){
            lobby.addMatch(player, opponent);
            player.joinedGame();
            opponent.joinedGame();
            vm.put(RED_PLAYER, player);
            vm.put(WHITE_PLAYER, opponent);
            vm.put(BOARD, boardView);
        }
        else{
            vm.put(RED_PLAYER, lobby.getOpponent(player));
            vm.put(WHITE_PLAYER, player);
            vm.put(BOARD, opponentBoard);
        }
        session.attribute(CURRENT_USER, vm.get(CURRENT_USER));
        session.attribute(RED_PLAYER, vm.get(RED_PLAYER));
        session.attribute(WHITE_PLAYER, vm.get(WHITE_PLAYER));
        session.attribute(ACTIVE_COLOR, vm.get(ACTIVE_COLOR));
        session.attribute(GAME_ID, gameId);

        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }

    public BoardView makeBoard(){
        List<Row> rows = new ArrayList<>();
        for ( int i=0 ; i<8 ; i++ ){
            List<Space> spaces = new ArrayList<>();
            for ( int j=0 ; j<8 ; j++ ){
                Space space = new Space(i+j);
                if ( i%2 == 0 ){
                    if ( j%2 == 0 ){
                        space.setColor(Space.Color.LIGHT);
                    }
                    else {
                        space.setColor(Space.Color.DARK);
                    }
                }
                else {
                    if ( j%2 != 0 ){
                        space.setColor(Space.Color.LIGHT);
                    }
                    else {
                        space.setColor(Space.Color.DARK);
                    }
                }
                spaces.add(space);
            }
            Row row = new Row(i, spaces);
            rows.add(row);
        }
        BoardView boardView = new BoardView(rows);
        return boardView;
    }

    public void addPiece( BoardView boardView ){
        List<Row> rows = boardView.getRows();
        int i = 0;
        for ( Row row : rows ){
            List<Space> spaces = row.getSpaces();
            for ( Space space : spaces ){
                if ( i>=6 ){
                    if ( space.isValid() ){
                        Piece piece = new Piece(RED);
                        space.setPiece(piece);
                    }
                }
                else if (i<=1){
                    if ( space.isValid() ){
                        Piece piece = new Piece(WHITE);
                        space.setPiece(piece);
                    }
                }
            }
            i++;
        }
    }

    public BoardView flipBoard( BoardView boardView ){
        Row[] rows = new Row[8];
        List<Row> rows1 = boardView.getRows();
        List<Row> newRow = new ArrayList<>();
        int i = 7;
        for ( Row row : rows1 ){
            Space[] spaces = new Space[8];
            List<Space> spaces1 = row.getSpaces();
            List<Space> newSpaces = new ArrayList<>();
            int j=7;
            for ( Space space : spaces1 ){
                spaces[j] = space;
                j--;
            }
            for ( int k=0 ; k<8 ; k++ ){
                newSpaces.add(spaces[k]);
            }
            Row newRow1 = new Row(row.getIndex(), newSpaces);
            rows[i] = newRow1;
            i--;
        }
        for (int j=0 ; j<8 ; j++){
            newRow.add(rows[j]);
        }
        BoardView boardView1 = new BoardView(newRow);
        return boardView1;
    }
}
