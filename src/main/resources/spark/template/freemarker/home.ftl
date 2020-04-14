<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />
    <div>

        <p>
        There are <#if numLoggedIn gt 0 >
            ${numLoggedIn}
        <#else> no
        </#if>
        players logged in
        </p>

    </div>
    <#if finishedGame??>
    <div>
            <p>
            You recently finished a game and ${finishedGame}
            </p>
    </div>
    </#if>
    <#if currentUser??>
        <h3>Players Online</h3>
        <ol>
            <#list readyPlayers as readyPlayer>
                <li>
                    <form action = "/game" method="GET">
                        <input type="submit" name="opponent" value="${readyPlayer.getName()}">
                    </form>
                </li>
            </#list>
        </ol>

        <h3>Active Matches</h3>
        <ol>
            <#list opponents as player1, player2>
                <li>
                    ${player1.getName()} vs ${player2.getName()}
                </li>
            </#list>
        </ol>
    </#if>

    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->

  </div>

</div>
</body>

</html>
