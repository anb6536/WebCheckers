package com.webcheckers.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
// TODO REMOVE THIS.
public class UserModel {
    private static final Logger LOG = Logger.getLogger(UserModel.class.getName());

    public static Map<String, Piece.color> playersWithTypes = new HashMap<String, Piece.color>();
    List<Player> loggedInPlayers;

    public UserModel() {

        LOG.fine("userModel done");
    }

}