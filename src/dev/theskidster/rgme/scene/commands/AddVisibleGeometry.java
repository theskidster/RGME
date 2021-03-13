package dev.theskidster.rgme.scene.commands;

import dev.theskidster.rgme.scene.Scene;
import dev.theskidster.rgme.ui.Command;

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
        System.out.println("add geometry");
    }

}