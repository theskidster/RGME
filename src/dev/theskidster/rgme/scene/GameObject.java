package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.main.Program;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 6, 2021
 */

public abstract class GameObject {
    
    private static int indexLimit;
    public final int index;
    
    protected boolean visible = true;
    
    protected String name;
    protected Vector3f position;
    
    public GameObject(String name) {
        this.index = indexLimit++;
        this.name  = name;
    }
    
    abstract void update();
    abstract void render(Program sceneProgram, Vector3f camPos, Vector3f camUp);
    
    public boolean getVisible()   { return visible; }
    public String getName()       { return name; }
    public Vector3f getPosition() { return position; };
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }
    
}