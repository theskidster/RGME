package dev.theskidster.rgme.ui.tools;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.widgets.Widget;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import org.joml.Vector2f;

/**
 * @author J Hoffman
 * Created: Mar 24, 2021
 */

public abstract class Tool extends Widget {
    
    private int order;
    public final int cellX;
    public final int cellY;
    
    private final float PADDING = 4;
    
    public final String name;
    private Color btnColor;
    private final Icon icon = new Icon(20, 20);
    
    public Tool(String name, int cellX, int cellY) {
        super(0, 0, 30, 30);
        this.name  = name;
        this.cellX = cellX;
        this.cellY = cellY;
        
        icon.setSubImage(cellX, cellY);
    }
    
    protected void updateButton(Vector2f cursorPos, float parentPosX, float parentPosY, int order) {
        this.order  = order + 1;
        bounds.xPos = parentPosX + PADDING;
        bounds.yPos = (parentPosY + ((bounds.height + PADDING) * this.order)) - bounds.height;
        
        icon.position.set(bounds.xPos + 5, bounds.yPos + 25);
        
        
        
        btnColor = (bounds.contains(cursorPos)) ? Color.RGME_LIGHT_GRAY : Color.RGME_MEDIUM_GRAY;
    }
    
    protected void renderButton(Program uiProgram, Background background) {
        background.drawRectangle(bounds, btnColor, uiProgram);
        icon.render(uiProgram);
    }
    
    public abstract Command update(Mouse mouse, GameObject selectedGameObject, float parentPosX, float parentPosY, int order);
    public abstract void render(Program uiProgram, Background background, FreeTypeFont font);
    
}