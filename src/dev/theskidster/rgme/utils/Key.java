package dev.theskidster.rgme.utils;

/**
 * @author J Hoffman
 * Created: Mar 21, 2021
 */

public class Key {
    
    private final char c;
    private final char C;

    Key(char c, char C) {
        this.c = c;
        this.C = C;
    }

    public char getChar(boolean shiftHeld) {
        return (!shiftHeld) ? c : C;
    }
    
}