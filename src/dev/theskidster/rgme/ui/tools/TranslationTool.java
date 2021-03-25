package dev.theskidster.rgme.ui.tools;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;

/**
 * @author J Hoffman
 * Created: Mar 24, 2021
 */

public class TranslationTool extends Tool {

    public TranslationTool() {
        super("Translate", 0, 2);
    }

    @Override
    public Command update(Mouse mouse, GameObject selectedGameObject, float parentPosX, float parentPosY, int order) {
        updateButton(mouse.cursorPos, parentPosX, parentPosY, order);
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderButton(uiProgram, background);
    }

}