package com.webcheckers.ui;

import java.util.*;
import java.util.function.LongBinaryOperator;
import java.util.logging.Logger;

import com.webcheckers.appl.PlayerLobby;
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
    List<String> playerList = null;
    // currentUser shenanigans for logins
    if (request.session().attribute("UserAttrib") != null) {
      vm.put("currentUser", request.session().attribute("UserAttrib"));
      // Assign signedIn value to be true once player gets a UserAttrib
      vm.put("signedIn", true);
      playerList = new ArrayList<String>(playerLobby.getLoggedInPlayers());
      // The player representing the current user
      Player currentPlayer =  request.session().attribute("UserAttrib");
      // Remove the current player from the list of players they could play against
      if (playerList != null && currentPlayer != null) {
        playerList.remove(currentPlayer.getname());
      }
      vm.put("readyPlayers", playerList);
    } else {
      // Boolean value if the player has signed in or not
      vm.put("signedIn", true);// Get the list of players to render

    }



    // render the View
    return templateEngine.render(new ModelAndView(vm, "home.ftl"));
  }
}
