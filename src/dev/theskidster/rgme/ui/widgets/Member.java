package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.scene.WorldLight;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;

/**
 * @author J Hoffman
 * Created: Mar 7, 2021
 */

class Member {
    
    private int clickCount;
    
    private boolean prevPressed;
    private boolean currPressed;
    private boolean eyeHovered;
    private boolean hovered;
    private boolean clicked;
    boolean selected;
    
    final GameObject gameObject;
    final Rectangle bounds;
    final Icon gameObjectIcon;
    private Color color;
    private final Icon eyeIcon;
    private final Rectangle eyeButton;
    
    Member(String categoryName, GameObject gameObject) {
        this.gameObject = gameObject;
        
        bounds   = new Rectangle(0, 0, 336, 28);
        gameObjectIcon = new Icon(20, 20);
        
        switch(categoryName) {
            case "Visible Geometry" -> gameObjectIcon.setSubImage(0, 1);
            case "Bounding Volumes" -> gameObjectIcon.setSubImage(1, 1);
            case "Trigger Boxes"    -> gameObjectIcon.setSubImage(2, 1);
            
            case "Light Sources" -> {
                gameObjectIcon.setSubImage((gameObject instanceof WorldLight) ? 3 : 4, 1);
            }
            
            case "Entities"  -> gameObjectIcon.setSubImage(5, 1);
            case "Instances" -> gameObjectIcon.setSubImage(6, 1);
        }
        
        eyeButton = new Rectangle(0, 0, 22, 18);
        eyeIcon   = new Icon(20, 20);
        eyeIcon.setSubImage(9, 2);
    }

    public void update(float parentPosX, float parentPosY, float parentHeight, Mouse mouse, int index, boolean categorySelected, 
                       Rectangle topEdge, Rectangle bottomEdge) {
        bounds.xPos = parentPosX;
        bounds.yPos = parentPosY + (28 * index);
        
        prevPressed = currPressed;
        currPressed = mouse.clicked;
        
        boolean outOfBounds = (bounds.yPos + bounds.height <= topEdge.yPos + topEdge.height) || 
                              (bounds.yPos >= bottomEdge.yPos);
        
        //detemine member selection eligibility
        {
            if(!outOfBounds && !topEdge.contains(mouse.cursorPos) && !bottomEdge.contains(mouse.cursorPos) && mouse.button.equals("left")) {
                if(bounds.contains(mouse.cursorPos) && !eyeButton.contains(mouse.cursorPos)) {
                    hovered = true;
                    if(mouse.clicked) clicked = true;
                    
                    //Provide the text area in SceneGraph to change the name of the selected game object.
                    if((prevPressed != currPressed && !prevPressed) && mouse.cursorPos.x > gameObjectIcon.position.x + 20 &&
                       !(gameObject instanceof WorldLight)) {
                        clickCount++;
                        SceneGraph.showTextArea(clickCount >= 2, this);
                    }
                } else {
                    hovered = false;
                    clicked = false;
                    if(mouse.clicked) clickCount = 0;
                }
            }
        }
        
        color = (categorySelected) ? Color.RGME_YELLOW : Color.RGME_WHITE;
        
        //toggle game object visibility
        {
            eyeButton.xPos = bounds.xPos + 3;
            eyeButton.yPos = bounds.yPos + 5;
            
            if(eyeButton.contains(mouse.cursorPos) && !outOfBounds && !topEdge.contains(mouse.cursorPos) && 
              !bottomEdge.contains(mouse.cursorPos) && mouse.button.equals("left")) {
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
        
        gameObjectIcon.position.set(bounds.xPos + 56, bounds.yPos + 24);
        gameObjectIcon.setColor(color);
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
        gameObjectIcon.render(uiProgram);
    }
    
    boolean onlyBoundsSelected() {
        return hovered && clicked && !eyeHovered && 
               (prevPressed != currPressed && !prevPressed);
    }
    
}