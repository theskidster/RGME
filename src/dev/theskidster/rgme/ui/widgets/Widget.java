package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.ui.elements.Element;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.Map;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public abstract class Widget implements Renderable {
    
    public boolean hovered;
    public boolean remove;
    
    protected String title;
    protected Rectangle bounds;
    protected Rectangle titleBar;
    protected Icon icon;
    
    protected Map<String, Element> elements;
    
    Widget(int xPos, int yPos, int width, int height) {
        bounds = new Rectangle(xPos, yPos, width, height);
    }
    
    Widget(int xPos, int yPos, int width, int height, String title, int iconSpriteX, int iconSpriteY) {
        this(xPos, yPos, width, height);
        this.title = title;
        
        icon     = new Icon(20, 20);
        titleBar = new Rectangle(0, 0, width, 40);
        
        icon.setSubImage(iconSpriteX, iconSpriteY);
    }
    
    protected void updateTitleBarPos(int viewportWidth, int viewportHeight) {
        titleBar.xPos = bounds.xPos;
        titleBar.yPos = bounds.yPos;
        
        icon.position.set(bounds.xPos + 10, bounds.yPos + 30);
    }
    
    public boolean hasHoveredElement() {
        return elements.values().stream().anyMatch(element -> element.hovered);
    }
    
}