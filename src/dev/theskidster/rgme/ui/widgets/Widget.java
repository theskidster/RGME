package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.ui.Renderable;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import org.joml.Vector2f;

/**
 * @author J Hoffman
 * Created: Mar 13, 2021
 */

public abstract class Widget implements Renderable {
    
    private boolean prevPressed;
    private boolean currPressed;
    protected boolean remove;
    
    protected final Rectangle bounds;
    
    protected Widget(float xPos, float yPos, float width, float height) {
        bounds = new Rectangle(xPos, yPos, width, height); 
    }
    
    protected boolean clickedOnce(Rectangle rectangle, Mouse mouse) {
        prevPressed = currPressed;
        currPressed = mouse.clicked;
        
        return (prevPressed != currPressed && !prevPressed) && rectangle.contains(mouse.cursorPos);
    }
    
    public boolean removalRequested() {
        return remove;
    }
    
    public boolean hovered(Vector2f cursorPos) {
        return bounds.contains(cursorPos);
    }
    
    public void remove() {
        remove = true;
    }
    
}