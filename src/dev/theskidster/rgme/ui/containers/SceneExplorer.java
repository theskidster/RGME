package dev.theskidster.rgme.ui.containers;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.Scene;
import dev.theskidster.rgme.ui.FreeTypeFont;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.ui.widgets.Group;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Observable;
import dev.theskidster.rgme.utils.Rectangle;
import org.joml.Vector2f;

/**
 * @author J Hoffman
 * Created: Mar 14, 2021
 */

public final class SceneExplorer extends Container {
    
    private Scene scene;
    
    private final Rectangle seperator   = new Rectangle(0, 0, 2, 224);
    private final Observable observable = new Observable(this);
    
    private Group[] groups = new Group[6];
    
    public SceneExplorer(Scene scene) {
        super(0, 28, TOOLBAR_WIDTH, 264, "Scene Explorer", 5, 0);
        this.scene = scene;
        
        groups[0] = new Group("Visible Geometry", scene.visibleGeometry);
        groups[1] = new Group("Bounding Volumes", scene.boundingVolumes);
        groups[2] = new Group("Trigger Boxes",    scene.triggerBoxes);
        groups[3] = new Group("Light Sources",    scene.lightSources);
        groups[4] = new Group("Entities",         scene.entities);
        groups[5] = new Group("Instances",        scene.instances);
        
        observable.properties.put("viewportSize", null);
        observable.properties.put("verticalOffset", 0);
        
        for(Group group : groups) observable.addObserver(group);
    }

    @Override
    public Command update(Mouse mouse) {
        for(Group group : groups) {
            Command command = group.update(mouse);
            if(command != null) return command;
        }
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_DARK_GRAY, uiProgram);
        renderTitleBar(uiProgram, background, font);
        
        for(Group group : groups) group.render(uiProgram, background, font);
        
        background.drawRectangle(seperator, Color.RGME_BLACK, uiProgram);
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        //Align the scene explorer to the right side of the window.
        bounds.xPos = parentPosX - bounds.width;
        
        relocateTitleBar();
        
        seperator.xPos = bounds.xPos + 28;
        seperator.yPos = bounds.yPos + titleBar.height;
        
        observable.notifyObservers("viewportSize", new Vector2f(bounds.xPos, bounds.yPos + titleBar.height));
    }

}