package dev.theskidster.rgme.ui.elements;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.Command;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * @author J Hoffman
 * Created: Mar 12, 2021
 */

public final class Menu extends Element {

    private int currIndex;
    
    private Rectangle outline;
    
    private Rectangle[] rectangles;
    private final List<Command> commands;
    
    public Menu(List<Command> commands, float xPos, float yPos, float width) {
        super(0, 0);
        
        this.commands = commands;
        init(xPos, yPos, width);
    }
    
    public Menu(String action, Command command, float width) {
        super(0, 0);
        
        commands = new ArrayList<Command>() {{
            add(command);
        }};
        
        init(0, 0, width);
    }
    
    public void init(float xPos, float yPos, float width) {
        outline    = new Rectangle(xPos, yPos, width, 30 * commands.size());
        rectangles = new Rectangle[commands.size()];
        
        for(int c = 0; c < commands.size(); c++) {
            rectangles[c] = new Rectangle(xPos + 1, yPos + (28 * c) + 1, width - 2, 28);
        }
    }
    
    @Override
    public void update(Mouse mouse) {
        prevPressed = currPressed;
        currPressed = mouse.clicked;
        
        hovered = false;
        
        for(int c = 0; c < commands.size(); c++) {
            if(rectangles[c].contains(mouse.cursorPos)) {
                currIndex = c;
                hovered   = true;
                
                if((prevPressed != currPressed && !prevPressed) && mouse.button.equals("left")) {
                    commands.get(currIndex).execute();
                }
            }
        }
        
        if(!hovered) currIndex = -1;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(outline, Color.RGME_SILVER, uiProgram);
        
        for(int c = 0; c < commands.size(); c++) {
            background.drawRectangle(
                    rectangles[c], 
                    (currIndex == c && hovered) ? Color.RGME_LIGHT_GRAY : Color.RGME_MEDIUM_GRAY, 
                    uiProgram);
            
            font.drawString(
                    commands.get(c).action, 
                    rectangles[c].xPos + 6, rectangles[c].yPos + 20, 
                    1, 
                    Color.RGME_WHITE, 
                    uiProgram);
        }
    }

    @Override
    public void updatePosX(int parentPosX) {
        outline.xPos = parentPosX;
        for(Rectangle rectangle : rectangles) rectangle.xPos = parentPosX + 1;
    }

    @Override
    public void updatePosY(int parentPosY) {
        outline.yPos = parentPosY;
        
        for(int c = 0; c < commands.size(); c++) {
            rectangles[c].yPos = parentPosY + (28 * c) + 1;
        }
    }

}