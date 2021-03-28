package dev.theskidster.rgme.ui.tools;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.containers.ToolBox;
import dev.theskidster.rgme.ui.widgets.SpinBox;
import dev.theskidster.rgme.ui.widgets.TextArea;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import java.util.LinkedList;

/**
 * @author J Hoffman
 * Created: Mar 24, 2021
 */

public class Translate extends Tool {
    
    private SpinBox xPosInput;
    private TextArea textArea;
    
    public Translate(float parentPosX, float parentPosY, int order) {
        super("Translate", 0, 2, order);
        
        xPosInput = new SpinBox(50, 100, 120, bounds.xPos, bounds.yPos);
        textArea = new TextArea(200, 100, 140, bounds.xPos, bounds.yPos, false);
        
        widgets = new LinkedList<>() {{
            add(xPosInput);
            add(textArea);
        }};
    }
    
    @Override
    public Command update(Mouse mouse, ToolBox toolBox, GameObject selectedGameObject) {
        updateButton(mouse, toolBox);
        xPosInput.update(mouse);
        textArea.update(mouse);
        
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
            textArea.render(uiProgram, background, font);
        }
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateButton(parentPosX, parentPosY);
        
        System.out.println(bounds.xPos + ", " + bounds.yPos + ", " + bounds.width + " " + bounds.height);
        
        xPosInput.relocate(bounds.xPos, bounds.yPos);
        textArea.relocate(bounds.xPos, bounds.yPos);
    }

}