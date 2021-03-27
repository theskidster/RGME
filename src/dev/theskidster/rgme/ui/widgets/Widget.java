package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.ui.LogicLoop;
import dev.theskidster.rgme.ui.Relocatable;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import org.joml.Vector2f;

/**
 * @author J Hoffman
 * Created: Mar 13, 2021
 */

public abstract class Widget implements LogicLoop, Relocatable {
    
    private final boolean[] prevClicked = new boolean[2];
    private final boolean[] currClicked = new boolean[2];
    protected boolean remove;
    
    protected final Rectangle bounds;
    
    protected Widget(float xPos, float yPos, float width, float height) {
        bounds = new Rectangle(xPos, yPos, width, height); 
    }
    
    protected boolean clickedOnce(Rectangle rectangle, Mouse mouse) {
        if(!rectangle.contains(mouse.cursorPos)) return false;
        
        int index = (rectangle.equals(bounds)) ? 0 : 1;
        
        prevClicked[index] = currClicked[index];
        currClicked[index] = mouse.clicked;
        
        return (prevClicked[index] != currClicked[index] && !prevClicked[index]);
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