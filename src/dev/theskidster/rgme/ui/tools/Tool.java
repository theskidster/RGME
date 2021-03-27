package dev.theskidster.rgme.ui.tools;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.Relocatable;
import dev.theskidster.rgme.ui.Renderable;
import dev.theskidster.rgme.ui.containers.ToolBox;
import dev.theskidster.rgme.ui.widgets.Widget;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import java.util.LinkedList;

/**
 * @author J Hoffman
 * Created: Mar 24, 2021
 */

public abstract class Tool extends Widget implements Renderable, Relocatable {
    
    private final int order;
    public final int cellX;
    public final int cellY;
    
    private final float PADDING = 4;
    
    public boolean selected;
    
    public final String name;
    private Color btnColor;
    private final Icon icon = new Icon(20, 20);
    
    protected LinkedList<Widget> widgets;
    
    public Tool(String name, int cellX, int cellY, int order) {
        super(0, 0, 30, 30);
        this.name  = name;
        this.cellX = cellX;
        this.cellY = cellY;
        this.order = order;
        
        icon.setSubImage(cellX, cellY);
    }
    
    protected void updateButton(Mouse mouse, ToolBox toolBox) {
        if(clickedOnce(bounds, mouse)) {
            toolBox.widgets = widgets;
            selected = true;
            btnColor = Color.RGME_BLUE;
        }
        
        if(!selected) {
            btnColor = (bounds.contains(mouse.cursorPos)) ? Color.RGME_LIGHT_GRAY : Color.RGME_MEDIUM_GRAY;
        }
        
        selected = (toolBox.widgets == widgets);
    }
    
    protected void renderButton(Program uiProgram, Background background) {
        background.drawRectangle(bounds, btnColor, uiProgram);
        icon.render(uiProgram);
    }
    
    protected void relocateButton(float parentPosX, float parentPosY) {
        bounds.xPos = parentPosX + PADDING;
        bounds.yPos = (parentPosY + ((bounds.height + PADDING) * order)) - bounds.height;
        
        icon.position.set(bounds.xPos + 5, bounds.yPos + 25);
    }
    
    public abstract Command update(Mouse mouse, ToolBox toolBox, GameObject selectedGameObject);
    
}