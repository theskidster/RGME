package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Mouse;

/**
 * @author J Hoffman
 * Created: Mar 13, 2021
 */

public interface Renderable {
    
    void update(Mouse mouse);
    void render(Program uiProgram, Background background, FreeTypeFont font);
    void updatePosX(int parentPosX);
    void updatePosY(int parentPosY);
    
}