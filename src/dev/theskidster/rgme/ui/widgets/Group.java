package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.LogicLoop;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import org.joml.Vector2f;

/**
 * @author J Hoffman
 * Created: Mar 17, 2021
 */

public class Group extends Widget implements LogicLoop, PropertyChangeListener {

    private final int index;
    
    private float verticalOffset;
    
    private boolean visible   = true;
    private boolean collapsed = true;
    
    private final String name;
    private final Icon eyeIcon          = new Icon(20, 20);
    private final Icon arrowIcon        = new Icon(20, 20);
    private final Rectangle eyeBounds   = new Rectangle(0, 0, 22, 18);
    private final Rectangle arrowBounds = new Rectangle(0, 0, 16, 16);
    
    private final Color color; //TODO: temp just to see where things are.
    
    public Map<Integer, GameObject> gameObjects;
    
    public Group(String name, Map<Integer, GameObject> gameObjects) {
        super(0, 0, TOOLBAR_WIDTH - 28, 28);
        this.name        = name;
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
        
        color = Color.random();
    }
    
    @Override
    public Command update(Mouse mouse) {
        if(clickedOnce(eyeBounds, mouse)) {
            visible = !visible;
            gameObjects.values().forEach(gameObject -> gameObject.setVisible(visible));
            
            if(visible) eyeIcon.setSubImage(9, 2);
            else        eyeIcon.setSubImage(10, 2);
        }
        
        if(clickedOnce(arrowBounds, mouse)) {
            collapsed = !collapsed;
            
            if(collapsed) arrowIcon.setSubImage(7, 1);
            else          arrowIcon.setSubImage(8, 1);
        }
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, color, uiProgram);
        
        eyeIcon.render(uiProgram);
        arrowIcon.render(uiProgram);
        
        font.drawString(name, bounds.xPos + 56, bounds.yPos + 20, 1, Color.RGME_WHITE, uiProgram);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()) {
            case "viewportSize" -> {
                Vector2f size = (Vector2f) evt.getNewValue();
                
                bounds.xPos = size.x;
                bounds.yPos = size.y + (bounds.height * index) + verticalOffset;
                
                eyeBounds.xPos = bounds.xPos + 3;
                eyeBounds.yPos = bounds.yPos + 5;
                eyeIcon.position.set(eyeBounds.xPos + 1, eyeBounds.yPos + 19);
                
                arrowBounds.xPos = bounds.xPos + 34;
                arrowBounds.yPos = bounds.yPos + 7;
                arrowIcon.position.set(arrowBounds.xPos - 1, arrowBounds.yPos + 18);
            }
            
            case "verticalOffset" -> {
                verticalOffset = (Float) evt.getNewValue();
            }
        }
    }

}