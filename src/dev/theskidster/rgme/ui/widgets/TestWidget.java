package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.LinkedHashSet;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public class TestWidget extends Widget {

    Rectangle bar = new Rectangle(200, 200, 600, 400);
    
    public TestWidget() {
        super(0, 0);
        
        elements = new LinkedHashSet() {{}};
    }
    
    @Override
    public void update(int viewportWidth, int viewportHeight, Mouse mouse) {
        elements.forEach(element -> element.update(xPos, yPos, mouse));
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bar, Color.RGME_MEDIUM_GRAY, uiProgram);
        elements.forEach(element -> element.render(uiProgram, background, font));
    }
    
}