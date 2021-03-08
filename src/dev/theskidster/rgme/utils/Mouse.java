package dev.theskidster.rgme.utils;

import org.joml.Vector2i;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public final class Mouse {
    
    private int prevCursorShape;
    public int scrollValue;
    
    private final long windowHandle;
    private long cursorHandle;
    
    public boolean clicked;
    public boolean scrolled;
    
    public String button = "";
    public Vector2i cursorPos = new Vector2i();
    
    public Mouse(long windowHandle) {
        this.windowHandle = windowHandle;
        
        cursorHandle = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        glfwSetCursor(windowHandle, cursorHandle);
    }
    
    public void setCursorShape(int shape) {
        if(shape != prevCursorShape) {
            glfwDestroyCursor(cursorHandle);
            cursorHandle = glfwCreateStandardCursor(shape);
            glfwSetCursor(windowHandle, cursorHandle);
            
            prevCursorShape = shape;
        }
    }
    
}