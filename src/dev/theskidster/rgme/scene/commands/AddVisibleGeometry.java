package dev.theskidster.rgme.scene.commands;

import dev.theskidster.rgme.scene.Scene;
import dev.theskidster.rgme.scene.TestObject;
import dev.theskidster.rgme.ui.Command;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 12, 2021
 */

public class AddVisibleGeometry extends Command {

    private Scene scene;
    
    public AddVisibleGeometry(Scene scene) {
        super("Add Visible Geometry");
        
        this.scene = scene;
    }

    @Override
    public void execute() {
        scene.gameObjects.put("bleh", new TestObject(new Vector3f()));
    }

}