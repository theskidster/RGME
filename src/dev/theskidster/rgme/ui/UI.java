package dev.theskidster.rgme.ui;

import com.mlomb.freetypejni.FreeType;
import com.mlomb.freetypejni.Library;
import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.commands.CommandHistory;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.main.Window;
import dev.theskidster.rgme.scene.Scene;
import dev.theskidster.rgme.ui.containers.Container;
import dev.theskidster.rgme.ui.containers.SceneExplorer;
import dev.theskidster.rgme.utils.Mouse;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashSet;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author J Hoffman
 * Created: Feb 24, 2021
 */

public final class UI implements PropertyChangeListener {
    
    public static final int TOOLBAR_WIDTH = 360;
    
    private final FreeTypeFont font;
    private final Mouse mouse;
    
    private final Library freeType      = FreeType.newLibrary();
    private final Background background = new Background();
    private final Matrix4f projMatrix   = new Matrix4f();
    
    private final LinkedHashSet<Container> containers;
    
    public UI(Window window, Scene scene) {
        mouse = new Mouse(window);
        font  = new FreeTypeFont(freeType, "fnt_karla_regular.ttf", 17);
        
        containers = new LinkedHashSet<>() {{
            add(new SceneExplorer(scene));
        }};
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()) {
            case "viewportSize" -> {
                Vector2f windowSize = ((Vector2f) evt.getNewValue());
                
                containers.forEach(container -> container.relocate(windowSize.x, windowSize.y));
                
                projMatrix.setPerspective((float) Math.toRadians(45), windowSize.x / windowSize.y, 0.1f, Float.POSITIVE_INFINITY);
                
                float hw = (2f / windowSize.x);
                float hh = (-2f / windowSize.y);
                
                projMatrix.set(hw,  0,  0, 0, 
                                0, hh,  0, 0, 
                                0,  0, -1, 0, 
                               -1,  1,  0, 1);
            }
        }
    }
    
    public void update(CommandHistory cmdHistory) {
        containers.forEach(container -> {
            Command command = container.update(mouse);
            if(command != null) cmdHistory.executeCommand(command);
        });
        
        containers.removeIf(container -> container.removalRequested());
        
        if(!containerHovered()) mouse.setCursorShape(GLFW_ARROW_CURSOR);
        
        mouse.scrolled = false;
    }
    
    public void render(Program uiProgram) {
        uiProgram.setUniform("uProjection", false, projMatrix);
        containers.forEach(container -> container.render(uiProgram, background, font));
    }
    
    public void destroy() {
        freeType.delete();
        font.freeBuffers();
        background.freeBuffers();
    }
    
    public boolean containerHovered() {
        return containers.stream().anyMatch(container -> container.hovered(mouse.cursorPos));
    }
    
    public void setMouseCursorPos(double xPos, double yPos) {
        mouse.cursorPos.set((int) xPos, (int) yPos);
    }
    
    public void setMouseAction(int button, int action) {
        switch(button) {
            case GLFW_MOUSE_BUTTON_RIGHT  -> mouse.button = "right";
            case GLFW_MOUSE_BUTTON_MIDDLE -> mouse.button = "middle";
            default -> mouse.button = "left";
        }
        
        mouse.clicked = (action == GLFW_PRESS);
    }
    
    public void setMouseScroll(double value) {
        mouse.scrollValue = (float) value;
        mouse.scrolled    = true;
    }
    
}