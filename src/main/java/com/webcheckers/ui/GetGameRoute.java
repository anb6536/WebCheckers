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

    /**
     * instantiates the GetGameRoute
     * @param templateEngine used to render the page
     * @param lobby a reference of the player lobby
     */
    public GetGameRoute(TemplateEngine templateEngine, PlayerLobby lobby) {
        this.templateEngine = templateEngine;
        this.lobby = lobby;
        this.gson = new Gson();
        LOG.config("GetGameRoute is Initialized");
    }

    /**
     * handles any request to view /game
     * @param request the request made to view the page
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

        // if the opponent is in the gmae, return to the home page with an error parameter
        if ( opponentName!=null && opponent.isInGame() ){
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
        BoardView boardView = makeBoard();
        addPiece(boardView);
        BoardView opponentBoard = flipBoard(boardView);

        // if the opponent
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

        // attribute information about this game to the session
        session.attribute(CURRENT_USER, vm.get(CURRENT_USER));
        session.attribute(RED_PLAYER, vm.get(RED_PLAYER));
        session.attribute(WHITE_PLAYER, vm.get(WHITE_PLAYER));
        session.attribute(ACTIVE_COLOR, vm.get(ACTIVE_COLOR));
        session.attribute(GAME_ID, gameId);

        // show the game screen
        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }

    /**
     * create a BoardView with correct starting positions
     * @return the start of a board
     */
    public BoardView makeBoard(){

        // create the list of rows
        List<Row> rows = new ArrayList<>();

        // add a row to the list 8 times
        for ( int i=0 ; i<8 ; i++ ){

            // create the list of spaces for this row
            List<Space> spaces = new ArrayList<>();

            // add a space to the row 8 times
            for ( int j=0 ; j<8 ; j++ ){
                Space space = new Space(i+j);

                // if it is an even row
                if ( i%2 == 0 ){

                    // if it is an even row and a even column
                    if ( j%2 == 0 ){
                        // set the color to light
                        space.setColor(Space.Color.LIGHT);
                    }
                    // if it is an even row and an odd column
                    else {
                        // set the color to dark
                        space.setColor(Space.Color.DARK);
                    }
                }
                // if it is an odd row
                else {
                    // if it is an odd row and an odd column
                    if ( j%2 != 0 ){
                        // set the color to light
                        space.setColor(Space.Color.LIGHT);
                    }
                    // if it is an odd row and an even column
                    else {
                        // set the color to dark
                        space.setColor(Space.Color.DARK);
                    }
                }
                // add the list of spaces to the row
                spaces.add(space);
            }
            // add the row to the list of rows
            Row row = new Row(i, spaces);
            rows.add(row);
        }
        // return the boardview of the board
        BoardView boardView = new BoardView(rows);
        return boardView;
    }

    /**
     *
     * @param boardView the reference of the board filled with spaces
     */
    public void addPiece( BoardView boardView ){
        // get the rows
        List<Row> rows = boardView.getRows();
        int i = 0;

        // for every row, add pieces to every proper space
        for ( Row row : rows ){
            // get the spaces for this row
            List<Space> spaces = row.getSpaces();
            for ( Space space : spaces ){
                // if the row should be filled with red pieces
                if ( i>=5 ){
                    // if the space is a dark square
                    if ( space.isValid() ){
                        // add a red piece to the space on the board
                        Piece piece = new Piece(RED);
                        space.setPiece(piece);
                    }
                }
                // if the row should be filled with white pieces
                else if (i<=2){
                    // if the space is a dark square
                    if ( space.isValid() ){
                        // add a white piece to the space on the board
                        Piece piece = new Piece(WHITE);
                        space.setPiece(piece);
                    }
                }
            }
            i++;
        }
    }

    /**
     * flip the board so that the other player has a correct facing board
     * @param boardView the filled out board for a game that is facing the original way
     * @return the flipped board
     */
    public BoardView flipBoard( BoardView boardView ){
        Row[] rows = new Row[8];
        List<Row> rows1 = boardView.getRows();
        List<Row> newRow = new ArrayList<>();

        // start at the other end of the board
        int i = 7;
        // for every row in the original board
        for ( Row row : rows1 ){
            Space[] spaces = new Space[8];
            List<Space> spaces1 = row.getSpaces();
            List<Space> newSpaces = new ArrayList<>();
            int j=7;

            // for every space in the original row
            for ( Space space : spaces1 ){
                spaces[j] = space;
                j--;
            }
            for ( int k=0 ; k<8 ; k++ ){
                newSpaces.add(spaces[k]);
            }
            // make a new row  with the correct index
            Row newRow1 = new Row(row.getIndex(), newSpaces);
            rows[i] = newRow1;
            i--;
        }
        for (int j=0 ; j<8 ; j++){
            newRow.add(rows[j]);
        }

        // return the flipped board
        BoardView boardView1 = new BoardView(newRow);
        return boardView1;
    }
}
