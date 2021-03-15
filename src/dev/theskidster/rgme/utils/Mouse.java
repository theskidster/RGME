package dev.theskidster.rgme.utils;

import dev.theskidster.rgme.main.Window;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public final class Mouse {
    
    public float scrollValue;
    
    public boolean clicked;
    public boolean scrolled;
    
    public String button = "";
    public Vector2f cursorPos = new Vector2f();
    
    private final Observable observable;
    
    public Mouse(Window window) {
        observable = new Observable(this);
        observable.properties.put("cursorShape", GLFW_ARROW_CURSOR);
        observable.addObserver(window);
    }
    
    public void setCursorShape(int cursorShape) {
        observable.notifyObservers("cursorShape", cursorShape);
    }
    
}