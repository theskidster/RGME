package dev.theskidster.rgme.scene;

import static dev.theskidster.rgme.scene.Scene.CELL_SIZE;
import java.util.LinkedHashMap;
import org.joml.Intersectionf;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author J Hoffman
 * Created: Mar 23, 2021
 */

abstract class Polyhedron extends GameObject {

    protected final int vao = glGenVertexArrays();
    protected final int vbo = glGenBuffers();
    
    protected final int floatsPerVertex;
    protected int numVertices;
    protected int bufferSizeInBytes;
    private int vpOffset;
    protected int fOffset;
    
    private final int[] offsets = new int[8];
    
    private float shapeHeight;
    
    protected boolean updateData;
    
    private final Vector3i locationDiff   = new Vector3i();
    private final VertexSelector selector = new VertexSelector();
    
    private final LinkedHashMap<Integer, Vector3f> initialVPs  = new LinkedHashMap<>();
    protected LinkedHashMap<Integer, Vector3f> vertexPositions = new LinkedHashMap<>();
    protected LinkedHashMap<Integer, Face> faces               = new LinkedHashMap<>();
    
    Polyhedron(String name, int floatsPerVertex) {
        super(name);
        this.floatsPerVertex = floatsPerVertex;
    }
    
    protected void findBufferSize() {
        numVertices       = faces.size() * 3;
        bufferSizeInBytes = numVertices * floatsPerVertex * Float.BYTES;
        
        glBindVertexArray(vao);
        
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, bufferSizeInBytes, GL_DYNAMIC_DRAW);
    }
    
    protected void genVertexPositions(float x, float y, float z) {
        vpOffset = vertexPositions.size();
        
        vertexPositions.put(vpOffset,     new Vector3f(x,             y,             z + CELL_SIZE));
        vertexPositions.put(vpOffset + 1, new Vector3f(x + CELL_SIZE, y,             z + CELL_SIZE));
        vertexPositions.put(vpOffset + 2, new Vector3f(x + CELL_SIZE, y + CELL_SIZE, z + CELL_SIZE));
        vertexPositions.put(vpOffset + 3, new Vector3f(x,             y + CELL_SIZE, z + CELL_SIZE));
        vertexPositions.put(vpOffset + 4, new Vector3f(x,             y,             z));
        vertexPositions.put(vpOffset + 5, new Vector3f(x + CELL_SIZE, y,             z));
        vertexPositions.put(vpOffset + 6, new Vector3f(x + CELL_SIZE, y + CELL_SIZE, z));
        vertexPositions.put(vpOffset + 7, new Vector3f(x,             y + CELL_SIZE, z));
        
        initialVPs.clear();
        
        for(int i = vpOffset; i < vpOffset + 8; i++) {
            initialVPs.put(i, new Vector3f(vertexPositions.get(i)));
        }
    }
    
    protected void strechShape(float verticalChange, boolean ctrlHeld, Scene scene) {
        if(ctrlHeld) {
            shapeHeight += verticalChange;
            shapeHeight = (shapeHeight > scene.height) ? scene.height : shapeHeight;
            shapeHeight = (shapeHeight < 1) ? 1 : shapeHeight;
            
            if((int) shapeHeight > 0) {
                setVertexPos(vpOffset + 2, "y", (int) shapeHeight);
                setVertexPos(vpOffset + 3, "y", (int) shapeHeight);
                setVertexPos(vpOffset + 6, "y", (int) shapeHeight);
                setVertexPos(vpOffset + 7, "y", (int) shapeHeight);
            }
        } else {
            if(scene.tiles.containsValue(true)) {
                Vector2i tileLocation = scene.tiles.entrySet().stream().filter(entry -> entry.getValue()).findAny().get().getKey();
                scene.cursorLocation.set(tileLocation.x, 0, tileLocation.y);

                if(!scene.cursorLocation.equals(scene.initialLocation)) {
                    scene.cursorLocation.sub(scene.initialLocation, locationDiff);
                    
                    for(int i = 0; i < offsets.length; i++) offsets[i] = vpOffset + i;
                    
                    /*
                    There's probably a more elegant mathematical solution to 
                    this- but I'm too dumb to implement it.
                    */
                    if(locationDiff.x > 0) {
                        if(locationDiff.z > 0) {
                            setVertexPos(offsets[0], scene.initialLocation.x,            getVertexPos(offsets[0]).y, scene.cursorLocation.z + CELL_SIZE);
                            setVertexPos(offsets[1], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[1]).y, scene.cursorLocation.z + CELL_SIZE);
                            setVertexPos(offsets[2], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[2]).y, scene.cursorLocation.z + CELL_SIZE);
                            setVertexPos(offsets[3], scene.initialLocation.x,            getVertexPos(offsets[3]).y, scene.cursorLocation.z + CELL_SIZE);
                            resetVertexPos(offsets[4], "z");
                            setVertexPos(offsets[5], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[5]).y, scene.initialLocation.z);
                            setVertexPos(offsets[6], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[6]).y, scene.initialLocation.z);
                            resetVertexPos(offsets[7], "z");
                        } else if(locationDiff.z < 0) {
                            resetVertexPos(offsets[0], "z");
                            setVertexPos(offsets[1], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[1]).y, scene.initialLocation.z + CELL_SIZE);
                            setVertexPos(offsets[2], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[2]).y, scene.initialLocation.z + CELL_SIZE);
                            resetVertexPos(offsets[3], "z");
                            setVertexPos(offsets[4], scene.initialLocation.x,            getVertexPos(offsets[4]).y, scene.cursorLocation.z);
                            setVertexPos(offsets[5], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[5]).y, scene.cursorLocation.z);
                            setVertexPos(offsets[6], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[6]).y, scene.cursorLocation.z);
                            setVertexPos(offsets[7], scene.initialLocation.x,            getVertexPos(offsets[7]).y, scene.cursorLocation.z);
                        } else {
                            resetVertexPos(offsets[0], "z");
                            setVertexPos(offsets[1], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[1]).y, scene.cursorLocation.z + CELL_SIZE);
                            setVertexPos(offsets[2], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[2]).y, scene.cursorLocation.z + CELL_SIZE);
                            resetVertexPos(offsets[3], "z");
                            resetVertexPos(offsets[4], "z");
                            setVertexPos(offsets[5], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[5]).y, scene.initialLocation.z);
                            setVertexPos(offsets[6], scene.cursorLocation.x + CELL_SIZE, getVertexPos(offsets[6]).y, scene.initialLocation.z);
                            resetVertexPos(offsets[7], "z");
                        }
                    } else if(locationDiff.x < 0) {
                        if(locationDiff.z > 0) {
                            setVertexPos(offsets[0], scene.cursorLocation.x,              getVertexPos(offsets[0]).y, scene.cursorLocation.z + CELL_SIZE);
                            setVertexPos(offsets[1], scene.initialLocation.x + CELL_SIZE, getVertexPos(offsets[1]).y, scene.cursorLocation.z + CELL_SIZE);
                            setVertexPos(offsets[2], scene.initialLocation.x + CELL_SIZE, getVertexPos(offsets[2]).y, scene.cursorLocation.z + CELL_SIZE);
                            setVertexPos(offsets[3], scene.cursorLocation.x,              getVertexPos(offsets[3]).y, scene.cursorLocation.z + CELL_SIZE);
                            setVertexPos(offsets[4], scene.cursorLocation.x,              getVertexPos(offsets[4]).y, scene.initialLocation.z);
                            resetVertexPos(offsets[5], "z");
                            resetVertexPos(offsets[6], "z");
                            setVertexPos(offsets[7], scene.cursorLocation.x, getVertexPos(offsets[7]).y, scene.initialLocation.z);
                        } else if(locationDiff.z < 0) {
                            setVertexPos(offsets[0], scene.cursorLocation.x, getVertexPos(offsets[0]).y, scene.initialLocation.z + CELL_SIZE);
                            resetVertexPos(offsets[1], "z");
                            resetVertexPos(offsets[2], "z");
                            setVertexPos(offsets[3], scene.cursorLocation.x,              getVertexPos(offsets[3]).y, scene.initialLocation.z + CELL_SIZE);
                            setVertexPos(offsets[4], scene.cursorLocation.x,              getVertexPos(offsets[4]).y, scene.cursorLocation.z);
                            setVertexPos(offsets[5], scene.initialLocation.x + CELL_SIZE, getVertexPos(offsets[5]).y, scene.cursorLocation.z);
                            setVertexPos(offsets[6], scene.initialLocation.x + CELL_SIZE, getVertexPos(offsets[6]).y, scene.cursorLocation.z);
                            setVertexPos(offsets[7], scene.cursorLocation.x,              getVertexPos(offsets[7]).y, scene.cursorLocation.z);
                        } else {
                            setVertexPos(offsets[0], scene.cursorLocation.x, getVertexPos(offsets[0]).y, scene.initialLocation.z + CELL_SIZE);
                            resetVertexPos(offsets[1], "z");
                            resetVertexPos(offsets[2], "z");
                            setVertexPos(offsets[3], scene.cursorLocation.x, getVertexPos(offsets[3]).y, scene.initialLocation.z + CELL_SIZE);
                            setVertexPos(offsets[4], scene.cursorLocation.x, getVertexPos(offsets[4]).y, scene.initialLocation.z);
                            resetVertexPos(offsets[5], "z");
                            resetVertexPos(offsets[6], "z");
                            setVertexPos(offsets[7], scene.cursorLocation.x, getVertexPos(offsets[7]).y, scene.initialLocation.z);
                        }
                    } else {
                        if(locationDiff.z > 0) {
                            setVertexPos(offsets[0], scene.initialLocation.x,             getVertexPos(offsets[0]).y, scene.cursorLocation.z + CELL_SIZE);
                            setVertexPos(offsets[1], scene.initialLocation.x + CELL_SIZE, getVertexPos(offsets[1]).y, scene.cursorLocation.z + CELL_SIZE);
                            setVertexPos(offsets[2], scene.initialLocation.x + CELL_SIZE, getVertexPos(offsets[2]).y, scene.cursorLocation.z + CELL_SIZE);
                            setVertexPos(offsets[3], scene.initialLocation.x,             getVertexPos(offsets[3]).y, scene.cursorLocation.z + CELL_SIZE);
                            resetVertexPos(offsets[4], "x");
                            resetVertexPos(offsets[5], "x");
                            resetVertexPos(offsets[6], "x");
                            resetVertexPos(offsets[7], "x");
                        } else if(locationDiff.z < 0) {
                            resetVertexPos(offsets[0], "x");
                            resetVertexPos(offsets[1], "x");
                            resetVertexPos(offsets[2], "x");
                            resetVertexPos(offsets[3], "x");
                            setVertexPos(offsets[4], scene.initialLocation.x,             getVertexPos(offsets[4]).y, scene.cursorLocation.z);
                            setVertexPos(offsets[5], scene.initialLocation.x + CELL_SIZE, getVertexPos(offsets[5]).y, scene.cursorLocation.z);
                            setVertexPos(offsets[6], scene.initialLocation.x + CELL_SIZE, getVertexPos(offsets[6]).y, scene.cursorLocation.z);
                            setVertexPos(offsets[7], scene.initialLocation.x,             getVertexPos(offsets[7]).y, scene.cursorLocation.z);
                        }
                    }
                } else {
                    resetVertexPos();
                }
            }
        }
    }
    
    private void resetVertexPos(int index, String axis) {
        float value;
        
        switch(axis) {
            case "x", "X" -> value = initialVPs.get(index).x;
            case "y", "Y" -> value = initialVPs.get(index).y;
            case "z", "Z" -> value = initialVPs.get(index).z;
            default -> { return; }
        }
        
        setVertexPos(index, axis, value);
    }
    
    private void resetVertexPos() {
        boolean alreadyReset = true;
        
        for(int i = vpOffset; i < vpOffset + 7; i++) {
            alreadyReset = initialVPs.get(i).equals(vertexPositions.get(i));
            if(!alreadyReset) break;
        }
        
        if(!alreadyReset) {
            for(int i = vpOffset; i < vpOffset + 8; i++) {
                float yPos = (initialVPs.get(i).y == CELL_SIZE) ? (int) shapeHeight : initialVPs.get(i).y;
                vertexPositions.put(i, new Vector3f(initialVPs.get(i).x, yPos, initialVPs.get(i).z));
            }
            
            updateData = true;
        }
    }
    
    abstract void addShape(float x, float y, float z);
    
    void selectVertices(Vector3f camPos, Vector3f camRay) {
        vertexPositions.forEach((id, pos) -> {
            float distance = (float) Math.sqrt(
                    Math.pow((pos.x - camPos.x), 2) + 
                    Math.pow((pos.y - camPos.y), 2) +
                    Math.pow((pos.z - camPos.z), 2)) *
                    0.0003f;

            if(Intersectionf.testRaySphere(camPos, camRay, pos, distance)) {
                selector.addVertex(id);
            }
        });
    }
    
    void clearSelectedVertices() {
        selector.clear();
    }
    
    Vector3f getVertexPos(int index) { return vertexPositions.get(index); }
    
    void setVertexPos(int index, String axis, float value) {
        switch(axis) {
            case "x", "X" -> vertexPositions.get(index).x = value;
            case "y", "Y" -> vertexPositions.get(index).y = value;
            case "z", "Z" -> vertexPositions.get(index).z = value;
        }
        
        updateData = true;
    }
    
    void setVertexPos(int index, float x, float y, float z) {
        vertexPositions.get(index).set(x, y, z);
        updateData = true;
    }
    
    void snapVertexPos(int index) {
        vertexPositions.get(index).round();
        updateData = true;
    }
    
    void selectAll() {
        vertexPositions.forEach((index, position) -> selector.addVertex(index));
    }
    
}