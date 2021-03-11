package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.scene.WorldLight;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.UI;
import dev.theskidster.rgme.ui.elements.Scrollbar;
import dev.theskidster.rgme.ui.elements.TextArea;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author J Hoffman
 * Created: Mar 4, 2021
 */

public final class SceneGraph extends Widget {
    
    public static final int TOOLBAR_WIDTH = 360;
    
    private int currCategoryIndex;
    
    private static boolean showTextArea;
    
    private final Rectangle scissorBox = new Rectangle();
    private final Rectangle seperator  = new Rectangle(0, 0, 2, 224);
    private final Scrollbar scrollbar;
    
    private static TextArea textArea;
    private static Member selectedMember;
    
    private final Category[] categories = new Category[6];
    
    private final Map<Integer, Integer> categoryLengths = new HashMap<>();
    
    public SceneGraph() {
        super(0, 28, TOOLBAR_WIDTH, 264, "Scene Graph", 5, 0);
        
        bounds.xPos = UI.getViewWidth() - bounds.width;
        bounds.yPos = 28;
        
        categories[0] = new Category("Visible Geometry");
        categories[1] = new Category("Bounding Volumes");
        categories[2] = new Category("Trigger Boxes");
        categories[3] = new Category("Light Sources");
        categories[4] = new Category("Entities");
        categories[5] = new Category("Instances");
        
        scrollbar = new Scrollbar((int) (bounds.width - 24), 40, true, 176, 224);
        textArea  = new TextArea(0, 1, 200, bounds.xPos, bounds.yPos, false);
        
        elements = new LinkedHashSet<>() {{
            add(scrollbar);
            add(textArea);
        }};
        
        //TODO: get specified world light or provide default one
        categories[3].addGameObject(new WorldLight());
    }

    @Override
    public void update(int viewportWidth, int viewportHeight, Mouse mouse) {
        bounds.xPos = (int) (viewportWidth - bounds.width);
        bounds.yPos = 28;
        
        hovered = bounds.contains(mouse.cursorPos);
        
        seperator.xPos = bounds.xPos + 28;
        seperator.yPos = bounds.yPos + 40;
        
        scissorBox.xPos   = bounds.xPos;
        scissorBox.yPos   = viewportHeight - (bounds.yPos + bounds.height);
        scissorBox.width  = bounds.xPos + bounds.width;
        scissorBox.height = bounds.height - 40;
        
        updateTitleBarPos(viewportWidth, viewportHeight);
        
        if(showTextArea) {
            textArea.update(
                    selectedMember.bounds.xPos + 80, 
                    selectedMember.bounds.yPos, 
                    mouse);
            
            textArea.scroll();
            
            textArea.scissorBox.yPos   = scissorBox.yPos;
            textArea.scissorBox.height = scissorBox.height;
            
            if(!textArea.hasFocus()) {
                if(!categories[currCategoryIndex].containsObjectByName(textArea.getText()) && 
                   !(textArea.getText().length() == 0)) {
                    categories[currCategoryIndex].removeObjectName(selectedMember.gameObject.getName());
                    selectedMember.gameObject.setName(textArea.getText());
                }
                
                showTextArea = false;
                mouse.setCursorShape(GLFW_ARROW_CURSOR);
            }
        }
        
        scrollbar.update(bounds.xPos, bounds.yPos, mouse);
        
        int verticalOffset = scrollbar.getContentScrollOffset();
        
        for(int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            
            category.setVerticalOffset(verticalOffset);
            category.setParentDimensions(bounds.width, bounds.height);
            category.update(bounds.xPos, bounds.yPos, mouse);
            
            verticalOffset += 28 * category.getLength();
            
            categoryLengths.put(i, 28 * category.getLength());
            
            if(!titleBar.contains(mouse.cursorPos) && !(mouse.cursorPos.y > bounds.yPos + bounds.height)) {
                if(mouse.clicked && category.onlyBoundsSelected() && mouse.button.equals("left")) {
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
        
        glEnable(GL_SCISSOR_TEST);
        glScissor((int) scissorBox.xPos, (int) scissorBox.yPos, (int) scissorBox.width, (int) scissorBox.height);
            for(Category category : categories) category.render(uiProgram, background, font);
            if(showTextArea) textArea.render(uiProgram, background, font);
        glDisable(GL_SCISSOR_TEST);
        
        scrollbar.render(uiProgram, background, font);
        
        background.drawRectangle(seperator, Color.RGME_BLACK, uiProgram);
    }
    
    void setCurrCategory(int index, boolean clicked) {
        currCategoryIndex = index;
        
        for(int i = 0; i < categories.length; i++) {
            categories[i].selected = (i == index);
            categories[i].clicked  = (i == index) && clicked;
            
            if(((i != index) ^ clicked) || (i != index) && clicked) {
                categories[i].unselectMembers();
            }
        }
    }
    
    static void showTextArea(boolean value, Member member) {
        showTextArea   = value;
        selectedMember = member;
        
        textArea.setText(selectedMember.gameObject.getName());
    }
    
    GameObject getSelectedGameObject() {
        return Category.currGameObject;
    }
    
}