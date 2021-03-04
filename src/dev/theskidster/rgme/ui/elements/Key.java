package dev.theskidster.rgme.ui.elements;

/**
 * @author J Hoffman
 * Created: Mar 3, 2021
 */

public class Key {
    
    final char c;
    final char C;

    Key(char c, char C) {
        this.c = c;
        this.C = C;
    }

    char getChar(boolean shiftHeld) {
        return (!shiftHeld) ? c : C;
    }
    
}