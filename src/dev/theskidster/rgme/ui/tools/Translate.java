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
        
        xPosInput = new SpinBox(140, 15, 120, parentPosX, parentPosY, 0, false);
        yPosInput = new SpinBox(140, 60, 120, parentPosX, parentPosY, 0, false);
        zPosInput = new SpinBox(140, 105, 120, parentPosX, parentPosY, 0, false);
        
        widgets = new LinkedList<>() {{
            add(xPosInput);
            add(yPosInput);
            add(zPosInput);
        }};
    }
    
    @Override
    public Command update(Mouse mouse, ToolBox toolBox, GameObject selectedGameObject) {
        updateButton(mouse, toolBox);
        
        if(selected) {
            xPosInput.update(mouse);
            yPosInput.update(mouse);
            zPosInput.update(mouse);
        }
        
        if(xPosInput.getValue() != prevVal.x || yPosInput.getValue() != prevVal.y || zPosInput.getValue() != prevVal.z) {
            prevVal.set(xPosInput.getValue(), yPosInput.getValue(), zPosInput.getValue());
            return new MoveObject(selectedGameObject, prevVal);
        }
        
        if(xPosInput.getValue() != selectedGameObject.getPosition().x || yPosInput.getValue() != selectedGameObject.getPosition().y ||
           zPosInput.getValue() != selectedGameObject.getPosition().z) {
            prevVal.set(selectedGameObject.getPosition());
            xPosInput.setValue(selectedGameObject.getPosition().x);
            yPosInput.setValue(selectedGameObject.getPosition().y);
            zPosInput.setValue(selectedGameObject.getPosition().z);
        }
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderButton(uiProgram, background);
        
        if(selected) {
            font.drawString("Position X:", parentPosX + 50, parentPosY + 35, 1, Color.RGME_WHITE, uiProgram);
            font.drawString("Position Y:", parentPosX + 50, parentPosY + 80, 1, Color.RGME_WHITE, uiProgram);
            font.drawString("Position Z:", parentPosX + 50, parentPosY + 125, 1, Color.RGME_WHITE, uiProgram);
            
            xPosInput.render(uiProgram, background, font);
            yPosInput.render(uiProgram, background, font);
            zPosInput.render(uiProgram, background, font);
        }
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateButton(parentPosX, parentPosY);
        xPosInput.relocate(parentPosX, parentPosY);
        yPosInput.relocate(parentPosX, parentPosY);
        zPosInput.relocate(parentPosX, parentPosY);
    }

}