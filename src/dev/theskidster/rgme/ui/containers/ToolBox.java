package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;

/**
 * @author J Hoffman
 * Created: Mar 23, 2021
 */

public class ToolBox extends Container {

    private final SceneExplorer explorer;
    
    public ToolBox(SceneExplorer explorer) {
        super(0, explorer.getTotalLength(), TOOLBAR_WIDTH, 0, "Tool Box", 10, 0);
        this.explorer = explorer;
    }

    @Override
    public Command update(Mouse mouse) {
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_MEDIUM_GRAY, uiProgram);
        renderTitleBar(uiProgram, background, font);
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        bounds.xPos = parentPosX - bounds.width;
        bounds.height = parentPosY - explorer.getTotalLength();
        
        relocateTitleBar();
    }

}