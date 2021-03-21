package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 9, 2021
 */

public final class Scene {
    
    int width;
    int height;
    int depth;
    
    private final Origin origin;
    
    public final Map<Integer, GameObject> visibleGeometry = new HashMap<>();
    public final Map<Integer, GameObject> boundingVolumes = new HashMap<>();
    public final Map<Integer, GameObject> triggerBoxes    = new HashMap<>();
    public final Map<Integer, GameObject> lightSources    = new HashMap<>();
    public final Map<Integer, GameObject> entities        = new HashMap<>();
    public final Map<Integer, GameObject> instances       = new HashMap<>();
    
    private final Set<GameObject> allObjects = new LinkedHashSet<>();
    
    public Scene(int width, int height, int depth, Color clearColor) {
        this.width  = width;
        this.height = height;
        this.depth  = depth;
        
        App.setClearColor(clearColor);
        
        origin = new Origin(width, height, depth);
        
        //TODO: include world light
    }
    
    public void update() {
        //TODO: might be better off using an observer instead
        allObjects.clear();
        
        allObjects.addAll(visibleGeometry.values());
        allObjects.addAll(boundingVolumes.values());
        allObjects.addAll(triggerBoxes.values());
        allObjects.addAll(lightSources.values());
        allObjects.addAll(entities.values());
        allObjects.addAll(instances.values());
        
        allObjects.forEach(object -> object.update());
    }
    
    public void render(Program sceneProgram, Vector3f camPos, Vector3f camUp) {
        allObjects.forEach(object -> object.render(sceneProgram, camPos, camUp));
        
        origin.render(sceneProgram);
    }
    
}