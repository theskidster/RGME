package dev.theskidster.rgme.ui.tools;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.commands.ScaleGameObject;
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

public class Scale extends Tool {

    private float prevScaleVal;
    
    private SpinBox scaleInput;
    
    public Scale(int order, GameObject selectedGameObject) {
        super("Scale", 2, 2, order);
        
        prevScaleVal = (selectedGameObject != null) ? selectedGameObject.getScale() : 1;
        scaleInput   = new SpinBox(140, 15, 120, parentPosX, parentPosY, 0, false, Float.POSITIVE_INFINITY, false);
        
        scaleInput.setValue(prevScaleVal);
        
        widgets = new LinkedList<>() {{
            add(scaleInput);
        }};
    }
    
    @Override
    public Command update(Mouse mouse, ToolBox toolBox, GameObject selectedGameObject) {
        updateButton(mouse, toolBox);
        
        if(selected) scaleInput.update(mouse);
        
        if(scaleInput.getValue() != prevScaleVal) {
            prevScaleVal = scaleInput.getValue();
            return new ScaleGameObject(selectedGameObject, prevScaleVal);
        }
        
        if(scaleInput.getValue() != selectedGameObject.getScale()) {
            prevScaleVal = selectedGameObject.getScale();
            scaleInput.setValue(selectedGameObject.getScale());
        }
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderButton(uiProgram, background);
        
        if(selected) {
            font.drawString("Scale:", parentPosX + 50, parentPosY + 35, 1, Color.RGME_WHITE, uiProgram);
            scaleInput.render(uiProgram, background, font);
        }
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateButton(parentPosX, parentPosY);
        scaleInput.relocate(parentPosX, parentPosY);
    }

}