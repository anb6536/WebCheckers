package com.webcheckers.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UserModel {
    private static final Logger LOG = Logger.getLogger(UserModel.class.getName());

    public static Map<String, PlayerTypes.types> playersWithTypes = new HashMap<String, PlayerTypes.types>();
    List<Player> loggedInPlayers;

    public UserModel() {

        LOG.fine("userModel done");
    }

}