package dev.theskidster.rgme.ui.tools;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.containers.ToolBox;
import dev.theskidster.rgme.utils.Mouse;
import java.util.LinkedList;

/**
 * @author J Hoffman
 * Created: Mar 24, 2021
 */

public class Scale extends Tool {

    public Scale() {
        super("Scale", 2, 2);
        
        widgets = new LinkedList<>();
    }

    @Override
    public Command update(Mouse mouse, ToolBox toolBox, GameObject selectedGameObject, float parentPosX, float parentPosY, int order) {
        updateButton(mouse, toolBox, parentPosX, parentPosY, order);
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderButton(uiProgram, background);
    }

}