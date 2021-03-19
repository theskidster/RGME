package dev.theskidster.rgme.commands;

import dev.theskidster.rgme.scene.GameObject;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author J Hoffman
 * Created: Mar 18, 2021
 */

public class DeleteGameObject extends Command {

    private Entry<Integer, GameObject> entry;
    private Map<Integer, GameObject> collection;
    
    public DeleteGameObject(Map<Integer, GameObject> collection) {
        this.collection = collection;
    }
    
    @Override
    void execute() {
        
    }

    @Override
    void undo() {
        
    }
    
}