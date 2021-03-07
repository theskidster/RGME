package dev.theskidster.rgme.ui.elements;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**
 * @author J Hoffman
 * Created: Mar 5, 2021
 */

public final class Scrollbar extends Element {

    private final int MARGIN = 24;
    
    private int length;
    
    private final boolean vertical;
    
    private final Rectangle bounds = new Rectangle();
    
    private final Icon[] icons           = new Icon[4];
    private final Rectangle[] rectangles = new Rectangle[5];
    private final Color[] buttonColors   = new Color[2];
    
    private final Map<Integer, Rectangle> containers = new HashMap<>();
    
    public Scrollbar(int xOffset, int yOffset, boolean vertical, int length) {
        this.xOffset  = xOffset;
        this.yOffset  = yOffset;
        this.vertical = vertical;
        this.length   = length;
        
        icons[0] = new Icon(24, 24);
        icons[1] = new Icon(24, 24);
        icons[2] = new Icon(20, 20);
        icons[3] = new Icon(20, 20);
        
        icons[0].setColor(Color.WHITE);
        icons[1].setColor(Color.WHITE);
        icons[2].setColor(Color.RGME_WHITE);
        icons[3].setColor(Color.RGME_WHITE);
        
        icons[1].setSubImage(4, 4);
        
        rectangles[3] = new Rectangle(0, 0, MARGIN, MARGIN);
        rectangles[4] = new Rectangle(0, 0, MARGIN, MARGIN);
        
        if(vertical) {
            icons[0].setSubImage(4, 3);
            icons[2].setSubImage(6, 0);
            icons[3].setSubImage(7, 0);
            
            rectangles[0] = new Rectangle(0, 0, MARGIN, length);
            rectangles[1] = new Rectangle(0, 0, MARGIN - 2, length);
            rectangles[2] = new Rectangle(0, 0, MARGIN - 6, 0);
        } else {
            icons[0].setSubImage(3, 4);
            icons[2].setSubImage(8, 0);
            icons[3].setSubImage(9, 0);
            
            rectangles[0] = new Rectangle(0, 0, length, MARGIN);
            rectangles[1] = new Rectangle(0, 0, length, MARGIN - 2);
            rectangles[2] = new Rectangle(0, 0, 0, MARGIN - 6);
        }
        
        buttonColors[0] = Color.RGME_SLATE_GRAY;
        buttonColors[1] = Color.RGME_SLATE_GRAY;
    }
    
    @Override
    public void update(int parentPosX, int parentPosY, Mouse mouse) {
        bounds.xPos = xOffset + parentPosX;
        bounds.yPos = yOffset + parentPosY;
        
        if(vertical) {
            bounds.width  = MARGIN;
            bounds.height = length + (MARGIN * 2);
            
            icons[0].position.set(bounds.xPos, bounds.yPos + MARGIN);
            icons[1].position.set(bounds.xPos, bounds.yPos + length + (MARGIN * 2));
            icons[2].position.set(bounds.xPos + 2, bounds.yPos + 21);
            icons[3].position.set(bounds.xPos + 2, bounds.yPos + length + (MARGIN * 2) - 1);
            
            rectangles[0].xPos = bounds.xPos;
            rectangles[0].yPos = bounds.yPos + MARGIN;
            
            rectangles[1].xPos = bounds.xPos + 1;
            rectangles[1].yPos = bounds.yPos + MARGIN;
            
            if(containers.isEmpty()) {
                rectangles[2].xPos   = bounds.xPos + 3;
                rectangles[2].yPos   = bounds.yPos + MARGIN;
                rectangles[2].height = length;
            }
            
            rectangles[3].xPos = bounds.xPos;
            rectangles[3].yPos = bounds.yPos;
            
            rectangles[4].xPos = bounds.xPos;
            rectangles[4].yPos = bounds.yPos + length + MARGIN;
            
        } else {
            bounds.width  = length + (MARGIN * 2);
            bounds.height = MARGIN;
            
            icons[0].position.set(bounds.xPos, bounds.yPos + MARGIN);
            icons[1].position.set(bounds.xPos + length + MARGIN, bounds.yPos + MARGIN);
            icons[2].position.set(bounds.xPos + 1, bounds.yPos + 22);
            icons[3].position.set(bounds.xPos + length + MARGIN + 3, bounds.yPos + 22);
            
            rectangles[0].xPos = bounds.xPos + MARGIN;
            rectangles[0].yPos = bounds.yPos;
            
            rectangles[1].xPos = bounds.xPos + MARGIN;
            rectangles[1].yPos = bounds.yPos + 1;
            
            if(containers.isEmpty()) {
                rectangles[2].xPos  = bounds.xPos + MARGIN;
                rectangles[2].yPos  = bounds.yPos + 3;
                rectangles[2].width = length;
            }
            
            rectangles[3].xPos = bounds.xPos;
            rectangles[3].yPos = bounds.yPos;
            
            rectangles[4].xPos = bounds.xPos + length + MARGIN;
            rectangles[4].yPos = bounds.yPos;
            
        }
        
        hovered = bounds.contains(mouse.cursorPos);
        
        captureButtonInput(3, 0, mouse);
        captureButtonInput(4, 1, mouse);
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(rectangles[0], Color.RGME_LIGHT_GRAY, uiProgram);
        background.drawRectangle(rectangles[1], Color.RGME_DARK_GRAY, uiProgram);
        background.drawRectangle(rectangles[2], Color.RGME_SILVER, uiProgram);
        background.drawRectangle(rectangles[3], buttonColors[0], uiProgram);
        background.drawRectangle(rectangles[4], buttonColors[1], uiProgram);
        
        for(Icon icon : icons) icon.render(uiProgram);
    }
    
    private void captureButtonInput(int rectID, int colorIndex, Mouse mouse) {
        if(rectangles[rectID].contains(mouse.cursorPos)) {
            if(mouse.clicked) {
                buttonColors[colorIndex] = Color.RGME_LIGHT_GRAY;
            } else {
                buttonColors[colorIndex] = Color.RGME_MEDIUM_GRAY;
            }
        } else {
            buttonColors[colorIndex] = Color.RGME_DARK_GRAY;
        }
    }
    
    public void setContainers(Map<Integer, Rectangle> containers) {
        /*
        TODO:
        
        containers will contain rectangle objects that specify the dimensions of
        the content in the table that this scrollbar will use to determine its 
        length
        */
        
        this.containers.clear();
        this.containers.putAll(containers);
    }
    
}