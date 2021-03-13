package dev.theskidster.rgme.ui;

/**
 * @author J Hoffman
 * Created: Mar 12, 2021
 */

public abstract class Command {

    public String action;
    
    protected Command(String action) {
        this.action = action;
    }
    
    public abstract void execute();
    
}