package com.webcheckers.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  private final TemplateEngine templateEngine;
  private PlayerLobby playerLobby;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP
   * requests.
   *
   * @param templateEngine the HTML template rendering engine
   */
  public GetHomeRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //

    // Getting access to the application's player lobby
    this.playerLobby = playerLobby;
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request  the HTTP request
   * @param response the HTTP response
   *
   * @return the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetHomeRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    // display a user message in the Home page
    vm.put("message", WELCOME_MSG);
    // Getting and displaying number of logged in users
    int numLoggedIn = playerLobby.getNumLoggedInPlayers();
    vm.put("numLoggedIn", numLoggedIn);
    // Have a default signedIn value has false
    vm.put("signedIn", false);

    // Declare list of potential players
    List<Player> playerList = null;

    // currentUser shenanigans for logins
    if (request.session().attribute("UserAttrib") != null) {
      vm.put("currentUser", request.session().attribute("UserAttrib"));

      // Assign signedIn value to be true once player gets a UserAttrib
      vm.put("signedIn", true);
      playerList = new ArrayList<Player>(playerLobby.getLoggedInPlayers());

      // The player representing the current user
      Player currentPlayer = request.session().attribute("UserAttrib");
      String opponentName = request.queryParams("opponent");
      Player opponent = playerLobby.getPlayer(opponentName);
      vm.put("opponent", opponent);
      request.session().attribute("opponent", opponent);

      // Remove the current player from the list of players they could play against
      if (!playerList.isEmpty() && currentPlayer != null) {
        playerList.remove(currentPlayer);
      }
      vm.put("readyPlayers", playerList);

      // if the current player is in a game and we aren't already there
      if (currentPlayer != null && currentPlayer.isInGame()) {
        response.redirect(WebServer.GAME_URL);
        return null;
      }

    } else {

      // Boolean value if the player has signed in or not
      // the should not be signed in
      vm.put("signedIn", false);// Get the list of players to render
    }
    vm.put("opponents", playerLobby.getOpponents());

    if (request.queryParams("error") != null) {
      vm.put("message", Message.error("This player is already in a game"));
      return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    }

    if (request.queryParams("errorS") != null) {
      vm.put("message", Message.error("This player is spectating another Match"));
      return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    }
    // render the View
    return templateEngine.render(new ModelAndView(vm, "home.ftl"));
  }
}
