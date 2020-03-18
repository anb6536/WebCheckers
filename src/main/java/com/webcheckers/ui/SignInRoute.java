package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

public class SignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(SignInRoute.class.getName());
    TemplateEngine engine;

    public SignInRoute(TemplateEngine engine) {
        this.engine = engine;

        LOG.config("SignInRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response){
        Map<String, Object> vm = new HashMap<>();
        return engine.render(new ModelAndView(vm, "signin.ftl"));
    }

}