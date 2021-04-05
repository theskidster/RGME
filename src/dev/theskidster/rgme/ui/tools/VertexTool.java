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
import dev.theskidster.rgme.ui.widgets.VertexID;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Observable;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import org.joml.Vector2f;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author J Hoffman
 * Created: Mar 24, 2021
 */

public class VertexTool extends Tool {
    
    private boolean collectionEmpty;
    
    private final Rectangle scissorBox  = new Rectangle();
    private final Rectangle viewport    = new Rectangle(0, 0, 294, 224);
    private final Rectangle addButton   = new Rectangle(0, 0, 24, 24);
    private final Rectangle subButton   = new Rectangle(0, 0, 24, 24);
    private final Icon addIcon          = new Icon(24, 24);
    private final Icon subIcon          = new Icon(24, 24);
    private final Scrollbar scrollbar   = new Scrollbar(TOOLBAR_WIDTH - 24, 0, 176, 224);
    private final Observable observable = new Observable(this);
    
    private LinkedHashMap<Integer, VertexID> vertices   = new LinkedHashMap<>();
    private LinkedHashMap<Integer, Float> vertexLengths = new LinkedHashMap<>();
    
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
            for(int i = 0; i < vertices.size(); i++) vertexLengths.put(i, 28f);
            
            scrollbar.setContentLength(vertexLengths);
            scrollbar.parentHovered = viewport.contains(mouse.cursorPos);
            scrollbar.update(mouse);
            
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
            
            collectionEmpty = toolBox.getVertexPositions().isEmpty();
            
            if(!collectionEmpty) {
                int order = 0;
                
                for(int i = 0; i <= Collections.max(toolBox.getVertexPositions().keySet()); i++) {
                    if(toolBox.getVertexPositions().containsKey(i)) {
                        order++;

                        if(vertices.containsKey(i)) vertices.get(i).update(viewport, order, scrollbar.getContentScrollOffset());
                        else                        vertices.put(i, new VertexID(i));
                    }
                }
            }
            
            vertices.entrySet().removeIf(entry -> !toolBox.getVertexPositions().containsKey(entry.getKey()));
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
            
            glEnable(GL_SCISSOR_TEST);
            glScissor((int) scissorBox.xPos, (int) scissorBox.yPos, (int) scissorBox.width, (int) scissorBox.height);
                if(!collectionEmpty) vertices.values().forEach(vertex -> vertex.render(uiProgram, background, font));
            glDisable(GL_SCISSOR_TEST);
        }
    }
    
    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateButton(parentPosX, parentPosY);
        
        viewport.xPos = parentPosX + 42;
        viewport.yPos = parentPosY;
        
        scissorBox.xPos   = viewport.xPos;
        scissorBox.yPos   = ToolBox.getWindowHeight() - (viewport.yPos + viewport.height);
        scissorBox.width  = viewport.xPos + viewport.width;
        scissorBox.height = viewport.height;
        
        addButton.xPos = parentPosX + (TOOLBAR_WIDTH - 64);
        addButton.yPos = parentPosY - 32;
        subButton.xPos = parentPosX + (TOOLBAR_WIDTH - 32);
        subButton.yPos = parentPosY - 32;
        
        addIcon.position.set(addButton.xPos, addButton.yPos + 24);
        subIcon.position.set(subButton.xPos, subButton.yPos + 24);
        
        observable.notifyObservers("viewportSize", new Vector2f(parentPosX, parentPosY));
    }
    
}