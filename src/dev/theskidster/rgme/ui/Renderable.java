package dev.theskidster.rgme.ui;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;

/**
 * @author J Hoffman
 * Created: Mar 26, 2021
 */

public interface Renderable {
    
    void render(Program uiProgram, Background background, FreeTypeFont font);
    
}