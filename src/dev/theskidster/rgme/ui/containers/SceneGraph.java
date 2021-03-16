package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.commands.MoveShape;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;

/**
 * @author J Hoffman
 * Created: Mar 14, 2021
 */

public final class SceneGraph extends Container {

    Rectangle button;
    
    public SceneGraph() {
        super(700, 250, TOOLBAR_WIDTH, 264, "Scene Graph", 5, 0);
        
        button = new Rectangle(bounds.xPos + 50, bounds.yPos + 100, 100, 32);
    }

    @Override
    public Command update(Mouse mouse) {
        if(clickedOnce(button, mouse)) {
            return new MoveShape(App.testObject, (float) Math.random(), (float) Math.random(), (float) Math.random());
        } else {
            return null;
        }
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderTitleBar(uiProgram, background, font);
        
        background.drawRectangle(button, Color.RGME_SLATE_GRAY, uiProgram);
        font.drawString("move shape", button.xPos + 3, button.yPos + 22, 1, Color.RGME_WHITE, uiProgram);
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateTitleBarIcon();
    }

}