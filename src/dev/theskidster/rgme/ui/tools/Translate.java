package dev.theskidster.rgme.ui.tools;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.containers.ToolBox;
import dev.theskidster.rgme.ui.widgets.SpinBox;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import java.util.LinkedList;

/**
 * @author J Hoffman
 * Created: Mar 24, 2021
 */

public class Translate extends Tool {

    float parentPosX;
    float parentPosY;
    
    private SpinBox xPosInput;
    
    public Translate(float parentPosX, float parentPosY) {
        super("Translate", 0, 2);
        
        xPosInput = new SpinBox(20, 100, 50, parentPosX, parentPosY);
        
        widgets = new LinkedList<>() {{
            add(xPosInput);
        }};
    }

    @Override
    public Command update(Mouse mouse, ToolBox toolBox, GameObject selectedGameObject, float parentPosX, float parentPosY, int order) {
        updateButton(mouse, toolBox, parentPosX, parentPosY, order);
        
        this.parentPosX = parentPosX;
        this.parentPosY = parentPosY;
        
        return null;
    }
    
    @Override
    public Command update(Mouse mouse) {
        //Unused.
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderButton(uiProgram, background);
        
        if(selected) {
            font.drawString("Position X:", parentPosX + 45, parentPosY + 20, 1, Color.RGME_WHITE, uiProgram);
            font.drawString("Y:", parentPosX + 110, parentPosY + 40, 1, Color.RGME_WHITE, uiProgram);
            font.drawString("Z:", parentPosX + 110, parentPosY + 60, 1, Color.RGME_WHITE, uiProgram);
            
            xPosInput.render(uiProgram, background, font);
        }
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
    }

}