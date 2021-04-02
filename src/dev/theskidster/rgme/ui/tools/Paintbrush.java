package dev.theskidster.rgme.ui.tools;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.containers.ToolBox;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import java.util.LinkedList;

/**
 * @author J Hoffman
 * Created: Mar 24, 2021
 */

public class Paintbrush extends Tool {

    private static String[] notice;
    
    {
        notice = new String[6];
        
        notice[0] = "*NOTICE* transformations made";
        notice[1] = "to this shape will offset the accuracy";
        notice[2] = "of the mesh painbrush.";
        notice[3] = "It's a large hammer intended to be used";
        notice[4] = "in the initial phases of shape";
        notice[5] = "sculpting, so swing it carefully.";
    }
    
    public Paintbrush(int order) {
        super("Mesh Paintbrush", 5, 2, order);
        
        widgets = new LinkedList<>();
    }
    
    @Override
    public Command update(Mouse mouse, ToolBox toolBox, GameObject selectedGameObject) {
        updateButton(mouse, toolBox);
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderButton(uiProgram, background);
        
        if(selected) {
            font.drawString(notice[0], parentPosX + 42, parentPosY + 20, 1, Color.RGME_WHITE, uiProgram);
            font.drawString(notice[1], parentPosX + 42, parentPosY + 40, 1, Color.RGME_WHITE, uiProgram);
            font.drawString(notice[2], parentPosX + 42, parentPosY + 60, 1, Color.RGME_WHITE, uiProgram);
            font.drawString(notice[3], parentPosX + 42, parentPosY + 100, 1, Color.RGME_WHITE, uiProgram);
            font.drawString(notice[4], parentPosX + 42, parentPosY + 120, 1, Color.RGME_WHITE, uiProgram);
            font.drawString(notice[5], parentPosX + 42, parentPosY + 140, 1, Color.RGME_WHITE, uiProgram);
        }
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateButton(parentPosX, parentPosY);
    }
    
}