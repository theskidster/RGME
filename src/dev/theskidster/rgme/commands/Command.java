package dev.theskidster.rgme.commands;

/**
 * @author J Hoffman
 * Created: Mar 12, 2021
 */

public abstract class Command {
    
    abstract void execute();
    abstract void undo();
    
}