package com.webcheckers.model;

/**
 * Class that defines a move, copies the definition found in move.js
 */
public class Move {
    // These are public to facilitate easy GSON stuff, unluckily for us, this is not
    // C# and properties are not a language defined existence.
    public Position start;
    public Position end;

    public static Move createMove(Position start, Position end) {
        Move move = new Move();
        move.start = start;
        move.end = end;
        return move;
    }

    /**
     * This flips the positions around, used for backing up moves
     */
    public void reverse() {
        Position intermediate = start;
        start = end;
        end = intermediate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Move other = (Move) obj;
        if (end == null) {
            if (other.end != null)
                return false;
        } else if (!end.equals(other.end))
            return false;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Move{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public Move invertMove() {
        return createMove(this.start.invertPosition(), this.end.invertPosition());
    }

}