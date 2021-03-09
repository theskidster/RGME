package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;

/**
 * @author J Hoffman
 * Created: Mar 7, 2021
 */

class Member {
    
    private boolean prevPressed;
    private boolean currPressed;
    private boolean eyeHovered;
    private boolean hovered;
    private boolean clicked;
    boolean selected;
    
    final GameObject gameObject;
    final Rectangle bounds;
    final Icon typeIcon;
    private Color color;
    private final Icon eyeIcon;
    private final Rectangle eyeButton;
    
    Member(String categoryName, GameObject gameObject) {
        this.gameObject = gameObject;
        
        bounds   = new Rectangle(0, 0, 296, 28);
        typeIcon = new Icon(20, 20);
        
        switch(categoryName) {
            case "Visible Geometry" -> typeIcon.setSubImage(0, 1);
            case "Bounding Volumes" -> typeIcon.setSubImage(1, 1);
            case "Trigger Boxes"    -> typeIcon.setSubImage(2, 1);
            case "Light Sources"    -> typeIcon.setSubImage(3, 1);
            case "Entities"         -> typeIcon.setSubImage(5, 1);
            case "Instances"        -> typeIcon.setSubImage(6, 1);
        }
        
        eyeButton = new Rectangle(0, 0, 22, 18);
        eyeIcon   = new Icon(20, 20);
        eyeIcon.setSubImage(9, 2);
    }

    public void update(float parentPosX, float parentPosY, Mouse mouse, int index, boolean categorySelected) {
        bounds.xPos = parentPosX;
        bounds.yPos = parentPosY + (28 * index);
        
        prevPressed = currPressed;
        currPressed = mouse.clicked;
        
        //detemine member selection eligibility
        {
            if(bounds.contains(mouse.cursorPos)) {
                hovered = true;
                if(mouse.clicked) clicked = true;
            } else {
                hovered = false;
                clicked = false;
            }
        }
        
        color = (categorySelected) ? Color.RGME_YELLOW : Color.RGME_WHITE;
        
        //toggle game object visibility
        {
            eyeButton.xPos = bounds.xPos + 3;
            eyeButton.yPos = bounds.yPos + 5;
            
            if(eyeButton.contains(mouse.cursorPos)) {
                eyeHovered = true;
                
                if(prevPressed != currPressed && !prevPressed) {
                    gameObject.setVisible(!gameObject.getVisible());
                }
            } else {
                eyeHovered = false;
            }
            
            eyeIcon.position.set(eyeButton.xPos + 1, eyeButton.yPos + 19);
            eyeIcon.setColor(color);
            
            if(gameObject.getVisible()) eyeIcon.setSubImage(9, 2);
            else                        eyeIcon.setSubImage(10, 2);
        }
        
        typeIcon.position.set(bounds.xPos + 56, bounds.yPos + 24);
        typeIcon.setColor(color);
    }
    
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        if(selected) background.drawRectangle(bounds, Color.RGME_BLUE, uiProgram);
        
        font.drawString(
                gameObject.getName(), 
                bounds.xPos + 80, bounds.yPos + 20, 
                1, 
                color, 
                uiProgram);
        
        eyeIcon.render(uiProgram);
        typeIcon.render(uiProgram);
    }
    
    boolean onlyBoundsSelected() {
        return hovered && clicked && !eyeHovered && 
               (prevPressed != currPressed && !prevPressed);
    }
    
}