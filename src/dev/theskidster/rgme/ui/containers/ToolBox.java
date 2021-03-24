package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.scene.VisibleGeometry;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;

/**
 * @author J Hoffman
 * Created: Mar 23, 2021
 */

public class ToolBox extends Container implements PropertyChangeListener {
    
    private float totalLengthOfExplorer;
    private GameObject selectedGameObject;
    
    private final Rectangle sidebar = new Rectangle(0, 0, 38, 0);
    
    public ToolBox() {
        super(0, 0, TOOLBAR_WIDTH, 0, "Tool Box", 10, 0);
        widgets = new ArrayList<>();
    }

    @Override
    public Command update(Mouse mouse) {
        if(!widgetHovered(mouse.cursorPos)) {
            mouse.setCursorShape(GLFW_ARROW_CURSOR);
        }
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_MEDIUM_GRAY, uiProgram);
        background.drawRectangle(sidebar, Color.RGME_DARK_GRAY, uiProgram);
        renderTitleBar(uiProgram, background, font);
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        bounds.xPos   = parentPosX - bounds.width;
        bounds.yPos   = totalLengthOfExplorer;
        bounds.height = parentPosY - totalLengthOfExplorer;
        
        sidebar.xPos   = bounds.xPos;
        sidebar.yPos   = bounds.yPos + titleBar.height;
        sidebar.height = bounds.height;
        
        relocateTitleBar();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()) {
            case "totalLength" -> { totalLengthOfExplorer = (Float) evt.getNewValue(); }
            
            case "gameObject" ->  {
                selectedGameObject = (GameObject) evt.getNewValue();
                
                if(selectedGameObject != null) {
                    if(selectedGameObject instanceof VisibleGeometry) {
                        //TODO: populate toolbox with tools corresponding to this type.
                    }
                }
            }
        }
    }

}