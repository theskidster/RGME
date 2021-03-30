package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.commands.CommandHistory;
import dev.theskidster.rgme.commands.MoveObject;
import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import static dev.theskidster.rgme.utils.Light.NOON;
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
    
    private boolean cursorActive;
    private boolean snapToGrid;
    private boolean prevObjectPosSet;
    
    private final RayAabIntersection rayTest = new RayAabIntersection();
    
    final Vector3i initialLocation = new Vector3i();
    final Vector3i cursorLocation  = new Vector3i();
    final Map<Vector2i, Boolean> tiles;
    
    private final Origin origin;
    private final Floor floor           = new Floor();
    private final MovementCursor cursor = new MovementCursor();
    
    private Movement cursorMovement      = new Movement();
    private final Vector3f prevObjectPos = new Vector3f();
    
    public GameObject selectedGameObject;
    
    public final Map<Integer, GameObject> visibleGeometry = new HashMap<>();
    public final Map<Integer, GameObject> boundingVolumes = new HashMap<>();
    public final Map<Integer, GameObject> triggerBoxes    = new HashMap<>();
    public final Map<Integer, GameObject> lightSources    = new HashMap<>();
    public final Map<Integer, GameObject> entities        = new HashMap<>();
    public final Map<Integer, GameObject> instances       = new HashMap<>();
    private final Map<Integer, Vector3f> vertexPositions  = new HashMap<>();
    
    public Scene(int width, int height, int depth, Color clearColor) {
        this.width  = width;
        this.height = height;
        this.depth  = depth;
        
        App.setClearColor(clearColor);
        
        origin = new Origin(width, height, depth);
        
        vertexPositions.put(0, new Vector3f());
        
        tiles = new HashMap<>() {{
            for(int w = -(width / 2); w < width / 2; w++) {
                for(int d = -(depth / 2); d < depth / 2; d++) {
                    put(new Vector2i(w, d), false);
                }
            }
        }};
        
        LightSource worldLight = new LightSource(NOON);
        lightSources.put(worldLight.index, worldLight);
    }
    
    public void update(String currTool) {
        if(currTool != null) {
            switch(currTool) {
                case "Translate" -> {
                    Vector3f newPos = selectedGameObject.position;

                    switch(cursorMovement.axis) {                    
                        case "x", "X" -> selectedGameObject.position.set(newPos.x + cursorMovement.value, newPos.y, newPos.z);
                        case "y", "Y" -> selectedGameObject.position.set(newPos.x, newPos.y + cursorMovement.value, newPos.z);
                        case "z", "Z" -> selectedGameObject.position.set(newPos.x, newPos.y, newPos.z + cursorMovement.value);
                    }
                    
                    cursorActive = true;
                    cursor.update(selectedGameObject.position);
                    
                    cursorMovement.axis  = "";
                    cursorMovement.value = 0;
                }
            }
        } else {
            cursorActive = false;
        }
        
        visibleGeometry.values().forEach(geometry -> ((VisibleGeometry) geometry).update());
        lightSources.values().forEach(light -> ((LightSource) light).update());
    }
    
    public void render(Program sceneProgram, Vector3f camPos, Vector3f camUp) {
        floor.draw(sceneProgram, tiles);
        
        visibleGeometry.values().forEach(geometry -> {
            if(geometry.visible) {
                ((VisibleGeometry) geometry).render(
                        sceneProgram, 
                        lightSources.values().toArray(new GameObject[lightSources.size()]), 
                        lightSources.size());
            }
        });
        
        lightSources.values().forEach(light -> {
            if(light.visible) ((LightSource) light).render(sceneProgram, camPos, camUp);
        });
        
        if(cursorActive) cursor.render(sceneProgram, camPos, camUp);
        
        origin.render(sceneProgram);
    }
    
    public void selectTile(Vector3f camPos, Vector3f camRay) {
        rayTest.set(camPos.x, camPos.y, camPos.z, camRay.x, camRay.y, camRay.z);
        
        tiles.entrySet().forEach((entry) -> {
            Vector2i location = entry.getKey();
            entry.setValue(rayTest.test(location.x, 0, location.y, location.x + CELL_SIZE, 0, location.y + CELL_SIZE));
        });
    }
    
    public void unselectTiles() {
        tiles.entrySet().forEach((entry) -> entry.setValue(false));
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
    
    public void selectCursorArrow(Vector3f camPos, Vector3f camRay) {
        cursor.selectArrow(camPos, camRay);
    }
    
    public void moveCursor(Vector3f camDir, Vector3f rayChange, boolean ctrlHeld) {
        if(!prevObjectPosSet) {
            prevObjectPos.set(selectedGameObject.position);
            prevObjectPosSet = true;
        }
        
        cursorMovement = cursor.moveArrow(camDir, rayChange);
    }
    
    public void finalizeMovement(CommandHistory cmdHistory) {
        if(prevObjectPosSet) {
            cmdHistory.executeCommand(new MoveObject(selectedGameObject, prevObjectPos, selectedGameObject.position));
        }
        
        prevObjectPosSet = false;
    }
    
}