package dev.theskidster.rgme.commands;

import dev.theskidster.rgme.scene.TestObject;

/**
 * @author J Hoffman
 * Created: Mar 15, 2021
 */

public class MoveShape extends Command {

    private float x;
    private float y;
    private float z;
    
    private float prevX;
    private float prevY;
    private float prevZ;
    
    private TestObject object;
    
    public MoveShape(TestObject object, float x, float y, float z) {
        this.object = object;
        this.x      = x;
        this.y      = y;
        this.z      = z;
    }
    
    @Override
    public void execute() {
        prevX = object.getPosition().x;
        prevY = object.getPosition().y;
        prevZ = object.getPosition().z;
        
        object.setPosition(x, y, z);
    }

    @Override
    public void undo() {
        object.setPosition(prevX, prevY, prevZ);
    }

}