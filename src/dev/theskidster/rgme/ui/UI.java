package dev.theskidster.rgme.ui;

import com.mlomb.freetypejni.FreeType;
import com.mlomb.freetypejni.Library;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.elements.TextInputElement;
import dev.theskidster.rgme.ui.widgets.TestWidget;
import dev.theskidster.rgme.ui.widgets.Widget;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Observable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.joml.Matrix4f;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author J Hoffman
 * Created: Feb 24, 2021
 */

public final class UI {
    
    private static int viewWidth;
    private static int viewHeight;
    
    private static Mouse mouse;
    private static TextInputElement textInput;
    private final Library freeType;
    private FreeTypeFont font;
    private final Background background = new Background();
    private final Matrix4f projMatrix   = new Matrix4f();
    
    private Map<String, Widget> widgets;
    
    public UI(long windowHandle, int viewportWidth, int viewportHeight) {
        mouse      = new Mouse(windowHandle);
        viewWidth  = viewportWidth;
        viewHeight = viewportHeight;
        
        freeType = FreeType.newLibrary();
        setFont("fnt_karla_regular.ttf", 17);
        
        widgets = new LinkedHashMap<>() {{
            put("test widget", new TestWidget());
            //put("scene graph", new SceneGraph());
        }};
    }
    
    public void update(int viewportWidth, int viewportHeight) {
        viewWidth  = viewportWidth;
        viewHeight = viewportHeight;
        
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
        
        if(!getWidgetHovered() || !widgets.values().stream().anyMatch(widget -> widget.hasHoveredElement())) {
            mouse.setCursorShape(GLFW_ARROW_CURSOR);
        }
        
        widgets.forEach((name, widget) -> widget.update(viewportWidth, viewportHeight, mouse));
        widgets.entrySet().removeIf(widget -> widget.getValue().removeRequest);
        
        mouse.scrolled = false;
    }
    
    public void render(Program uiProgram) {
        uiProgram.setUniform("uProjection", false, projMatrix);
        widgets.forEach((name, widget) -> widget.render(uiProgram, background, font));
    }
    
    public void destroy() {
        freeType.delete();
        font.freeBuffers();
        background.freeBuffers();
    }
    
    public void captureKeyInput(int key, int action) {
        if(textInput != null) textInput.processInput(key, action);
    }
    
    public static TextInputElement getTextInputElement() { return textInput; } 
    public static int getViewWidth()  { return viewWidth; }
    public static int getViewHeight() { return viewHeight; }
    public boolean getWidgetHovered() { return widgets.values().stream().anyMatch(widget -> widget.hovered); }
    
    public void setFont(String filename, int size) {
        font = new FreeTypeFont(freeType, filename, size);
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
    
    public static void setTextInputElement(TextInputElement currElement) {
        textInput = currElement;
        if(currElement == null) mouse.setCursorShape(GLFW_ARROW_CURSOR);
    }
    
    public void initializeObservers(Observable observable) {
        widgets.forEach((name, widget) -> observable.addObserver(widget));
    }
    
}