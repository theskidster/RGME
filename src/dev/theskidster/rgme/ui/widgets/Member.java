package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.elements.Element;
import static dev.theskidster.rgme.ui.widgets.SceneGraph.*;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;

/**
 * @author J Hoffman
 * Created: Mar 7, 2021
 */

class Member {
    
    int index;
    
    final GameObject gameObject;
    final Rectangle bounds;
    final Icon icon;
    
    Member(String categoryName, GameObject gameObject) {
        this.gameObject = gameObject;
        
        bounds = new Rectangle(0, 0, 296, 28);
        icon   = new Icon(20, 20);
        
        switch(categoryName) {
            case "Visible Geometry" -> icon.setSubImage(0, 1);
            case "Bounding Volumes" -> icon.setSubImage(1, 1);
        }
    }

    public void update(int parentPosX, int parentPosY, Mouse mouse, int index) {
        bounds.xPos = parentPosX;
        bounds.yPos = parentPosY + (28 * index);
        
        icon.position.set(bounds.xPos + 54, bounds.yPos + 24);
    }
    
    Color color = Color.create((int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100));
    
    public void render(Program uiProgram, Background background, FreeTypeFont font, boolean categorySelected) {
        background.drawRectangle(bounds, color, uiProgram);
        
        font.drawString(
                gameObject.getName(), 
                bounds.xPos + 78, bounds.yPos + 20, 
                1, 
                categorySelected ? Color.RGME_YELLOW : Color.RGME_WHITE, 
                uiProgram);
        
        icon.render(uiProgram);
    }
    
}