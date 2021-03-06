package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.elements.Element;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**
 * @author J Hoffman
 * Created: Mar 6, 2021
 */

class Category extends Element {

    private float length = 28;
    
    boolean visible;
    boolean selected;
    boolean collapsed = true;
    
    private final Rectangle bounds;
    private final Rectangle eyeButton;
    private final Rectangle arrowButton;
    
    private final Icon eyeIcon;
    private final Icon arrowIcon;
    
    private final String name;
    
    private final Map<String, GameObject> gameObjects = new HashMap<>();
    
    Category(String name) {
        this.name = name;
        
        bounds = new Rectangle(0, 0, 296, 28);
        
        eyeButton = new Rectangle(0, 0, 22, 14);
        eyeIcon   = new Icon(20, 20);
        eyeIcon.setSubImage(9, 2);
        
        arrowButton = new Rectangle(0, 0, 14, 14);
        arrowIcon   = new Icon(20, 20);
        arrowIcon.setSubImage(7, 1);
    }
    
    @Override
    public void update(int parentPosX, int parentPosY, Mouse mouse) {
        bounds.xPos = xOffset + parentPosX;
        bounds.yPos = yOffset + parentPosY + 40;
        
        hovered = bounds.contains(mouse.cursorPos);
        
        if(hovered) {
            prevPressed = currPressed;
            currPressed = mouse.clicked;
        }
        
        //toggle category visibility
        {
            eyeButton.xPos = bounds.xPos + 3;
            eyeButton.yPos = bounds.yPos + 7;

            eyeIcon.position.set(eyeButton.xPos + 1, eyeButton.yPos + 17);

            if(eyeButton.contains(mouse.cursorPos)) {
                if(prevPressed != currPressed && !prevPressed) visible = !visible;

                if(visible) {
                    eyeIcon.setSubImage(10, 2);
                } else {
                    eyeIcon.setSubImage(9, 2);
                    //TODO: toggle visiblity of all game objects in this category
                }
            }
        }
        
        //expand/collapse category
        {
            arrowButton.xPos = bounds.xPos + 36;
            arrowButton.yPos = bounds.yPos + 7;

            arrowIcon.position.set(arrowButton.xPos - 3, arrowButton.yPos + 18);

            if(arrowButton.contains(mouse.cursorPos)) {
                if(prevPressed != currPressed && !prevPressed) collapsed = !collapsed;

                if(collapsed) {
                    arrowIcon.setSubImage(7, 1);
                    length = 28;
                } else {
                    arrowIcon.setSubImage(8, 1);
                    //TODO: expand category
                    
                    
                }
            }
        }
        
        if(gameObjects.isEmpty()) {
            
        }
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_BLUE, uiProgram);
        
        eyeIcon.render(uiProgram);
        arrowIcon.render(uiProgram);
        
        font.drawString(name, 
                        bounds.xPos + 56, bounds.yPos + 20, 
                        1, 
                        selected ? Color.RGME_YELLOW : Color.RGME_WHITE, 
                        uiProgram);
        
        if(!collapsed) {
            if(gameObjects.isEmpty()) {
                
            } else {
                for(int i = 0; i < gameObjects.size(); i++) {
                    
                }
            }
        }
    }
    
    String getName()  { return name; }
    float getLength() { return length; }
    
}