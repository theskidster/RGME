package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.scene.Scene;
import dev.theskidster.rgme.scene.SculptableGameObject;
import dev.theskidster.rgme.scene.VisibleGeometry;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.ui.tools.FaceTool;
import dev.theskidster.rgme.ui.tools.Paintbrush;
import dev.theskidster.rgme.ui.tools.Properties;
import dev.theskidster.rgme.ui.tools.Rotate;
import dev.theskidster.rgme.ui.tools.Scale;
import dev.theskidster.rgme.ui.tools.Tool;
import dev.theskidster.rgme.ui.tools.Translate;
import dev.theskidster.rgme.ui.tools.VertexTool;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;

/**
 * @author J Hoffman
 * Created: Mar 23, 2021
 */

public class ToolBox extends Container implements PropertyChangeListener {
    
    private float windowWidth;
    private static float windowHeight;
    
    private float totalLengthOfExplorer;
    
    private boolean explorerTextAreaVisible;
    
    private GameObject prevGameObject;
    private GameObject selectedGameObject;
    private Scene scene;
    public String currTool;
    
    private final Rectangle sidebar = new Rectangle(0, 0, 38, 0);
    
    private final LinkedList<Tool> tools = new LinkedList<>();
    
    public ToolBox(Scene scene) {
        super(0, 0, TOOLBAR_WIDTH, 0, "Tool Box", 10, 0);
        this.scene = scene;
        
        widgets = new LinkedList<>();
    }

    @Override
    public Command update(Mouse mouse) {
        if(!widgetHovered(mouse.cursorPos) && !explorerTextAreaVisible) {
            mouse.setCursorShape(GLFW_ARROW_CURSOR);
        }
        
        for(Tool tool : tools) {
            Command command = tool.update(mouse, this, selectedGameObject);
            if(tool.selected) currTool = tool.name;
            if(command != null) return command;
        }
        
        //Check if no tool is selected.
        if(!tools.stream().anyMatch(tool -> tool.selected)) {
            currTool = null;
            widgets.clear();
        }
        
        title = (currTool == null) ? "Tool Box" : currTool;
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_MEDIUM_GRAY, uiProgram);
        background.drawRectangle(sidebar, Color.RGME_SLATE_GRAY, uiProgram);
        renderTitleBar(uiProgram, background, font);
        
        tools.forEach(tool -> tool.render(uiProgram, background, font));
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        windowWidth  = parentPosX;
        windowHeight = parentPosY;
        
        bounds.xPos   = parentPosX - bounds.width;
        bounds.yPos   = totalLengthOfExplorer;
        bounds.height = parentPosY - totalLengthOfExplorer;
        
        sidebar.xPos   = bounds.xPos;
        sidebar.yPos   = bounds.yPos + titleBar.height;
        sidebar.height = bounds.height;
        
        relocateTitleBar();
        
        tools.forEach(tool -> tool.relocate(bounds.xPos, sidebar.yPos));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()) {
            case "totalLength"     -> { totalLengthOfExplorer = (Float) evt.getNewValue(); }
            case "textAreaVisible" -> { explorerTextAreaVisible = (Boolean) evt.getNewValue(); }
            
            case "gameObject" ->  {
                prevGameObject     = selectedGameObject;
                selectedGameObject = (GameObject) evt.getNewValue();
                
                if(prevGameObject != selectedGameObject) {
                    setAvailableTools();
                    relocate(windowWidth, windowHeight);
                }
            }
        }
    }
    
    private void setAvailableTools() {
        tools.clear();
        
        if(selectedGameObject != null) {
            if(selectedGameObject instanceof VisibleGeometry) {
                tools.add(new Paintbrush(1));
                tools.add(new VertexTool(2));
                tools.add(new FaceTool(3));
                tools.add(new Translate(bounds.xPos, bounds.yPos, 4));
                tools.add(new Rotate(bounds.xPos, bounds.yPos, 5));
                tools.add(new Scale(6, selectedGameObject));
                tools.add(new Properties(7));
            }
        }
    }
    
    public static float getWindowHeight() {
        return windowHeight;
    }
    
    public LinkedHashMap<Integer, Vector3f> getVertexPositions() {
        return ((SculptableGameObject) scene.selectedGameObject).getVertexPositions();
    }

}