package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import java.util.HashMap;
import java.util.Map;
import org.joml.Vector2i;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 9, 2021
 */

public final class Scene {
    
    public static final int CELL_SIZE = 1;
    
    int width;
    int height;
    int depth;
    
    private final Origin origin;
    private final Floor floor = new Floor();
    
    public final Map<Integer, GameObject> visibleGeometry = new HashMap<>();
    public final Map<Integer, GameObject> boundingVolumes = new HashMap<>();
    public final Map<Integer, GameObject> triggerBoxes    = new HashMap<>();
    public final Map<Integer, GameObject> lightSources    = new HashMap<>();
    public final Map<Integer, GameObject> entities        = new HashMap<>();
    public final Map<Integer, GameObject> instances       = new HashMap<>();
    
    private final Map<Vector2i, Boolean> tiles;
    
    public Scene(int width, int height, int depth, Color clearColor) {
        this.width  = width;
        this.height = height;
        this.depth  = depth;
        
        App.setClearColor(clearColor);
        
        origin = new Origin(width, height, depth);
        
        tiles = new HashMap<>() {{
            for(int w = -(width / 2); w < width / 2; w++) {
                for(int d = -(depth / 2); d < depth / 2; d++) {
                    put(new Vector2i(w, d), false);
                }
            }
        }};
    }
    
    public void update() {
        
    }
    
    public void render(Program sceneProgram, Vector3f camPos, Vector3f camUp) {
        floor.draw(sceneProgram, tiles);
        origin.render(sceneProgram);
    }
    
}