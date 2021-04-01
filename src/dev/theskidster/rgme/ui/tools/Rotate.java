package dev.theskidster.rgme.ui.tools;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.commands.RotateGameObject;
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

public class Rotate extends Tool {

    private final Vector3f prevVal = new Vector3f();
    
    private SpinBox xRotInput;
    private SpinBox yRotInput;
    private SpinBox zRotInput;
    
    public Rotate(float parentPosX, float parentPosY, int order) {
        super("Rotate", 1, 2, order);
        
        xRotInput = new SpinBox(140, 15, 120, parentPosX, parentPosY, 0, false, 180, true);
        yRotInput = new SpinBox(140, 60, 120, parentPosX, parentPosY, 0, false, 180, true);
        zRotInput = new SpinBox(140, 105, 120, parentPosX, parentPosY, 0, false, 180, true);
        
        widgets = new LinkedList<>() {{
            add(xRotInput);
            add(yRotInput);
            add(zRotInput);
        }};
    }
    
    @Override
    public Command update(Mouse mouse, ToolBox toolBox, GameObject selectedGameObject) {
        updateButton(mouse, toolBox);
        
        if(selected) {
            xRotInput.update(mouse);
            yRotInput.update(mouse);
            zRotInput.update(mouse);
        }
        
        if(xRotInput.getValue() != prevVal.x || yRotInput.getValue() != prevVal.y || zRotInput.getValue() != prevVal.z) {
            prevVal.set(xRotInput.getValue(), yRotInput.getValue(), zRotInput.getValue());
            return new RotateGameObject(selectedGameObject, prevVal);
        }
        
        if(xRotInput.getValue() != selectedGameObject.getRotation().x || yRotInput.getValue() != selectedGameObject.getRotation().y ||
           zRotInput.getValue() != selectedGameObject.getRotation().z) {
            prevVal.set(selectedGameObject.getRotation());
            xRotInput.setValue(selectedGameObject.getRotation().x);
            yRotInput.setValue(selectedGameObject.getRotation().y);
            zRotInput.setValue(selectedGameObject.getRotation().z);
        }
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderButton(uiProgram, background);
        
        if(selected) {
            font.drawString("Rotation X:", parentPosX + 50, parentPosY + 35, 1, Color.RGME_WHITE, uiProgram);
            font.drawString("Rotation Y:", parentPosX + 50, parentPosY + 80, 1, Color.RGME_WHITE, uiProgram);
            font.drawString("Rotation Z:", parentPosX + 50, parentPosY + 125, 1, Color.RGME_WHITE, uiProgram);
            
            xRotInput.render(uiProgram, background, font);
            yRotInput.render(uiProgram, background, font);
            zRotInput.render(uiProgram, background, font);
        }
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateButton(parentPosX, parentPosY);
        xRotInput.relocate(parentPosX, parentPosY);
        yRotInput.relocate(parentPosX, parentPosY);
        zRotInput.relocate(parentPosX, parentPosY);
    }

}