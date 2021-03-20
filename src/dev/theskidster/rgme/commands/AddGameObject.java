package dev.theskidster.rgme.commands;

import dev.theskidster.rgme.scene.GameObject;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author J Hoffman
 * Created: Mar 18, 2021
 */

public class AddGameObject extends Command {
    
    private Entry<Integer, GameObject> entry;
    private Map<Integer, GameObject> collection;
    
    public AddGameObject(Map<Integer, GameObject> collection, GameObject gameObject) {
        this.collection = collection;
        entry = new SimpleEntry(collection.size(), gameObject);
    }
    
    @Override
    void execute() {
        collection.put(entry.getKey(), entry.getValue());
    }

    @Override
    void undo() {
        collection.remove(entry.getKey());
    }
    
}