package dev.theskidster.rgme.commands;

import dev.theskidster.rgme.scene.GameObject;

/**
 * @author J Hoffman
 * Created: Mar 31, 2021
 */

public class ScaleGameObject extends Command {

    private final float currScale;
    private float prevScale;
    
    private final GameObject object;
    
    public ScaleGameObject(GameObject object, float currScale) {
        this.object    = object;
        this.currScale = currScale;
    }
    
    @Override
    void execute() {
        prevScale = object.getScale();
        object.setScale(currScale);
    }

    @Override
    void undo() {
        object.setScale(prevScale);
    }

}