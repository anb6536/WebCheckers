package com.webcheckers.model;

import java.util.List;
import java.util.logging.Logger;

public class UserModel {
    private static final Logger LOG = Logger.getLogger(UserModel.class.getName());

    List<Player> loggedInPlayers;

    public UserModel() {

        LOG.fine("userModel done");
    }

}