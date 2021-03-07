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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author J Hoffman
 * Created: Mar 6, 2021
 */

class Category extends Element {

    private int memberIndex;
    private int length = 1;
    
    boolean visible;
    boolean selected;
    boolean collapsed = true;
    boolean eyeHovered;
    boolean arrowHovered;
    
    private final Rectangle bounds;
    private final Rectangle eyeButton;
    private final Rectangle arrowButton;
    
    private final Icon eyeIcon;
    private final Icon arrowIcon;
    
    private final String categoryName;
    
    private final Map<Integer, Member> members = new LinkedHashMap<>();
    private final List<String> objectNames     = new ArrayList<>();
    
    Category(String categoryName) {
        this.categoryName = categoryName;
        bounds    = new Rectangle(0, 0, 296, 28);
        
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
        
        eyeHovered   = false;
        arrowHovered = false;
        
        //toggle category visibility
        {
            eyeButton.xPos = bounds.xPos + 3;
            eyeButton.yPos = bounds.yPos + 7;

            eyeIcon.position.set(eyeButton.xPos + 1, eyeButton.yPos + 17);

            if(eyeButton.contains(mouse.cursorPos)) {
                eyeHovered = true;
                
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
                arrowHovered = true;
                
                if(prevPressed != currPressed && !prevPressed) collapsed = !collapsed;

                if(collapsed) {
                    arrowIcon.setSubImage(7, 1);
                    length = 1;
                } else {
                    arrowIcon.setSubImage(8, 1);
                    if(!members.isEmpty()) length = members.size() + 1;
                }
            }
        }
        
        if(!collapsed && !members.isEmpty()) {
            for(int i = 0; i < memberIndex; i++) {
                if(members.get(i) != null) {
                    members.get(i).update(bounds.xPos, bounds.yPos, mouse, i + 1);
                }
            }
            
            if(!hovered && mouse.clicked) {
                clicked = false;
            }
        }
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        if(clicked) background.drawRectangle(bounds, Color.RGME_BLUE, uiProgram);
        
        eyeIcon.render(uiProgram);
        arrowIcon.render(uiProgram);
        
        font.drawString(categoryName, 
                        bounds.xPos + 56, bounds.yPos + 20, 
                        1, 
                        selected ? Color.RGME_YELLOW : Color.RGME_WHITE, 
                        uiProgram);
        
        if(!collapsed && !members.isEmpty()) {
            members.values().forEach(member -> member.render(uiProgram, background, font, selected));
        }
    }
    
    float getLength() { return length; }
    
    boolean onlyBoundsSelected() {
        return hovered && !eyeHovered && !arrowHovered && 
               (prevPressed != currPressed && !prevPressed);
    }
    
    void addGameObject(GameObject gameObject) {
        int duplicate = 0;

        while(objectNames.contains(gameObject.getName())) {
            duplicate++;
            gameObject.setName(gameObject.getName() + " (" + duplicate + ")");
        }
        
        objectNames.add(gameObject.getName());
        members.put(memberIndex, new Member(categoryName, gameObject));
        
        memberIndex++;
    }
    
}