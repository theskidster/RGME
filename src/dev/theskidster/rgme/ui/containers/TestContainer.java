package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.widgets.TextArea;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Observable;
import java.util.ArrayList;
import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;

/**
 * @author J Hoffman
 * Created: Mar 13, 2021
 */

public class TestContainer extends Container {

    private TextArea textArea1;
    private TextArea textArea2;
    private final Observable observable = new Observable(this);
    
    public TestContainer() {
        super(450, 200, 400, 400, "test", 5, 1);
        
        textArea1 = new TextArea(100, 200, 170, bounds.xPos, bounds.yPos, true);
        //textArea2 = new TextArea(100, 50, 100, bounds.xPos, bounds.yPos, false);
        
        widgets = new ArrayList<>() {{
            add(textArea1);
            //add(textArea2);
        }};
        
        //observable.addObserver(textArea2);
    }

    @Override
    public Command update(Mouse mouse) {        
        textArea1.update(mouse);
        //textArea2.update(mouse);
        
        if(!widgetHovered(mouse.cursorPos)) {
            mouse.setCursorShape(GLFW_ARROW_CURSOR);
        }
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_MEDIUM_GRAY, uiProgram);
        renderTitleBar(uiProgram, background, font);
        
        textArea1.render(uiProgram, background, font);
        //textArea2.render(uiProgram, background, font);
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        relocateTitleBar();
        
        textArea1.relocate(bounds.xPos, bounds.yPos);
        //textArea2.relocate(bounds.xPos, bounds.yPos);
    }

}