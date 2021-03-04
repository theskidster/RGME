package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.elements.Element;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.Set;
import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public abstract class Widget {
    
    public boolean hovered;
    public boolean removeRequest;
    
    protected Rectangle bounds;
    
    protected Set<Element> elements;
    
    Widget(int xPos, int yPos, int width, int height) {
        bounds = new Rectangle(xPos, yPos, width, height);
    }
    
    protected void resetMouseShape(Mouse mouse) {
        hovered = bounds.contains(mouse.cursorPos);
        
        if(!elements.stream().anyMatch(element -> element.hovered)) {
            mouse.setCursorShape(GLFW_ARROW_CURSOR);
        }
    }
    
    public abstract void update(int viewportWidth, int viewportHeight, Mouse mouse);
    public abstract void render(Program uiProgram, Background background, FreeTypeFont font);
    
}