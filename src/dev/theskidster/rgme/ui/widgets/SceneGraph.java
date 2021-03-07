package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.TestObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.elements.Scrollbar;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.LinkedHashSet;

/**
 * @author J Hoffman
 * Created: Mar 4, 2021
 */

public final class SceneGraph extends Widget {
    
    private int prevCategoryIndex = -1;
    private int currCategoryIndex = -1;
    
    private final Rectangle seperator = new Rectangle(0, 0, 2, 224);
    
    private final Category[] categories = new Category[2];
    
    public SceneGraph() {
        super(0, 28, 320, 0, "Scene Graph", 5, 0);
        
        bounds.xPos = 450;
        bounds.yPos = 200;
        bounds.height = 264;
        
        categories[0] = new Category("Visible Geometry");
        categories[1] = new Category("Bounding Volumes");
        
        elements = new LinkedHashSet<>() {{
            add(new Scrollbar((int) (bounds.width - 24), 40, true, 176));
        }};
        
        categories[0].addGameObject(new TestObject());
        categories[0].addGameObject(new TestObject());
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
        resetMouseShape(mouse);
        
        elements.forEach(element -> element.update(bounds.xPos, bounds.yPos, mouse));
        
        int verticalOffset = 0;
        
        for(int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            
            category.update(bounds.xPos, bounds.yPos + verticalOffset, mouse); //TODO: add scrollbar offset
            verticalOffset += 28 * category.getLength();
            
            if(mouse.clicked && category.onlyBoundsSelected()) {
                setCurrCategory(i, true);
            }
        }
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_DARK_GRAY, uiProgram);
        background.drawRectangle(titleBar, Color.RGME_LIGHT_GRAY, uiProgram);
        
        icon.render(uiProgram);
        font.drawString(title, bounds.xPos + 40, bounds.yPos + 26, 1, Color.RGME_WHITE, uiProgram);
        
        elements.forEach(element -> element.render(uiProgram, background, font));
        
        //TODO: specify scissor box
        for(Category category : categories) {
            category.render(uiProgram, background, font);
        }
        
        background.drawRectangle(seperator, Color.RGME_BLACK, uiProgram);
    }
    
    private void setCurrCategory(int index, boolean clicked) {
        for(int i = 0; i < categories.length; i++) {
            categories[i].selected = (i == index);
            categories[i].clicked  = clicked && (i == index);
        }
        
        if(index != prevCategoryIndex) {
            currCategoryIndex = index;
            prevCategoryIndex = currCategoryIndex;
        }
    }
    
}