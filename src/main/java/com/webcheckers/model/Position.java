package com.webcheckers.model;

/**
 * Helper class for defining a move, is modeled after position.js
 */
public class Position {
    // These are public to facilitate easy GSON stuff, unluckily for us, this is not
    // C# and properties are not a language defined existence.
    public int row;
    public int column;
}