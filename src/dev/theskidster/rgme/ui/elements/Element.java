package dev.theskidster.rgme.ui.elements;

import dev.theskidster.rgme.ui.widgets.Renderable;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public abstract class Element implements Renderable {

    protected float xOffset;
    protected float yOffset;
    
    protected boolean prevPressed;
    protected boolean currPressed;
    public boolean hovered;
    public boolean clicked;
    public boolean remove;
    
    protected Element(float xOffset, float yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    
}