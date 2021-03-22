package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.GameObject;
import dev.theskidster.rgme.scene.Scene;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.ui.widgets.Group;
import dev.theskidster.rgme.ui.widgets.Scrollbar;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Observable;
import dev.theskidster.rgme.utils.Rectangle;
import java.util.HashMap;
import java.util.Map;
import org.joml.Vector2f;
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
    
    public GameObject selectedGameObject;
    private final Scene scene;
    private final Scrollbar scrollbar;
    private final Rectangle scissorBox  = new Rectangle();
    private final Rectangle seperator   = new Rectangle(0, 0, 2, 224);
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
        
        observable.properties.put("viewportSize", null);
        for(Group group : groups) observable.addObserver(group);
        observable.addObserver(scrollbar);
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
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_DARK_GRAY, uiProgram);
        renderTitleBar(uiProgram, background, font);
        
        glEnable(GL_SCISSOR_TEST);
        glScissor((int) scissorBox.xPos, (int) scissorBox.yPos, (int) scissorBox.width, (int) scissorBox.height);
            for(Group group : groups) group.render(uiProgram, background, font);
        glDisable(GL_SCISSOR_TEST);
        
        scrollbar.render(uiProgram, background, font);
        
        background.drawRectangle(seperator, Color.RGME_BLACK, uiProgram);
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
        
        observable.notifyObservers("viewportSize", new Vector2f(bounds.xPos, bounds.yPos + titleBar.height));
    }

}