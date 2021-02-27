package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.elements.Element;
import dev.theskidster.rgme.utils.Mouse;
import java.util.Set;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public abstract class Widget {
    
    public int xPos;
    public int yPos;
    
    public boolean hovered;
    public boolean removeRequest;
    
    Set<Element> elements;
    
    Widget(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
    
    public abstract void update(int viewportWidth, int viewportHeight, Mouse mouse);
    public abstract void render(Program uiProgram, Background background, FreeTypeFont font);
    
}