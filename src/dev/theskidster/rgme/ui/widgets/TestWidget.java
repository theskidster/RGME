package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.elements.TextArea;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import java.util.LinkedHashSet;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public class TestWidget extends Widget {
    
    public TestWidget() {
        super(200, 200, 600, 400);
        
        elements = new LinkedHashSet() {{
            add(new TextArea(40, 60, 200, bounds.xPos, bounds.yPos));
            add(new TextArea(100, 300, 120, bounds.xPos, bounds.yPos));
        }};
    }
    
    @Override
    public void update(int viewportWidth, int viewportHeight, Mouse mouse) {
        resetMouseShape(mouse);
        elements.forEach(element -> element.update(bounds.xPos, bounds.yPos, mouse));
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_MEDIUM_GRAY, uiProgram);
        elements.forEach(element -> element.render(uiProgram, background, font));
    }
    
}