package dev.theskidster.rgme.ui;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Mouse;

/**
 * @author J Hoffman
 * Created: Mar 13, 2021
 */

public interface Renderable {
    
    Command update(Mouse mouse);
    void render(Program uiProgram, Background background, FreeTypeFont font);
    void relocate(float parentPosX, float parentPosY);
    
}