package com.webcheckers.model;

public class Player {
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getname() {
        return name;
    }

    @Override
    public boolean equals( Object player ){
        if ( player instanceof Player ){
            Player nPlayer = (Player) player;
            return this.getname().equals(nPlayer.getname());
        }
        else{
            return false;
        }
    }
}