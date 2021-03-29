package dev.theskidster.rgme.ui.tools;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.commands.MoveObject;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.containers.ToolBox;
import dev.theskidster.rgme.ui.widgets.SpinBox;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import java.util.LinkedList;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 24, 2021
 */

public class Translate extends Tool {
    
    private final Vector3f prevVal = new Vector3f();
    
    private SpinBox xPosInput;
    private SpinBox yPosInput;
    private SpinBox zPosInput;
    
    public Translate(float parentPosX, float parentPosY, int order) {
        super("Translate", 0, 2, order);
        
        xPosInput = new SpinBox(50, 100, 120, bounds.xPos, bounds.yPos, 0, false);
        yPosInput = new SpinBox(50, 140, 120, bounds.xPos, bounds.yPos, 0, false);
        zPosInput = new SpinBox(50, 180, 120, bounds.xPos, bounds.yPos, 0, false);
        
        widgets = new LinkedList<>() {{
            add(xPosInput);
        }};
    }
    
    @Override
    public Command update(Mouse mouse, ToolBox toolBox, GameObject selectedGameObject) {
        updateButton(mouse, toolBox);
        xPosInput.update(mouse);
        yPosInput.update(mouse);
        zPosInput.update(mouse);
        
        if(xPosInput.getValue() != prevVal.x || yPosInput.getValue() != prevVal.y || zPosInput.getValue() != prevVal.z) {
            prevVal.set(xPosInput.getValue(), yPosInput.getValue(), zPosInput.getValue());
            return new MoveObject(selectedGameObject, prevVal);
        }
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderButton(uiProgram, background);
        
        if(selected) {
            font.drawString("Position X:", parentPosX + 45, parentPosY + 30, 1, Color.RGME_WHITE, uiProgram);
            font.drawString("Position Y:", parentPosX + 45, parentPosY + 80, 1, Color.RGME_WHITE, uiProgram);
            font.drawString("Position Z:", parentPosX + 45, parentPosY + 100, 1, Color.RGME_WHITE, uiProgram);
            
            xPosInput.render(uiProgram, background, font);
            yPosInput.render(uiProgram, background, font);
            zPosInput.render(uiProgram, background, font);
        }
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateButton(parentPosX, parentPosY);
        xPosInput.relocate(bounds.xPos, bounds.yPos);
        yPosInput.relocate(bounds.xPos, bounds.yPos);
        zPosInput.relocate(bounds.xPos, bounds.yPos);
    }

}