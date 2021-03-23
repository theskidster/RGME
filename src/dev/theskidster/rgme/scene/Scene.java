package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import java.util.HashMap;
import java.util.Map;
import org.joml.RayAabIntersection;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

/**
 * @author J Hoffman
 * Created: Mar 9, 2021
 */

public final class Scene {
    
    public static final int CELL_SIZE = 1;
    
    int width;
    int height;
    int depth;
    
    private final RayAabIntersection rayTest = new RayAabIntersection();
    
    final Vector3i initialLocation = new Vector3i();
    final Vector3i cursorLocation  = new Vector3i();
    final Map<Vector2i, Boolean> tiles;
    
    private final Origin origin;
    private final Floor floor = new Floor();
    
    public GameObject selectedGameObject;
    
    public final Map<Integer, GameObject> visibleGeometry = new HashMap<>();
    public final Map<Integer, GameObject> boundingVolumes = new HashMap<>();
    public final Map<Integer, GameObject> triggerBoxes    = new HashMap<>();
    public final Map<Integer, GameObject> lightSources    = new HashMap<>();
    public final Map<Integer, GameObject> entities        = new HashMap<>();
    public final Map<Integer, GameObject> instances       = new HashMap<>();
    
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
        visibleGeometry.values().forEach(volume -> ((VisibleGeometry) volume).update());
    }
    
    public void render(Program sceneProgram, Vector3f camPos, Vector3f camUp) {
        floor.draw(sceneProgram, tiles);
        
        visibleGeometry.values().forEach(volume -> {
            if(volume.visible) ((VisibleGeometry) volume).render(sceneProgram);
        });
        
        origin.render(sceneProgram);
    }
    
    public void selectTile(Vector3f camPos, Vector3f camRay) {
        rayTest.set(camPos.x, camPos.y, camPos.z, camRay.x, camRay.y, camRay.z);
        
        tiles.entrySet().forEach((entry) -> {
            Vector2i location = entry.getKey();
            entry.setValue(rayTest.test(location.x, 0, location.y, location.x + CELL_SIZE, 0, location.y + CELL_SIZE));
        });
    }
    
    public void addShape() {
        if(tiles.containsValue(true) && selectedGameObject != null && selectedGameObject instanceof VisibleGeometry) {
            Vector2i tileLocation = tiles.entrySet().stream().filter(entry -> entry.getValue()).findAny().get().getKey();
            cursorLocation.set(tileLocation.x, 0, tileLocation.y);
            initialLocation.set(cursorLocation);
            
            ((VisibleGeometry) selectedGameObject).addShape(cursorLocation.x, 0, cursorLocation.z);
        }
    }
    
    public void stretchShape(float verticalChange, boolean ctrlHeld) {
        if(selectedGameObject != null && selectedGameObject instanceof VisibleGeometry) {
            ((VisibleGeometry) selectedGameObject).stretchShape(verticalChange, ctrlHeld, this);
        }
    }
    
    public void finalizeShape() {
        if(selectedGameObject != null && selectedGameObject instanceof VisibleGeometry) {
            ((VisibleGeometry) selectedGameObject).shapeHeight = 0;
        }
    }
    
}