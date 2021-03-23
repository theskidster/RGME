package dev.theskidster.rgme.scene;

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
        index     = indexLimit++;
        this.name = (name.equals("World Light")) ? name : name + " (" + index + ")";
    }
    
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