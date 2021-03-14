package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.elements.Scrollbar;
import dev.theskidster.rgme.ui.elements.TextArea;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import java.beans.PropertyChangeEvent;
import java.util.LinkedHashMap;

/**
 * @author J Hoffman
 * Created: Feb 26, 2021
 */

public class TestWidget extends Widget {
    
    public TestWidget() {
        super(200, 200, 600, 400);
        
        elements = new LinkedHashMap<>() {{
            put("scrollbar", new Scrollbar(400, 30, true, 200, 0));
            put("textArea", new TextArea(200, 200, 120, bounds.xPos, bounds.yPos, true));
        }};
    }
    
    @Override
    public void update(int viewportWidth, int viewportHeight, Mouse mouse) {
        hovered = bounds.contains(mouse.cursorPos);
        elements.values().forEach(element -> element.update(bounds.xPos, bounds.yPos, mouse));
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_MEDIUM_GRAY, uiProgram);
        elements.values().forEach(element -> element.render(uiProgram, background, font));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()) {
            case "windowWidth" -> {
                
            }
            
            case "windowHeight" -> {
                
            }
        }
    }
    
}