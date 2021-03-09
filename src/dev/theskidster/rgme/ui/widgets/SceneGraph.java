package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.scene.TestObject;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.elements.Scrollbar;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author J Hoffman
 * Created: Mar 4, 2021
 */

public final class SceneGraph extends Widget {
    
    private final Rectangle scissorBox = new Rectangle();
    private final Rectangle seperator  = new Rectangle(0, 0, 2, 224);
    private final Scrollbar scrollbar;
    
    private final Category[] categories = new Category[6];
    
    private final Map<Integer, Integer> categoryLengths = new HashMap<>();
    
    public SceneGraph() {
        super(0, 28, 320, 0, "Scene Graph", 5, 0);
        
        bounds.xPos   = 450;
        bounds.yPos   = 200;
        bounds.height = 264;
        
        categories[0] = new Category("Visible Geometry");
        categories[1] = new Category("Bounding Volumes");
        categories[2] = new Category("Trigger Boxes");
        categories[3] = new Category("Light Sources");
        categories[4] = new Category("Entities");
        categories[5] = new Category("Instances");
        
        scrollbar = new Scrollbar((int) (bounds.width - 24), 40, true, 176, 224);
        
        elements = new LinkedHashSet<>() {{
            add(scrollbar);
        }};
        
        //TODO: remove temp members, include default world lightsource.
        categories[0].addGameObject(new TestObject());
        categories[0].addGameObject(new TestObject());
        categories[1].addGameObject(new TestObject());
        categories[2].addGameObject(new TestObject());
        categories[2].addGameObject(new TestObject());
        categories[2].addGameObject(new TestObject());
        categories[2].addGameObject(new TestObject());
        categories[3].addGameObject(new TestObject());
        categories[3].addGameObject(new TestObject());
        categories[4].addGameObject(new TestObject());
        categories[5].addGameObject(new TestObject());
        categories[5].addGameObject(new TestObject());
        categories[5].addGameObject(new TestObject());
    }

    @Override
    public void update(int viewportWidth, int viewportHeight, Mouse mouse) {
        /*
        bounds.xPos   = (int) (viewportWidth - bounds.width);
        bounds.height = viewportHeight - bounds.yPos;
        */
        
        hovered = bounds.contains(mouse.cursorPos);
        
        seperator.xPos = bounds.xPos + 28;
        seperator.yPos = bounds.yPos + 40;
        
        scissorBox.xPos   = bounds.xPos;
        scissorBox.yPos   = viewportHeight - (bounds.yPos + bounds.height);
        scissorBox.width  = bounds.xPos + bounds.width;
        scissorBox.height = bounds.height - 40;
        
        updateTitleBarPos(viewportWidth, viewportHeight);
        resetMouseShape(mouse);
        
        elements.forEach(element -> element.update(bounds.xPos, bounds.yPos, mouse));
        
        int verticalOffset = scrollbar.getContentScrollOffset();
        
        for(int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            
            category.setVerticalOffset(verticalOffset);
            category.setParentDimensions(bounds.width, bounds.height);
            category.update(bounds.xPos, bounds.yPos, mouse);
            
            verticalOffset += 28 * category.getLength();
            
            categoryLengths.put(i, 28 * category.getLength());
            
            if(!titleBar.contains(mouse.cursorPos) && !(mouse.cursorPos.y > bounds.yPos + bounds.height)) {
                if(mouse.clicked && category.onlyBoundsSelected()) {
                    setCurrCategory(i, true);
                } else if(category.hasSelectedMember()) {
                    setCurrCategory(i, false);
                }
            }
        }
        
        scrollbar.setContentLength(categoryLengths);
        scrollbar.parentHovered = hovered;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_DARK_GRAY, uiProgram);
        background.drawRectangle(titleBar, Color.RGME_LIGHT_GRAY, uiProgram);
        
        icon.render(uiProgram);
        font.drawString(title, bounds.xPos + 40, bounds.yPos + 26, 1, Color.RGME_WHITE, uiProgram);
        
        elements.forEach(element -> element.render(uiProgram, background, font));
        
        glEnable(GL_SCISSOR_TEST);
        glScissor((int) scissorBox.xPos, (int) scissorBox.yPos, (int) scissorBox.width, (int) scissorBox.height);
        for(Category category : categories) {
            category.render(uiProgram, background, font);
        }
        glDisable(GL_SCISSOR_TEST);
        
        background.drawRectangle(seperator, Color.RGME_BLACK, uiProgram);
    }
    
    void setCurrCategory(int index, boolean clicked) {
        for(int i = 0; i < categories.length; i++) {
            categories[i].selected = (i == index);
            categories[i].clicked  = (i == index) && clicked;
            
            if(((i != index) ^ clicked) || (i != index) && clicked) {
                categories[i].unselectMembers();
            }
        }
    }
    
    GameObject getSelectedGameObject() {
        return Category.currGameObject;
    }
    
}