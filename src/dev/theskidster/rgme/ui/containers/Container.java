package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.ui.widgets.Widget;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.Map;

/**
 * @author J Hoffman
 * Created: Mar 13, 2021
 */

public abstract class Container extends Widget {
    
    protected String title;
    protected Icon icon;
    protected Rectangle titleBar;
    
    protected Map<String, Widget> widgets;
    
    protected Container(float xPos, float yPos, float width, float height) {
        super(xPos, yPos, width, height); 
    }
    
    protected Container(float xPos, float yPos, float width, float height, String title, int cellX, int cellY) {
        this(xPos, yPos, width, height);
        this.title = title;
        
        icon = new Icon(20, 20);
        icon.setSubImage(cellX, cellY);
        
        titleBar = new Rectangle(xPos, yPos, width, 40);
    }
    
}