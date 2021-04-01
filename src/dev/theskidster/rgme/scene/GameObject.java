package dev.theskidster.rgme.scene;

import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 6, 2021
 */

public abstract class GameObject {
    
    private static int indexLimit;
    public final int index;
    
    protected float scale = 1;
    
    protected boolean visible = true;
    
    protected String name;
    protected Vector3f position;
    protected Vector3f rotation;
    
    public GameObject(String name) {
        index     = indexLimit++;
        this.name = (name.equals("World Light")) ? name : name + " (" + index + ")";
    }
    
    public boolean getVisible()   { return visible; }
    public String getName()       { return name; }
    public Vector3f getPosition() { return position; };
    public Vector3f getRotation() { return rotation; };
    public float getScale()       { return scale; }
    
    public void setVisible(boolean value) {
        visible = value;
    }
    
    public void setName(String value) {
        name = value;
    }
    
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }
    
    public void setRotation(float x, float y, float z) {
        rotation.set(x, y, z);
    }
    
    public void setScale(float value) {
        scale = value;
    }
    
}