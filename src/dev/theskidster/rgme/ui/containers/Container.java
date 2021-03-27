package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.widgets.Widget;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.List;
import org.joml.Vector2f;

/**
 * @author J Hoffman
 * Created: Mar 13, 2021
 */

public abstract class Container extends Widget {
    
    protected String title;
    protected Icon icon;
    protected Rectangle titleBar;
    
    public List<Widget> widgets;
    
    protected Container(float xPos, float yPos, float width, float height, String title, int cellX, int cellY) {
        super(xPos, yPos, width, height);
        this.title = title;
        
        icon = new Icon(20, 20);
        icon.setSubImage(cellX, cellY);
        
        titleBar = new Rectangle(xPos, yPos, width, 40);
    }
    
    protected void renderTitleBar(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(titleBar, Color.RGME_LIGHT_GRAY, uiProgram);
        icon.render(uiProgram);
        font.drawString(title, bounds.xPos + 40, bounds.yPos + 26, 1, Color.RGME_WHITE, uiProgram);
    }
    
    protected void relocateTitleBar() {
        titleBar.xPos  = bounds.xPos;
        titleBar.yPos  = bounds.yPos;
        titleBar.width = bounds.width;
        
        icon.position.set(bounds.xPos + 10, bounds.yPos + 30);
    }
    
    protected boolean widgetHovered(Vector2f cursorPos) {
        return widgets.stream().anyMatch(widget -> widget.hovered(cursorPos));
    }
    
}