package dev.theskidster.rgme.commands;

import dev.theskidster.rgme.scene.GameObject;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 15, 2021
 */

public class MoveObject extends Command {

    private final Vector3f currPos = new Vector3f();
    private final Vector3f prevPos = new Vector3f();
    private final GameObject object;
    
    public MoveObject(GameObject object, Vector3f currPos) {
        this.object  = object;
        this.currPos.set(currPos.x, currPos.y, currPos.z);
    }
    
    @Override
    public void execute() {
        prevPos.set(object.getPosition());
        object.setPosition(currPos.x, currPos.y, currPos.z);
    }

    @Override
    public void undo() {
        object.setPosition(prevPos.x, prevPos.y, prevPos.z);
    }

}