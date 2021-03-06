<!--- geometry: margin=1in --->
# WebCheckers Design Documentation

<!--- > _The following template provides the headings for your Design
> Documentation.  As you edit each section make sure you remove these
> commentary 'blockquotes'; the lines that start with a > character
> and appear in the generated PDF in italics._ --->

## Team Information
* Team name: TEAM-A
* Team members
  * Aahish Balimane
  * Aaron Peterham
  * Hugh Gallo
  * AbuBakr Ghaznavi
  * Brenton Cousins

## Executive Summary

This is a Web based Application for the WebChecker Game. Players can log into their account and play the game of WebChecker with anyone who is present in the lobby. The game of WebCheckers is a board game where each player is given 12 pieces of color. The goal of the game is to eliminate all the opponent pieces present on the game board.

### Purpose
<!--- > _Provide a very brief statement about the project and the most
> important user group and user goals._ --->

Users of the WebCheckers application sign in in order to challenge other players to and play games of Checkers.

### Glossary and Acronyms
> _Provide a table of terms and acronyms._

| Term | Definition |
|------|------------|
| VO | Value Object |
| MVP | Minimum Viable Product |

## Requirements

This section describes the features of the application.

> Players must be able to sign-in to the application and play a game against other players. By using drag and drop capabilities,
> players can move pieces through the board, capturing or jumping over opponents' pieces.
> If players reach their opponent's end of the board, they will be "kinged", granting that piece the ability to move both forward and backward.
> While playing, either player can resign, ending the game and forfeiting the match.
> Players will also be able to play against a computer opponent and be able to watch replays of their matches.

### Definition of MVP
> Each player must be able to sign in before playing a game and be able to sign out when finished playing.
> Two players must be able to play a game of checkers based upon the American rules. Either player may choose to resign at any point,
> which ends the game.

### MVP Features
<!--- > _Provide a list of top-level Epics and/or Stories of the MVP._ --->

* #### Player Sign-in
    * As a player, I want to be able to sign-in so that I can play a game of checkers
* #### Player Sign-out
    * As a player who is signed into WebCheckers, I want to be able to sign out so that I am not placed in any new games
* #### Start a Game
    * As a Player, I want to start a game so that I can play checkers against an opponent. 
* #### Single Move
    * As a Player, I want to be able to move my pieces diagonally away from me so that I can advance towards the far end of the board
* #### Backwards Move
    * As a player with a kinged piece, I want to complete a simple move, a single jump move, or a multiple jump move with kinged pieces but towards my side of the board so I can reposition or capture opponent’s pieces with my kinged piece.
* #### Jump Moves
    * As a Player, I want to make a jump move so that I can advance forward.
    * ##### Single Jump Move
        * As the player, I want to be able to move an eligible piece in a jump over opponents diagonally towards the opponent so I can capture an enemy piece.
    * ##### Multiple Jump Move
        * As the player, I want to move an eligible piece in multiple jumps over opponent’s pieces diagonally in a recursive fashion towards the opponent so I can capture multiple enemy pieces.
* #### Resignation
    * The player must be able to resign any game he or she is playing.
    * ##### Win 
        * As a player who won the game, I want to see the results of a completed game so I can know who won.
    * ##### Lose
        * As a player who lost the game, I want to see the results of a completed game so I can know who won.
 * #### King
    * As a player with a piece in the final row, I want to be forced to king my piece when the piece reaches the row closest to my opponent, so I follow the rules of the game.
    
    

### Roadmap of Enhancements
<!--- > _Provide a list of top-level features in the order you plan to consider them._ --->

* #### Spectator Mode
    * The player should be able to watch games of other players playing WebCheckers.


## Application Domain

This section describes the application domain.

<!-- Below are the entities involved in the Checkers application domain. In every game of Checkers
there always exists two players who control pieces that occupy squares on the board.
There is a logical concept of a "game" between these two players that is modeled in the diagram
for cohesion. --> 

![The WebCheckers Domain Model](Sprint%204%20Domain%20Model.png)

<!--- > _Provide a high-level overview of the domain for this application. You
> can discuss the more important domain entities and their relationship
> to each other._ --->

The WebCheckers application consists of several related elements to form the application. 
The Game is the central element. A game consists of two players (who can be either an AI or Human).
These players manipulate pieces that can occupy the 64 squares on the board. These pieces can either be
normal pieces or king pieces. Each player takes turn making their moves. While manipulating the pieces, the moves made
have to obey the rules of the game which are the standard American rules. Logged in players are able
to watch games of other players. At any time, players are able to leave their matches or stop watching other players' games.

## Architecture and Design

This section describes the application architecture.

### Summary

The following Tiers/Layers model shows a high-level view of the webapp's architecture.

![The Tiers & Layers of the Architecture](architecture-tiers-and-layers.png)

As a web application, the user interacts with the system using a
browser.  The client-side of the UI is composed of HTML pages with
some minimal CSS for styling the page.  There is also some JavaScript
that has been provided to the team by the architect.

The server-side tiers include the UI Tier that is composed of UI Controllers and Views.
Controllers are built using the Spark framework and View are built using the FreeMarker framework.  The Application and Model tiers are built using plain-old Java objects (POJOs).

Details of the components within these tiers are supplied below.


### Overview of User Interface

This section describes the web interface flow; this is how the user views and interacts
with the WebCheckers application.

![The WebCheckers Web Interface Statechart](web-interface-s4.png)

<!--- > _Provide a summary of the application's user interface.  Describe, from
> the user's perspective, the flow of the pages in the web application._ --->

A Player starts from having no connection to the Webserver to connecting through the home page.
Once a user is connected to the home page, they are presented with some welcome information about the application.
They can either choose to sign in at this point, or exit the application and terminate their connection. If a player chooses
to sign in, they will be presented with a form page to enter a valid user name. If the username is invalid they will 
return to this page with an error message telling them the format of a proper username. If the username is valid, they will
return to the home page being able to select from the other players to challenge to a game. Additionally, players will be able to spectate
games between other players. While spectating, the page will refresh whenever the game switches turns between the players. The game is always
seen from the perspective of the Red player. The spectator has the ability to stop watching the game whenever they please. Once a player,
enters a game they will enter the game page which displays the board and pieces. When either player wins or loses, they can go back to the home page and either
select another game or sign-out. While playing the game, the client sends a request to the server to verify which player's turn is it
The player whose turn it is makes a move which is then checked by the server. If the move fails, the piece is moved back to its previous location.
Otherwise, the player has the option to submit their move, or to backup their move in order to make an alternative one.
When the player submits their move, the board updates appropriately, the turns switch and the other player undergoes the same process. This continues until either a player wins
or a player resigns.


### UI Tier
<!--- > INSTRUCTIONS FOR TIERS 
 _Provide a summary of the Server-side UI tier of your architecture.
> Describe the types of components in the tier and describe their
> responsibilities.  This should be a narrative description, i.e. it has
> a flow or "story line" that the reader can follow._

> _At appropriate places as part of this narrative provide one or more
> static models (UML class structure or object diagrams) with some
> details such as critical attributes and methods._ --->

<!--- >
> _You must also provide any dynamic models, such as statechart and
> sequence diagrams, as is relevant to a particular aspect of the design
> that you are describing.  For example, in WebCheckers you might create
> a sequence diagram of the `POST /validateMove` HTTP request processing
> or you might show a statechart diagram if the Game component uses a
> state machine to manage the game._

> _If a dynamic model, such as a statechart describes a feature that is
> not mostly in this tier and cuts across multiple tiers, you can
> consider placing the narrative description of that feature in a
> separate section for describing significant features. Place this after
> you describe the design of the three tiers._
--->

The server side of the application contains many components that communicate
information between the user client and the server. Many of these components are 
POST/GET routes used to send and receive information about the user's state.
These routes largely communicate through the API tier. The SignInRoutes are 
used for verifying correct login info. The GameRoutes are used for sending and retrieving
information to assign players to different games. The checkTurn, validateMove, and 
submitTurn routes all follow their namesakes and facilitate the consistent state between the application
and the users. The makeMove route allows for the game to be updated by a move and for turns to switch.
The spectator routes all handle instances of users watching others play games of WebCheckers.
The spectate route allows a spectator to watch a game. The /spectator/stopWatching route allows for users to stop watching games
and spectator/checkTurn makes sure that the spectator can see the right turns being made. A dynamic model of a game can be seen below
![Game statechart](statechart_game.png)





### Application Tier
<!--- > _Provide a summary of the Application tier of your architecture. This
> section will follow the same instructions that are given for the UI
> Tier above._ --->
>The application tier consists solely of the PlayerLobby class.
This class is responsible for maintaining the state of the multiple players who play the game.
It is used to pair players up in games, take them out of games and to assign which player has what color pieces.
It is used heavily for the model and the application tier of the program.


### Model Tier
<!--- > _Provide a summary of the  tier of your architecture. This
> section will follow the same instructions that are given for the UI
> Tier above._ --->
>The model tier consists of the classes that represent the state of the WebCheckers game. The BoardView, Board,
Row, Space and classes that relate to it are responsible for the state of the board. The Player and Piece classes are used for 
representing the user and the piece that the player has respectively. The Game class is responsible for coordinating
the states of all these classes. The Position class is responsible for storing a row and a column that may be used to represent
a location on the checkers board and are used by the Move class. Having a start position and an end position, the Move class
essentially represents a change in position (a move done by the player). The information from the move class is used to verify
the validity of the move using the MoveValidator class. The MoveValidator class largely uses static methods that check different aspects of the move that is passed into it
(which player is moving, which direction do they intend on moving, which piece, etc.) all of this information is used to validate the move and evaluate what kinds of changes the move would 
cause to the board. That information is stored in a MoveInformation class largely responsible for determining if a jump move is made and which pieces are removed as a result. This kind of information
combined with the Boolean checks from MoveValidator form the Pair classes we use to make and validate changes to the board.

![Sprint 4 Model](Sprint4-Model-Class-Diagram.png)

### Design Improvements
<!--- > _Discuss design improvements that you would make if the project were
> to continue. These improvement should be based on your direct
> analysis of where there are problems in the code base which could be
> addressed with design changes, and describe those suggested design
> improvements. After completion of the Code metrics exercise, you
> will also discuss the resutling metric measurements.  Indicate the
> hot spots the metrics identified in your code base, and your
> suggested design improvements to address those hot spots._
---> 
>If work was to continue on this project some of the design of the model would be improved.
Specifically the MoveValidator class. This class has a large responsibility while some of the work should
have been delegated to smaller subclasses in order to have more organized code. Additionally some of the methods within this class
could have been generalized in order to make writing some of the other methods easier, some of the classes in the model went completely unused.
Additionally, some of the game model could be redesigned to make retrieving whose turn it is a little easier. This would help with enforcing single responsibility. Although our development team
was not responsible for how it was implemented, we could have improved on the refresh rate for the application. For example, the board could have updated
through AJAX calls rather than refreshing the entire page. This would have made a significant visual improvement on our application.
>
## Testing
> _This section will provide information about the testing performed
> and the results of the testing._

### Acceptance Testing
<!--- > _Report on the number of user stories that have passed all their
> acceptance criteria tests, the number that have some acceptance
> criteria tests failing, and the number of user stories that
> have not had any testing yet. Highlight the issues found during
> acceptance testing and if there are any concerns._ --->
> All of our classes passed acceptance criteria tests as they met the business
>needs for the application. The functional and non functional requirements set for the stories
>were all met. The Unit Tests for some of the classes, especially in the UI and API tier are lacking
> but we heavily tested the running of the application manually.

### Unit Testing and Code Coverage
<!--- > _Discuss your unit testing strategy. Report on the code coverage
> achieved from unit testing of the code base. Discuss the team's
> coverage targets, why you selected those values, and how well your
> code coverage met your targets. If there are any anomalies, discuss
> those_ --->



![Code Coverage Percentage](code_coverage.png)
> In the beginning our unit testing strategy was to essentially complete the sprint
>and then write our unit tests allowing us to fix any bugs we can across towards the end of the sprint.
> We then shifted our strategy to doing more unit testing as we went along so we could verify that 
>certain components of the application worked before continuing on dependant components. Our target for code
>coverage was about 70% - 80% coverage. If there was a specific reason why a class or component would
>have lower coverage, that means we don't hold that class to the same standard. The UI tier has GetGameRoute as an anomaly
>because the branches that would be covered aren't due to other classes being used to help test those branches.

#### Model Coverage
![Code coverage model](model-code_coverage.png)
> Almost every element in our model was tested to our threshold and beyond.
>The exceptions are the space class and the MoveValidator class. The space class was largely used through the 
>Position class meaning that well tested and well functioning Position class depends heavily on the space class. Since
>position and all of its dependent classes function as expected, it can be assumed that Space works as well. The MoveValidator class
>is thoroughly tested in all of it's used components. There are two large methods that were deprecated in sprints 3 and 4 that went untested which dragged the 
>code coverage down.
>
#### Application Coverage
![Code coverage application](appl-code_coverage.png)
> The PlayerLobby class was well tested although there are some tests lacking for when 
>the secondary player is not present. 
#### UI and API Coverage
![Code coverage ui](ui-code_coverage.png)
![Code coverage api](api-code_coverage.png)
> The GetGameRoute handle method is not that thoroughly tested. This is largely because there are other
> classes that were tested that cover these cases within the UI tier. Additionally WebServer was a class that we were provided with.
>This allows us to assume that this is class is friendly, meaning that extensive testing is not required for this class.
>The API class is well tested except for the SpectateAPIRoutes. This lack of testing is likely a result of the last minute
>bug fixes that were implemented towards the end of the last sprint.

