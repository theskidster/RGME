package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Mouse;
import java.util.HashMap;

/**
 * @author J Hoffman
 * Created: Mar 13, 2021
 */

public class TestContainer extends Container {

    public TestContainer() {
        super(600, 200, 400, 400, "test", 5, 1);
        
        widgets = new HashMap<>() {{
            
        }};
    }

    @Override
    public void update(Mouse mouse) {        
        if(clickedOnce(bounds, mouse)) {
            System.out.println("test container clicked");
        }
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderTitleBar(uiProgram, background, font);
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateTitleBarIcon();
    }

}