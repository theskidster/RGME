package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.scene.VisibleGeometry;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.ui.containers.SceneExplorer;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;

/**
 * @author J Hoffman
 * Created: Mar 18, 2021
 */

public class Member {
    
    private final int groupIndex;
    
    public boolean selected;
    private boolean typeIconSet;
    private boolean prevPressed;
    private boolean currPressed;
    
    private final Icon typeIcon       = new Icon(20, 20);
    private final Icon eyeIcon        = new Icon(20, 20);
    private final Rectangle eyeBounds = new Rectangle(0, 0, 22, 18);
    private final Rectangle bounds    = new Rectangle(0, 0, TOOLBAR_WIDTH - 28, 28);
    
    public GameObject gameObject;
    
    public Member(int groupIndex) {
        this.groupIndex = groupIndex;
        eyeIcon.setSubImage(9, 2);
        
        /*
        TODO:
        menu options:
        - Rename
        - Delete
        - Show/Hide
        */
    }
    
    public void update(GameObject gameObject, Rectangle groupBounds, int order, Mouse mouse, SceneExplorer explorer) {
        this.gameObject = gameObject;
        
        bounds.xPos = groupBounds.xPos;
        bounds.yPos = groupBounds.yPos + (28 * order);
        
        eyeBounds.xPos = bounds.xPos + 3;
        eyeBounds.yPos = bounds.yPos + 5;
        eyeIcon.position.set(eyeBounds.xPos + 1, eyeBounds.yPos + 19);
        
        typeIcon.position.set(bounds.xPos + 56, bounds.yPos + 24);
        
        prevPressed = currPressed;
        currPressed = mouse.clicked;
        
        if((prevPressed != currPressed && !prevPressed) && eyeBounds.contains(mouse.cursorPos) && !explorer.outOfBounds) {
            gameObject.setVisible(!gameObject.getVisible());
        }
        
        if(gameObject.getVisible()) eyeIcon.setSubImage(9, 2);
        else                        eyeIcon.setSubImage(10, 2);
        
        if((prevPressed != currPressed && !prevPressed) && !eyeBounds.contains(mouse.cursorPos) && 
           bounds.contains(mouse.cursorPos) && !explorer.outOfBounds && 
           (mouse.button.equals("left") || mouse.button.equals("right"))) {
            explorer.groupIndex         = groupIndex;
            explorer.selectedGameObject = gameObject;
            
            selected = true;
        }
        
        selected = (gameObject == explorer.selectedGameObject);
        
        if(!typeIconSet) {
            if(gameObject instanceof VisibleGeometry) {
                typeIcon.setSubImage(0, 1);
            }
            
            typeIconSet = true;
        }
    }
    
    public void render(Program uiProgram, Background background, FreeTypeFont font, Color fontColor) {
        if(gameObject != null) {
            if(selected) background.drawRectangle(bounds, Color.RGME_BLUE, uiProgram);
            
            eyeIcon.setColor(fontColor);
            typeIcon.setColor(fontColor);
            
            eyeIcon.render(uiProgram);
            typeIcon.render(uiProgram);
            
            font.drawString(gameObject.getName(), bounds.xPos + 80, bounds.yPos + 20, 1, fontColor, uiProgram);
        }
    }
    
}