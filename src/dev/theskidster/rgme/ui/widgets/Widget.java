package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.elements.Element;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public abstract class Widget implements PropertyChangeListener {
    
    public boolean hovered;
    public boolean removeRequest;
    
    protected String title;
    protected Rectangle bounds;
    protected Rectangle titleBar;
    protected Icon icon;
    
    protected Set<Element> elements;
    
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
    
    public abstract void update(int viewportWidth, int viewportHeight, Mouse mouse);
    public abstract void render(Program uiProgram, Background background, FreeTypeFont font);
    
    @Override
    public abstract void propertyChange(PropertyChangeEvent evt);
    
    public boolean hasHoveredElement() {
        return elements.stream().anyMatch(element -> element.hovered);
    }
    
}