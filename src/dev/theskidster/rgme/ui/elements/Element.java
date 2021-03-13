package dev.theskidster.rgme.ui.elements;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Mouse;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public abstract class Element {

    protected float xOffset;
    protected float yOffset;
    
    protected boolean prevPressed;
    protected boolean currPressed;
    public boolean hovered;
    public boolean clicked;
    public boolean remove;
    
    public abstract void update(float parentPosX, float parentPosY, Mouse mouse);
    public abstract void render(Program uiProgram, Background background, FreeTypeFont font);
    
}