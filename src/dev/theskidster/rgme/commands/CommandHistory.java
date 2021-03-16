package dev.theskidster.rgme.commands;

/**
 * @author J Hoffman
 * Created: Mar 15, 2021
 */

public class CommandHistory {

    private int index = 4;
    
    private final Command[] history = new Command[5];
    
    public void executeCommand(Command command) {
        command.execute();
        
        for(int c = 0; c < history.length; c++) {
            history[c] = (c == history.length - 1) ? command : history[c + 1];
        }
    }
    
    public void undoCommand() { 
        System.out.println("undo: " + index + " " + history[index]);
        
        if(history[index] != null) {
            history[index].undo();
            if(index > 0) index--;
        }
    }
    
    public void redoCommand() {
        System.out.println("redo: " + index + " " + history[index]);
        
        //TODO: look into this further- make sure it works predictably.
        
        if(index < history.length - 1) index++;
        history[index].execute();
    }
    
}