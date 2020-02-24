package com.webcheckers.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import spark.Request;
import spark.Response;
import spark.Route;

public class SignInApiRoute implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String username = request.queryParams("username");
        Map<String, String> stuff = request.params();
        stuff.forEach((t, t1) -> {
            System.out.println(t);
        });
        HttpServletRequest req = request.raw();
        request.session().attribute("UserAttrib", username);
        response.redirect("/");
        // TODO Auto-generated method stub
        return response;
    }

}