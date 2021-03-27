package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.ui.containers.SceneExplorer;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.joml.Vector2f;

/**
 * @author J Hoffman
 * Created: Mar 17, 2021
 */

public class Group extends Widget implements PropertyChangeListener {

    private final int index;
    private int length;
    
    private float verticalOffset;
    private float parentPosY;
    
    private boolean selected;
    private boolean visible   = true;
    private boolean collapsed = true;
    
    private final String name;
    private final SceneExplorer explorer;
    private final Icon eyeIcon          = new Icon(20, 20);
    private final Icon arrowIcon        = new Icon(20, 20);
    private final Rectangle eyeBounds   = new Rectangle(0, 0, 22, 18);
    private final Rectangle arrowBounds = new Rectangle(0, 0, 16, 16);
    
    private Color fontColor;
    
    private Map<Integer, GameObject> gameObjects;
    private final Map<Integer, Member> members = new HashMap<>();
    
    public Group(String name, SceneExplorer explorer, Map<Integer, GameObject> gameObjects) {
        super(0, 0, TOOLBAR_WIDTH - 28, 28);
        this.name        = name;
        this.explorer    = explorer;
        this.gameObjects = gameObjects;
        
        eyeIcon.setSubImage(9, 2);
        arrowIcon.setSubImage(7, 1);
        
        switch(name) {
            default                 -> index = 0;
            case "Bounding Volumes" -> index = 1;
            case "Trigger Boxes"    -> index = 2;
            case "Light Sources"    -> index = 3;
            case "Entities"         -> index = 4;
            case "Instances"        -> index = 5;
        }
    }
    
    @Override
    public Command update(Mouse mouse) {
        if(clickedOnce(bounds, mouse) && !eyeBounds.contains(mouse.cursorPos) && !arrowBounds.contains(mouse.cursorPos) && 
           !explorer.outOfBounds && mouse.button.equals("left")) {
            explorer.groupIndex         = index;
            explorer.selectedGameObject = null;
            
            selected = true;
            
            members.values().forEach(member -> member.selected = false);
            explorer.showTextArea(false, null);
        }
        
        if(index == explorer.groupIndex) {
            fontColor = Color.RGME_YELLOW;
            
            if(members.values().stream().anyMatch(member -> member.selected)) {
                selected = false;
            }
        } else {
            fontColor = Color.RGME_WHITE;
            selected  = false;
        }
        
        if(explorer.selectedGameObject != null) selected = false;
        
        eyeIcon.setColor(fontColor);
        arrowIcon.setColor(fontColor);
        
        if(clickedOnce(eyeBounds, mouse) && !explorer.outOfBounds && mouse.button.equals("left")) {
            visible = !visible;
            gameObjects.values().forEach(gameObject -> gameObject.setVisible(visible));

            if(visible) eyeIcon.setSubImage(9, 2);
            else        eyeIcon.setSubImage(10, 2);
        }

        if(clickedOnce(arrowBounds, mouse) && !explorer.outOfBounds && mouse.button.equals("left")) {
            toggleCollapsed();
        }
        
        if(collapsed) arrowIcon.setSubImage(7, 1);
        else          arrowIcon.setSubImage(8, 1);
        
        if(!collapsed) {
            length = gameObjects.size() + 1;
            
            if(length > 1) {
                int order = 0;
                
                for(int i = 0; i <= Collections.max(gameObjects.keySet()); i++) {
                    if(gameObjects.containsKey(i)) {
                        order++;
                        
                        if(members.containsKey(i)) members.get(i).update(gameObjects.get(i), bounds, order, mouse, explorer);
                        else                       members.put(i, new Member(index));
                    }
                }
            }
            
            members.entrySet().removeIf(entry -> !gameObjects.containsKey(entry.getKey()));
            
        } else {
            length = 1;
        }
        
        bounds.yPos = parentPosY + verticalOffset;
        
        eyeBounds.xPos = bounds.xPos + 3;
        eyeBounds.yPos = bounds.yPos + 5;
        eyeIcon.position.set(eyeBounds.xPos + 1, eyeBounds.yPos + 19);

        arrowBounds.xPos = bounds.xPos + 34;
        arrowBounds.yPos = bounds.yPos + 7;
        arrowIcon.position.set(arrowBounds.xPos - 1, arrowBounds.yPos + 18);
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        if(selected) background.drawRectangle(bounds, Color.RGME_BLUE, uiProgram);
        
        eyeIcon.render(uiProgram);
        arrowIcon.render(uiProgram);
        
        font.drawString(name, bounds.xPos + 56, bounds.yPos + 20, 1, fontColor, uiProgram);
        
        if(!collapsed) {
            members.values().forEach(member -> member.render(uiProgram, background, font, fontColor));
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()) {
            case "viewportSize" -> {
                Vector2f size = (Vector2f) evt.getNewValue();
                
                bounds.xPos = size.x;
                parentPosY  = size.y;
            }
        }
    }
    
    @Override
    public void relocate(float parentPosX, float parentPosY) {
        //Unused.
    }
    
    public void setVerticalOffset(float verticalOffset) {
        this.verticalOffset = verticalOffset;
    }
    
    public int getLength() {
        return length;
    }
    
    public void setCollapsed(boolean value) {
        collapsed = value;
    }
    
    public void toggleCollapsed() {
        collapsed = !collapsed;
    }

}