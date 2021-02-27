package dev.theskidster.rgme.ui;

import com.mlomb.freetypejni.FreeType;
import com.mlomb.freetypejni.Library;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.widgets.MenuBar;
import dev.theskidster.rgme.ui.widgets.Widget;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import java.util.LinkedHashMap;
import java.util.Map;
import org.joml.Matrix4f;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author J Hoffman
 * Created: Feb 24, 2021
 */

public final class UI {
    
    private final Mouse mouse;
    private final Library freeType;
    private FreeTypeFont font;
    private final Background background = new Background();
    private final Matrix4f projMatrix   = new Matrix4f();
    
    private Map<String, Widget> widgets;
    
    public UI(long windowHandle) {
        mouse = new Mouse(windowHandle);
        
        freeType = FreeType.newLibrary();
        setFont("fnt_karla_regular.ttf", 17);
        
        widgets = new LinkedHashMap<>() {{
            put("menu bar", new MenuBar());
        }};
    }
    
    public void update(int viewportWidth, int viewportHeight) {
        projMatrix.setPerspective((float) Math.toRadians(45), 
                                  (float) viewportWidth / viewportHeight, 
                                  0.1f, 
                                  Float.POSITIVE_INFINITY);
        
        float hw = (2f / viewportWidth);
        float hh = (-2f / viewportHeight);
        
        projMatrix.set(hw,  0,  0, 0, 
                        0, hh,  0, 0, 
                        0,  0, -1, 0, 
                       -1,  1,  0, 1);
        
        if(!widgets.values().stream().anyMatch(widget -> widget.hovered)) {
            mouse.setCursorShape(GLFW_ARROW_CURSOR);
        }
        
        widgets.forEach((name, widget) -> widget.update(viewportWidth, viewportHeight, mouse));
        widgets.entrySet().removeIf(widget -> widget.getValue().removeRequest);
    }
    
    public void render(Program uiProgram) {
        uiProgram.setUniform("uProjection", false, projMatrix);
        widgets.forEach((name, widget) -> widget.render(uiProgram, background, font));
        
        font.drawString("bleh", 100, 200, 1, Color.WHITE, uiProgram);
    }
    
    public void setFont(String filename, int size) {
        font = new FreeTypeFont(freeType, filename, size);
    }
    
    public void setMouseCursorPos(double xPos, double yPos) {
        mouse.cursorPos.set((int) xPos, (int) yPos);
    }
    
    public void destroy() {
        freeType.delete();
        font.freeBuffers();
        background.freeBuffers();
    }
    
}