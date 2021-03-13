package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.scene.commands.AddVisibleGeometry;
import dev.theskidster.rgme.ui.Command;
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
    
    private Origin origin;
    
    private final static Map<String, Command> commands = new HashMap<>();
    
    
    private final Map<String, VisibleGeometry> visibleGeometry = new HashMap<>();
    private final Map<String, GameObject> gameObjects;
    
    public Scene(int width, int height, int depth, Color clearColor) {
        this.width  = width;
        this.height = height;
        this.depth  = depth;
        
        App.setClearColor(clearColor);
        
        origin = new Origin(width, height, depth);
        
        //TODO: include world light
        
        gameObjects = new HashMap<>() {{
            put("test", new TestObject(new Vector3f(0, 0, -10)));
        }};
        
        commands.put("add visible geometry", new AddVisibleGeometry(this));
    }
    
    public void update() {
        gameObjects.forEach((name, object) -> object.update());
        visibleGeometry.forEach((name, object) -> object.update());
    }
    
    public void render(Program sceneProgram, Vector3f camPos, Vector3f camUp) {
        visibleGeometry.forEach((name, object) -> object.render(sceneProgram, camPos, camUp));
        gameObjects.forEach((name, object) -> object.render(sceneProgram, camPos, camUp));
        
        origin.render(sceneProgram);
    }
    
    public static Command getCommand(String name) {
        return commands.get(name);
    }
    
}