package dev.theskidster.rgme.commands;

import dev.theskidster.rgme.scene.GameObject;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 30, 2021
 */

public class RotateGameObject extends Command {

    private final boolean prevRotSetImplictly;
    
    private final Vector3f currRot = new Vector3f();
    private final Vector3f prevRot = new Vector3f();
    private final GameObject object;
    
    public RotateGameObject(GameObject object, Vector3f currRot) {
        this.object = object;
        this.currRot.set(currRot);
        
        prevRotSetImplictly = true;
    }
    
    public RotateGameObject(GameObject object, Vector3f prevRot, Vector3f currRot) {
        this.object = object;
        this.prevRot.set(prevRot);
        this.currRot.set(currRot);
        
        prevRotSetImplictly = false;
    }
    
    @Override
    public void execute() {
        if(prevRotSetImplictly) prevRot.set(object.getRotation());
        object.setRotation(currRot.x, currRot.y, currRot.z);
    }

    @Override
    public void undo() {
        object.setRotation(prevRot.x, prevRot.y, prevRot.z);
    }

}