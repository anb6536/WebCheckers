<!DOCTYPE html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <title>Web Checkers | Sign in</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<div class="page">
<h1> Web Checkers | Sign In </h1>
<div class="body">
<#include "message.ftl"/>
<form id="signin" method="post" action="/signin">
    <div>
        <label for="username"> Enter your username: </label>
        <input type="text" name="username" id="username" maxlength="20" required />
    </div>
    <div>
        <input type="submit" value="Sign in" />
    </div>
    <!--<div>
    <label for="pass">Enter your password: </label>
    <input type="password" name="pass" id="pass" required>
</div>
-->
</form>
</div>
</div>