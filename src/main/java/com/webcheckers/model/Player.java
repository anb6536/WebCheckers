package com.webcheckers.model;

public class Player {
    private final String name;
    private boolean inGame;

    public Player(String name) {
        this.name = name;
        this.inGame = false;
    }

    public String getname() {
        return name;
    }

    public void joinedGame(){
        this.inGame = true;
    }

    public void leftGame(){
        this.inGame = false;
    }

    public boolean isInGame(){
        return this.inGame;
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