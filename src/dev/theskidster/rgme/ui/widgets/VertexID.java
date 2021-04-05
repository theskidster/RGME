package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Rectangle;

/**
 * @author J Hoffman
 * Created: Apr 4, 2021
 */

public final class VertexID {
    
    public final int index;
    
    public boolean selected;
    
    private final Color bgColor   = Color.random();
    private final Color textColor = Color.RGME_WHITE;
    public final Rectangle bounds = new Rectangle(0, 0, TOOLBAR_WIDTH - 66, 28);

    public VertexID(int index) {
        this.index = index;
    }
    
    public void update(Rectangle viewport, int order, float verticalOffset) {
        bounds.xPos = viewport.xPos;
        bounds.yPos = ((viewport.yPos + (28 * order)) - 28) + verticalOffset;
        
        //TODO: add selecton
    }
    
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, bgColor, uiProgram);
        font.drawString("Vertex " + index, bounds.xPos + 4, bounds.yPos + 20, 1, textColor, uiProgram);
    }
    
}