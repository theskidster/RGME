package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.LogicLoop;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.List;
import org.joml.Vector2f;

/**
 * @author J Hoffman
 * Created: Mar 18, 2021
 */

public class ContextMenu extends Widget implements LogicLoop {
    
    private int currIndex;
    
    public float windowWidth;
    public float windowHeight;
    
    public boolean hovered;
    private boolean commandSelected;
    
    private String selectedCommandName;
    
    private final Rectangle[] rectangles;
    private final List<String> commands;
    
    public ContextMenu(float xPos, float yPos, float width, List<String> commands) {
        super(xPos, yPos, width, (28 * commands.size()) + 2);
        this.commands = commands;
        
        rectangles = new Rectangle[commands.size()];
        
        for(int c = 0; c < commands.size(); c++) {
            rectangles[c] = new Rectangle(xPos + 1, yPos + (28 * c) + 1, width - 2, 28);
        }
    }

    @Override
    public Command update(Mouse mouse) {
        hovered         = false;
        commandSelected = false;
        
        for(int c = 0; c < commands.size(); c++) {
            if(rectangles[c].contains(mouse.cursorPos)) {
                currIndex = c;
                hovered   = true;
                
                if(clickedOnce(rectangles[c], mouse)) {
                    commandSelected     = true;
                    selectedCommandName = commands.get(c);
                }
            }
        }
        
        if(!hovered) currIndex = -1;
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_SILVER, uiProgram);
        
        for(int c = 0; c < commands.size(); c++) {
            background.drawRectangle(
                    rectangles[c], 
                    (currIndex == c && hovered) ? Color.RGME_LIGHT_GRAY : Color.RGME_MEDIUM_GRAY, 
                    uiProgram);
            
            font.drawString(
                    commands.get(c), 
                    rectangles[c].xPos + 6, rectangles[c].yPos + 20, 
                    1, 
                    Color.RGME_WHITE, 
                    uiProgram);
        }
    }
    
    public void setPosition(Vector2f position, float windowWidth, float windowHeight) {
        if(position.x + bounds.width > windowWidth) {
            float offset = windowWidth - (position.x + bounds.width);
            bounds.xPos  = position.x + offset;
        } else {
            bounds.xPos = position.x;
        }
        
        if((windowHeight - position.y) - bounds.height < 0) {
            float offset = (windowHeight - position.y) - bounds.height;
            bounds.yPos  = position.y + offset;
        } else {
            bounds.yPos = position.y;
        }
        
        for(int c = 0; c < commands.size(); c++) {
            rectangles[c].xPos = bounds.xPos + 1;
            rectangles[c].yPos = bounds.yPos + (28 * c) + 1;
        }
    }
    
    public boolean commandSelected() {
        return commandSelected;
    }
    
    public String getSelectedCommandName() {
        return selectedCommandName;
    }
    
}