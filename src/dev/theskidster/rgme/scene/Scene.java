package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import java.util.HashMap;
import java.util.Map;
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
    
    public final Map<String, GameObject> gameObjects = new HashMap<>();
    
    public Scene(int width, int height, int depth, Color clearColor) {
        this.width  = width;
        this.height = height;
        this.depth  = depth;
        
        App.setClearColor(clearColor);
        
        origin = new Origin(width, height, depth);
        
        //TODO: include world light
        
        gameObjects.put("test", App.testObject);
    }
    
    public void update() {
        gameObjects.forEach((name, object) -> object.update());
    }
    
    public void render(Program sceneProgram, Vector3f camPos, Vector3f camUp) {
        gameObjects.forEach((name, object) -> object.render(sceneProgram, camPos, camUp));
        
        origin.render(sceneProgram);
    }
    
}