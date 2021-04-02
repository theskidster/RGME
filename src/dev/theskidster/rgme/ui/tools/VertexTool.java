package dev.theskidster.rgme.ui.tools;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.ui.containers.ToolBox;
import dev.theskidster.rgme.ui.widgets.Scrollbar;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Observable;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.LinkedList;
import org.joml.Vector2f;

/**
 * @author J Hoffman
 * Created: Mar 24, 2021
 */

public class VertexTool extends Tool {

    private final Rectangle viewport    = new Rectangle(0, 0, 294, 224);
    private final Rectangle addButton   = new Rectangle(0, 0, 24, 24);
    private final Rectangle subButton   = new Rectangle(0, 0, 24, 24);
    private final Icon addIcon          = new Icon(24, 24);
    private final Icon subIcon          = new Icon(24, 24);
    private final Scrollbar scrollbar   = new Scrollbar(TOOLBAR_WIDTH - 24, 0, 176, 224);
    private final Observable observable = new Observable(this);
    
    public VertexTool(int order) {
        super("Vertex Manipulator", 4, 2, order);
        
        observable.properties.put("viewportSize", null);
        observable.addObserver(scrollbar);
        
        addIcon.setSubImage(8, 3);
        subIcon.setSubImage(8, 4);
        
        widgets = new LinkedList<>();
        widgets.add(scrollbar);
    }
    
    @Override
    public Command update(Mouse mouse, ToolBox toolBox, GameObject selectedGameObject) {
        updateButton(mouse, toolBox);
        
        if(selected) {
            if(addButton.contains(mouse.cursorPos)) {
                if(mouse.clicked) addIcon.setColor(Color.RGME_WHITE);
                else              addIcon.setColor(Color.RGME_SILVER);
                
                //TODO: add/delete vertices, implement selector and movement
            } else {
                addIcon.setColor(Color.RGME_SILVER);
            }
            
            if(subButton.contains(mouse.cursorPos)) {
                if(mouse.clicked) subIcon.setColor(Color.RGME_WHITE);
                else              subIcon.setColor(Color.RGME_SILVER);
                
            } else {
                subIcon.setColor(Color.RGME_SILVER);
            }
            
            scrollbar.update(mouse);
        }
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderButton(uiProgram, background);
        
        if(selected) {
            background.drawRectangle(viewport, Color.RGME_SLATE_GRAY, uiProgram);
            
            addIcon.render(uiProgram);
            subIcon.render(uiProgram);
            
            scrollbar.render(uiProgram, background, font);
        }
    }
    
    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateButton(parentPosX, parentPosY);
        
        viewport.xPos = parentPosX + 42;
        viewport.yPos = parentPosY;
        
        addButton.xPos = parentPosX + (TOOLBAR_WIDTH - 64);
        addButton.yPos = parentPosY - 32;
        subButton.xPos = parentPosX + (TOOLBAR_WIDTH - 32);
        subButton.yPos = parentPosY - 32;
        
        addIcon.position.set(addButton.xPos, addButton.yPos + 24);
        subIcon.position.set(subButton.xPos, subButton.yPos + 24);
        
        observable.notifyObservers("viewportSize", new Vector2f(parentPosX, parentPosY));
    }
    
}