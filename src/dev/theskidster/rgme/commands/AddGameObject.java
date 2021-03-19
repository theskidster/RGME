package dev.theskidster.rgme.commands;

import dev.theskidster.rgme.scene.GameObject;
import java.util.Map;

/**
 * @author J Hoffman
 * Created: Mar 18, 2021
 */

public class AddGameObject extends Command {
    
    public AddGameObject(Map<Integer, GameObject> collection, GameObject gameObject) {
        System.out.println("AddGameObject command created");
    }
    
    @Override
    void execute() {
        System.out.println("AddGameObject command executed");
    }

    @Override
    void undo() {
    }
    
}