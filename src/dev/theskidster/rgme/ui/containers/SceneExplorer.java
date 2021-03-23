package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.commands.AddGameObject;
import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.commands.DeleteGameObject;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.scene.Scene;
import dev.theskidster.rgme.scene.VisibleGeometry;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.ui.widgets.Group;
import dev.theskidster.rgme.ui.widgets.Member;
import dev.theskidster.rgme.ui.widgets.Scrollbar;
import dev.theskidster.rgme.ui.widgets.TextArea;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Observable;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author J Hoffman
 * Created: Mar 14, 2021
 */

public final class SceneExplorer extends Container {
    
    public int groupIndex;
    
    public float windowWidth;
    public float windowHeight;
    
    public boolean outOfBounds;
    private boolean showTextArea;
    
    public GameObject selectedGameObject;
    private Rectangle memberBounds;
    
    private final Scene scene;
    private final Scrollbar scrollbar;
    private final TextArea textArea;
    private final Rectangle scissorBox  = new Rectangle();
    private final Rectangle seperator   = new Rectangle(0, 0, 2, 224);
    private final Rectangle addButton   = new Rectangle(0, 0, 24, 24);
    private final Rectangle subButton   = new Rectangle(0, 0, 24, 24);
    private final Icon addIcon          = new Icon(24, 24);
    private final Icon subIcon          = new Icon(24, 24);
    private final Observable observable = new Observable(this);
    
    private final Group[] groups = new Group[6];
    
    private final Map<Integer, Float> groupLengths = new HashMap<>();
    
    public SceneExplorer(Scene scene) {
        super(0, 28, TOOLBAR_WIDTH, 264, "Scene Explorer", 5, 0);
        this.scene = scene;
        
        groups[0] = new Group("Visible Geometry", this, scene.visibleGeometry);
        groups[1] = new Group("Bounding Volumes", this, scene.boundingVolumes);
        groups[2] = new Group("Trigger Boxes",    this, scene.triggerBoxes);
        groups[3] = new Group("Light Sources",    this, scene.lightSources);
        groups[4] = new Group("Entities",         this, scene.entities);
        groups[5] = new Group("Instances",        this, scene.instances);
        
        scrollbar = new Scrollbar(TOOLBAR_WIDTH - 24, 0, 176, 224);
        textArea  = new TextArea(0, 1, 200, bounds.xPos, bounds.yPos, false);
        
        observable.properties.put("viewportSize", null);
        observable.properties.put("viewportHeight", 0);
        
        for(Group group : groups) observable.addObserver(group);
        observable.addObserver(scrollbar);
        observable.addObserver(textArea);
        
        addIcon.setSubImage(8, 3);
        subIcon.setSubImage(8, 4);
        
        widgets = new ArrayList<>();
        widgets.add(textArea);
    }

    @Override
    public Command update(Mouse mouse) {
        outOfBounds = titleBar.contains(mouse.cursorPos) || mouse.cursorPos.y > bounds.yPos + bounds.height;
        
        int verticalOffset = scrollbar.getContentScrollOffset();
        
        for(int i = 0; i < groups.length; i++) {
            Group group = groups[i];
            
            group.setVerticalOffset(verticalOffset);
            group.update(mouse);
            
            verticalOffset += 28 * group.getLength();
            groupLengths.put(i, 28f * group.getLength());
        }
        
        scrollbar.setContentLength(groupLengths);
        scrollbar.parentHovered = hovered(mouse.cursorPos);
        scrollbar.update(mouse);
        
        if(showTextArea) {
            textArea.relocate(memberBounds.xPos + 80, memberBounds.yPos);
            textArea.update(mouse);
            
            textArea.scissorBox.yPos   = scissorBox.yPos;
            textArea.scissorBox.height = scissorBox.height;
            
            if(!textArea.hasFocus()) {
                if(!(textArea.getText().length() == 0)) {
                    selectedGameObject.setName(textArea.getText());
                }
                
                showTextArea = false;
            }
        }
        
        if(!widgetHovered(mouse.cursorPos)) mouse.setCursorShape(GLFW_ARROW_CURSOR);
        
        if(addButton.contains(mouse.cursorPos)) {
            if(mouse.clicked) addIcon.setColor(Color.RGME_WHITE);
            else              addIcon.setColor(Color.RGME_SILVER);
            
            if(clickedOnce(addButton, mouse)) {
                groups[groupIndex].setCollapsed(false);
                showTextArea = false;
                
                switch(groupIndex) {
                    default -> { return new AddGameObject(scene.visibleGeometry, new VisibleGeometry()); }
                    case 1  -> { return new AddGameObject(scene.boundingVolumes, new VisibleGeometry()); }
                    case 2  -> { return new AddGameObject(scene.triggerBoxes,    new VisibleGeometry()); }
                    case 3  -> { return new AddGameObject(scene.lightSources,    new VisibleGeometry()); }
                    case 4  -> { return new AddGameObject(scene.entities,        new VisibleGeometry()); }
                    case 5  -> { return new AddGameObject(scene.instances,       new VisibleGeometry()); }
                }
            }
        } else {
            addIcon.setColor(Color.RGME_SILVER);
        }
        
        if(subButton.contains(mouse.cursorPos)) {
            if(mouse.clicked) subIcon.setColor(Color.RGME_WHITE);
            else              subIcon.setColor(Color.RGME_SILVER);
            
            if(clickedOnce(subButton, mouse) && selectedGameObject != null) {
                showTextArea   = false;
                
                switch(groupIndex) {
                    default -> { return new DeleteGameObject(scene.visibleGeometry, selectedGameObject); }
                    case 1  -> { return new DeleteGameObject(scene.boundingVolumes, selectedGameObject); }
                    case 2  -> { return new DeleteGameObject(scene.triggerBoxes,    selectedGameObject); }
                    case 3  -> { return new DeleteGameObject(scene.lightSources,    selectedGameObject); }
                    case 4  -> { return new DeleteGameObject(scene.entities,        selectedGameObject); }
                    case 5  -> { return new DeleteGameObject(scene.instances,       selectedGameObject); }
                }
            }
        } else {
            subIcon.setColor(Color.RGME_SILVER);
        }
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_DARK_GRAY, uiProgram);
        renderTitleBar(uiProgram, background, font);
        
        glEnable(GL_SCISSOR_TEST);
        glScissor((int) scissorBox.xPos, (int) scissorBox.yPos, (int) scissorBox.width, (int) scissorBox.height);
            for(Group group : groups) group.render(uiProgram, background, font);
            if(showTextArea) textArea.render(uiProgram, background, font);
        glDisable(GL_SCISSOR_TEST);
        
        scrollbar.render(uiProgram, background, font);
        
        background.drawRectangle(seperator, Color.RGME_BLACK, uiProgram);
        
        addIcon.render(uiProgram);
        subIcon.render(uiProgram);
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        windowWidth  = parentPosX;
        windowHeight = parentPosY;
        
        //Align the scene explorer to the right side of the window.
        bounds.xPos = parentPosX - bounds.width;
        
        relocateTitleBar();
        
        seperator.xPos = bounds.xPos + 28;
        seperator.yPos = bounds.yPos + titleBar.height;
        
        scissorBox.xPos   = bounds.xPos;
        scissorBox.yPos   = parentPosY - (bounds.yPos + bounds.height);
        scissorBox.width  = bounds.xPos + bounds.width;
        scissorBox.height = 224;
        
        addButton.xPos = bounds.xPos + (bounds.width - 64);
        addButton.yPos = bounds.yPos + 8;
        subButton.xPos = bounds.xPos + (bounds.width - 32);
        subButton.yPos = bounds.yPos + 8;
        
        addIcon.position.set(addButton.xPos, addButton.yPos + 24);
        subIcon.position.set(subButton.xPos, subButton.yPos + 24);
        
        observable.notifyObservers("viewportSize", new Vector2f(bounds.xPos, bounds.yPos + titleBar.height));
        observable.notifyObservers("viewportHeight", parentPosY);
    }
    
    public void showTextArea(boolean value, Member member) {
        showTextArea = value;
        
        if(showTextArea) {
            memberBounds = member.bounds;
            textArea.setText(member.gameObject.getName());
            textArea.focus();
        }
    }

}