package com.webcheckers.model;

/**
 * Class that defines a move, copies the definition found in move.js
 */
public class Move {
    // These are public to facilitate easy GSON stuff, unluckily for us, this is not
    // C# and properties are not a language defined existence.
    public Position start;
    public Position end;

    /**
     * This flips the positions around, used for backing up moves
     */
    public void reverse() {
        Position intermediate = start;
        start = end;
        end = intermediate;
    }
}