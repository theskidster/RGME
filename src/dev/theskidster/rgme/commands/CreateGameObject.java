package dev.theskidster.rgme.commands;

import dev.theskidster.rgme.scene.GameObject;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author J Hoffman
 * Created: Mar 18, 2021
 */

public class CreateGameObject extends Command {
    
    private final Entry<Integer, GameObject> entry;
    private final Map<Integer, GameObject> collection;
    
    public CreateGameObject(Map<Integer, GameObject> collection, GameObject gameObject) {
        this.collection = collection;
        entry = new SimpleEntry(gameObject.index, gameObject);
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