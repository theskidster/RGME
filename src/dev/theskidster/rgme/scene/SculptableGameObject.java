package dev.theskidster.rgme.scene;

import java.util.LinkedHashMap;
import org.joml.Intersectionf;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Apr 4, 2021
 */

public abstract class SculptableGameObject extends GameObject {

    protected boolean updateData;
    
    protected final LinkedHashMap<Integer, Vector3f> vertexPositions = new LinkedHashMap<>();
    private final LinkedHashMap<Integer, Vector3f> positionSubMap    = new LinkedHashMap<>();
    
    public SculptableGameObject(String name) {
        super(name);
    }
    
    public void setVertexPositions(LinkedHashMap<Integer, Vector3f> newPositions) {
        for(int i = 0; i < vertexPositions.size(); i++) {
            if(newPositions.containsKey(i)) {
                vertexPositions.put(i, new Vector3f(newPositions.get(i)));
            }
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
    
    void selectVertices(Vector3f camPos, Vector3f camRay, VertexSelector selector) {
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
    
    LinkedHashMap<Integer, Vector3f> getSelectedVertices(VertexSelector selector) {
        positionSubMap.clear();
        
        for(int i = 0; i < vertexPositions.size(); i++) {
            if(selector.contains(i)) {
                positionSubMap.put(i, vertexPositions.get(i));
            }
        }
        
        return positionSubMap;
    }

    public LinkedHashMap<Integer, Vector3f> getVertexPositions() {
        return vertexPositions;
    }
    
}