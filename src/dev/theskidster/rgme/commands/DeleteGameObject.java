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
    private final Map<Integer, GameObject> collection;
    
    public DeleteGameObject(Map<Integer, GameObject> collection, GameObject gameObject) {
        this.collection = collection;
        
        for(Entry e : collection.entrySet()) {
            if(gameObject.equals(e.getValue())) entry = e;
        }
    }
    
    @Override
    void execute() {
        collection.remove(entry.getKey());
    }

    @Override
    void undo() {
        collection.put(entry.getKey(), entry.getValue());
    }
    
}