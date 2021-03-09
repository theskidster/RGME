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
    
    private int length = 1;
    private int maxMemberIndex;
    private int verticalOffset;
    
    private float parentWidth;
    private float parentHeight;
    
    boolean visible = true;
    boolean selected;
    boolean collapsed = true;
    boolean eyeHovered;
    boolean arrowHovered;
    
    private final Rectangle bounds;
    private final Rectangle eyeButton;
    private final Rectangle arrowButton;
    private static final Rectangle[] parentEdges = new Rectangle[2];
    
    private final Icon eyeIcon;
    private final Icon arrowIcon;
    
    private final String categoryName;
    
    static GameObject currGameObject;
    
    private Color color;
    
    private final Map<Integer, Member> members = new LinkedHashMap<>();
    private final List<String> objectNames     = new ArrayList<>();
    
    static {
        parentEdges[0] = new Rectangle(0, 0, 0, 28);
        parentEdges[1] = new Rectangle(0, 0, 0, 28);
    }
    
    Category(String categoryName) {
        this.categoryName = categoryName;
        bounds    = new Rectangle(0, 0, 296, 28);
        
        eyeButton = new Rectangle(0, 0, 22, 18);
        eyeIcon   = new Icon(20, 20);
        eyeIcon.setSubImage(9, 2);
        
        arrowButton = new Rectangle(0, 0, 16, 16);
        arrowIcon   = new Icon(20, 20);
        arrowIcon.setSubImage(7, 1);
    }
    
    @Override
    public void update(float parentPosX, float parentPosY, Mouse mouse) {
        bounds.xPos = xOffset + parentPosX;
        bounds.yPos = yOffset + (parentPosY + 40) + verticalOffset;
        
        hovered = bounds.contains(mouse.cursorPos);
        color   = (selected) ? Color.RGME_YELLOW : Color.RGME_WHITE;
        
        if(hovered) {
            prevPressed = currPressed;
            currPressed = mouse.clicked;
        }
        
        eyeHovered   = false;
        arrowHovered = false;
        
        boolean outOfBounds = (bounds.yPos + bounds.height <= parentPosY + 40) || 
                              (bounds.yPos >= parentPosY + parentHeight);
        
        parentEdges[0].xPos  = parentPosX;
        parentEdges[0].yPos  = parentPosY + 12;
        parentEdges[0].width = parentWidth;
        
        parentEdges[1].xPos  = parentPosX;
        parentEdges[1].yPos  = parentPosY + parentHeight;
        parentEdges[1].width = parentWidth;
        
        //Toggle category visibility.
        {
            eyeButton.xPos = bounds.xPos + 3;
            eyeButton.yPos = bounds.yPos + 5;

            eyeIcon.position.set(eyeButton.xPos + 1, eyeButton.yPos + 19);
            eyeIcon.setColor(color);
            
            if(eyeButton.contains(mouse.cursorPos) && !outOfBounds && !parentEdges[0].contains(mouse.cursorPos) && 
              !parentEdges[1].contains(mouse.cursorPos)) {
                eyeHovered = true;
                
                if(prevPressed != currPressed && !prevPressed) {
                    visible = !visible;
                    members.forEach((index, member) -> member.gameObject.setVisible(visible));
                }
                
                if(visible) eyeIcon.setSubImage(9, 2);
                else        eyeIcon.setSubImage(10, 2);
            }
        }
        
        //Expand/collapse category.
        {
            arrowButton.xPos = bounds.xPos + 34;
            arrowButton.yPos = bounds.yPos + 7;

            arrowIcon.position.set(arrowButton.xPos - 1, arrowButton.yPos + 18);
            arrowIcon.setColor(color);
            
            if(arrowButton.contains(mouse.cursorPos) && !outOfBounds && !parentEdges[0].contains(mouse.cursorPos) && 
              !parentEdges[1].contains(mouse.cursorPos)) {
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
            for(int i = 0; i < maxMemberIndex; i++) {
                if(members.get(i) != null) {
                    members.get(i).update(bounds.xPos, bounds.yPos, parentHeight, mouse, i + 1, selected, parentEdges[0], parentEdges[1]);
                    
                    if(mouse.clicked && members.get(i).onlyBoundsSelected()) {
                        setCurrSelectedMember(i);
                    }
                }
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
                        color, 
                        uiProgram);
        
        if(!collapsed && !members.isEmpty()) {
            members.values().forEach(member -> member.render(uiProgram, background, font));
        }
    }
    
    private void setCurrSelectedMember(int index) {
        for(int i = 0; i < maxMemberIndex; i++) {
            if(members.get(i) != null) {
                members.get(i).selected = (i == index);
                
                if(members.get(i).selected) {
                    currGameObject = members.get(i).gameObject;
                    
                    //these values are for the categories state
                    selected = true;
                    clicked  = false;
                }
            }
        }
    }
    
    void unselectMembers() {
        for(int i = 0; i < maxMemberIndex; i++) {
            if(members.get(i) != null) {
                members.get(i).selected = false;
            }
        }
        
        currGameObject = null;
    }
    
    void setVerticalOffset(int verticalOffset) {
        this.verticalOffset = verticalOffset;
    }
    
    void setParentDimensions(float parentWidth, float parentHeight) {
        this.parentWidth  = parentWidth;
        this.parentHeight = parentHeight;
    }
    
    int getLength() { return length; }
    
    boolean onlyBoundsSelected() {
        return hovered && !eyeHovered && !arrowHovered && 
               (prevPressed != currPressed && !prevPressed);
    }
    
    boolean hasSelectedMember() {
        for(int i = 0; i < maxMemberIndex; i++) {
            if(members.get(i) != null) {
                if(members.get(i).selected) return true;
            } 
        }
        
        return false;
    }
    
    void addGameObject(GameObject gameObject) {
        int duplicate = 0;

        String gameObjectName = gameObject.getName();
        
        while(objectNames.contains(gameObjectName)) {
            duplicate++;
            gameObjectName = gameObject.getName() + " (" + duplicate + ")";
        }
        
        gameObject.setName(gameObjectName);
        
        objectNames.add(gameObject.getName());
        members.put(maxMemberIndex, new Member(categoryName, gameObject));
        
        maxMemberIndex++;
    }
    
}