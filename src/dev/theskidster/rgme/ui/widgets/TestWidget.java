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

    Rectangle bar = new Rectangle(400, 200, 300, 280);
    Icon icon     = new Icon(20, 20);
    
    public TestWidget() {
        super(0, 0);
        
        elements = new LinkedHashSet() {{}};
        
        icon.position.set(bar.xPos + 20, bar.yPos + 40);
        icon.setSubImage(0, 1);
    }
    
    @Override
    public void update(int viewportWidth, int viewportHeight, Mouse mouse) {
        elements.forEach(element -> element.update(xPos, yPos, mouse));
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bar, Color.DARK_GRAY, uiProgram);
        elements.forEach(element -> element.render(uiProgram, background, font));
        icon.render(uiProgram);
    }
    
}