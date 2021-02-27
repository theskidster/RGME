package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.LinkedHashSet;
import static org.lwjgl.glfw.GLFW.GLFW_HAND_CURSOR;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public class MenuBar extends Widget {

    Rectangle bar = new Rectangle(0, 0, 0, 28);
    
    public MenuBar() {
        super(0, 0);
        
        elements = new LinkedHashSet() {{}};
    }
    
    @Override
    public void update(int viewportWidth, int viewportHeight, Mouse mouse) {
        bar.width = viewportWidth;
        
        elements.forEach(element -> element.update(xPos, yPos, mouse));
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bar, Color.GRAY, uiProgram);
        
        elements.forEach(element -> element.render(uiProgram, background, font));
    }
    
}