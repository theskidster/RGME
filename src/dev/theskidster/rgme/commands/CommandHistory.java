package dev.theskidster.rgme.commands;

/**
 * @author J Hoffman
 * Created: Mar 15, 2021
 */

public class CommandHistory {

    private int index;
    private final int LIMIT = 30;
    
    private final Command[] history = new Command[LIMIT];
    
    public void executeCommand(Command command) {
        command.execute();
        
        Command[] tempHistory = new Command[LIMIT];
        
        for(int c = 0; c < history.length; c++) {
            int offset = (c + index) - 1;
            
            if(offset < history.length && offset >= 0) {
                tempHistory[c] = history[offset];
            } else {
                tempHistory[c] = null;
            }
        }
        
        System.arraycopy(tempHistory, 0, history, 0, history.length);
        
        index = 0;
        history[index] = command;
    }
    
    public void undoCommand() {
        if(index < history.length) {
            if(index >= 0 && history[index] != null) {
                history[index].undo();
                index++;
            }
        }
    }
    
    public void redoCommand() {
        if(index >= 0) {
            if(index <= history.length && index - 1 != -1) {
                index--;
                history[index].execute();
            }
        }
    }
    
}