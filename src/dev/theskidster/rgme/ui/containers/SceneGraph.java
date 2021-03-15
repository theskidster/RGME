package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.utils.Mouse;

/**
 * @author J Hoffman
 * Created: Mar 14, 2021
 */

public final class SceneGraph extends Container {

    public SceneGraph() {
        super(450, 250, TOOLBAR_WIDTH, 264, "Scene Graph", 5, 0);
    }

    @Override
    public void update(Mouse mouse) {
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        renderTitleBar(uiProgram, background, font);
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateTitleBarIcon();
    }

}