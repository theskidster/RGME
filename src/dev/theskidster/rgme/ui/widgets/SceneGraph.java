package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import java.util.LinkedHashSet;

/**
 * @author J Hoffman
 * Created: Mar 4, 2021
 */

public final class SceneGraph extends Widget {

    public SceneGraph() {
        super(0, 28, 320, 0);
        
        elements = new LinkedHashSet() {{
            
        }};
    }

    @Override
    public void update(int viewportWidth, int viewportHeight, Mouse mouse) {
        bounds.xPos   = (int) (viewportWidth - bounds.width);
        bounds.height = viewportHeight - bounds.yPos;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_MEDIUM_GRAY, uiProgram);
    }

}