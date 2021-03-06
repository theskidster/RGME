package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.elements.Element;
import dev.theskidster.rgme.ui.elements.Scrollbar;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author J Hoffman
 * Created: Mar 4, 2021
 */

public final class SceneGraph extends Widget {
    
    private final Rectangle seperator = new Rectangle(0, 0, 2, 224);
    
    private final Map<String, Category> categories;
    
    public SceneGraph() {
        super(0, 28, 320, 0, "Scene Graph", 5, 0);
        
        bounds.xPos = 450;
        bounds.yPos = 200;
        bounds.height = 264;
        
        categories = new HashMap<>() {{
            put("Visible Geometry", new Category("Visible Geometry"));
            put("Bounding Volumes", new Category("Bounding Volumes"));
        }};
        
        elements = new LinkedHashSet() {{
            add(new Scrollbar((int) (bounds.width - 24), 40, true, 176));
            add(categories.get("Visible Geometry"));
            add(categories.get("Bounding Volumes"));
        }};
    }

    @Override
    public void update(int viewportWidth, int viewportHeight, Mouse mouse) {
        /*
        bounds.xPos   = (int) (viewportWidth - bounds.width);
        bounds.height = viewportHeight - bounds.yPos;
        */
        
        seperator.xPos = bounds.xPos + 28;
        seperator.yPos = bounds.yPos + 40;
        
        updateTitleBarPos(viewportWidth, viewportHeight);
        //resetMouseShape(mouse);
        
        int verticalOffset = -28;
        
        for(Element element : elements) {
            if(element instanceof Category) {
                Category category = ((Category) element);
                
                verticalOffset += category.getLength();
                
                element.update(bounds.xPos, bounds.yPos + verticalOffset, mouse);
            } else {
                if(element != null) {
                    element.update(bounds.xPos, bounds.yPos, mouse);
                }
            }
        }
        
        elements.removeIf(element -> element.remove);
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_MEDIUM_GRAY, uiProgram);
        background.drawRectangle(titleBar, Color.RGME_LIGHT_GRAY, uiProgram);
        
        icon.render(uiProgram);
        font.drawString(title, bounds.xPos + 40, bounds.yPos + 26, 1, Color.RGME_WHITE, uiProgram);
        
        elements.forEach(element -> element.render(uiProgram, background, font));
        
        background.drawRectangle(seperator, Color.RGME_BLACK, uiProgram);
    }

}