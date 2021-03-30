package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.graphics.Circle;
import dev.theskidster.rgme.graphics.Graphics;
import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import java.util.Map;
import org.joml.Intersectionf;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Mar 30, 2021
 */

final class TranslationCursor {
    
    private int arrowID;
    
    private final float START  = 0.1f;
    private final float LENGTH = 0.5f;
    private float sensitivityX = 9.5f;
    private float sensitivityY = 23.5f;
    private float distance;
    
    private boolean selected;
    
    private final Vector3f position;
    private final Vector3f[] directions;
    
    private final Graphics g        = new Graphics();
    private final Vector3f colorVec = new Vector3f();
    private final Vector3f avg      = new Vector3f();
    private final Vector3f min      = new Vector3f();
    private final Vector3f max      = new Vector3f();
    private final Vector3f normDir  = new Vector3f();
    private final Circle circle     = new Circle(0.1f, 1, Color.WHITE);
    
    TranslationCursor() {
        position   = new Vector3f();
        directions = new Vector3f[6];
        
        directions[0] = new Vector3f(-1,  0,  0);
        directions[1] = new Vector3f( 1,  0,  0);
        directions[2] = new Vector3f( 0, -1,  0);
        directions[3] = new Vector3f( 0,  1,  0);
        directions[4] = new Vector3f( 0,  0, -1);
        directions[5] = new Vector3f( 0,  0,  1);
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            g.vertices = stack.mallocFloat(36);
            
            //(vec3 position)
            g.vertices.put(-START) .put(0)      .put(0);
            g.vertices.put(-LENGTH).put(0)      .put(0);
            g.vertices.put( START) .put(0)      .put(0);
            g.vertices.put( LENGTH).put(0)      .put(0);
            g.vertices.put(0)      .put(-START) .put(0);
            g.vertices.put(0)      .put(-LENGTH).put(0);
            g.vertices.put(0)      .put( START) .put(0);
            g.vertices.put(0)      .put( LENGTH).put(0);
            g.vertices.put(0)      .put(0)      .put( START);
            g.vertices.put(0)      .put(0)      .put( LENGTH);
            g.vertices.put(0)      .put(0)      .put(-START);
            g.vertices.put(0)      .put(0)      .put(-LENGTH);
            
            g.vertices.flip();
        }
        
        g.bindBuffers();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (3 * Float.BYTES), 0);
        
        glEnableVertexAttribArray(0);
    }
    
    void update(Vector3f objectPos) {
        position.set(objectPos);
        g.modelMatrix.translation(position);
        circle.position.set(position);
        circle.update();
    }
    
    void update(Map<Integer, Vector3f> vertexPositions) {
        if(vertexPositions.size() == 1) {
            vertexPositions.keySet().forEach(key -> position.set(vertexPositions.get(key)));
        } else {
            vertexPositions.values().forEach(pos -> {
                avg.x += pos.x;
                avg.y += pos.y;
                avg.z += pos.z;
            });
            
            avg.div(vertexPositions.size());
            position.set(avg);
            avg.set(0);
        }
        
        g.modelMatrix.translation(position);
        
        circle.position.set(position);
        circle.update();
    }
    
    void render(Program sceneProgram, Vector3f camPos, Vector3f camUp) {
        glLineWidth(3);
        glBindVertexArray(g.vao);
        
        for(int i = 0; i < 6; i++) {
            switch(i) {
                case 0, 1 -> colorVec.set(Color.RGME_RED.r,   Color.RGME_RED.g,   Color.RGME_RED.b);
                case 2, 3 -> colorVec.set(Color.RGME_BLUE.r,  Color.RGME_BLUE.g,  Color.RGME_BLUE.b);
                case 4, 5 -> colorVec.set(Color.RGME_GREEN.r, Color.RGME_GREEN.g, Color.RGME_GREEN.b);
            }
            
            sceneProgram.setUniform("uType", 1);
            sceneProgram.setUniform("uModel", false, g.modelMatrix);
            sceneProgram.setUniform("uColor", colorVec);
            
            glDrawArrays(GL_LINES, 2 * i, (2 * i) + 2);
        }
        
        glLineWidth(1);
        App.checkGLError();
        
        circle.render(sceneProgram, camPos, camUp);
    }
    
    void selectArrow(Vector3f camPos, Vector3f camRay) {
        boolean result = false;
        
        distance = (float) Math.sqrt(
                    Math.pow((position.x - camPos.x), 2) + 
                    Math.pow((position.y - camPos.y), 2) +
                    Math.pow((position.z - camPos.z), 2)) *
                    0.003f;
        
        for(int i = 0; i < 6; i++) {
            switch(i) {
                case 0 -> {
                    min.set(position.x - LENGTH, position.y - distance, position.z - distance);
                    max.set(position.x - START, position.y + distance, position.z + distance);
                }
                case 1 -> {
                    min.set(position.x + START, position.y - distance, position.z - distance);
                    max.set(position.x + LENGTH, position.y + distance, position.z + distance);
                }
                case 2 -> {
                    min.set(position.x - distance, position.y - LENGTH, position.z - distance);
                    max.set(position.x + distance, position.y - START, position.z + distance);
                }
                case 3 -> {
                    min.set(position.x - distance, position.y + START, position.z - distance);
                    max.set(position.x + distance, position.y + LENGTH, position.z + distance);
                }
                case 4 -> {
                    min.set(position.x - distance, position.y - distance, position.z - LENGTH);
                    max.set(position.x + distance, position.y + distance, position.z - START);
                }
                case 5 -> {
                    min.set(position.x - distance, position.y - distance, position.z + START);
                    max.set(position.x + distance, position.y + distance, position.z + LENGTH);
                }
            }
            
            result = Intersectionf.testRayAab(camPos, camRay, min, max);
            
            if(result) {
                arrowID = i;
                break;
            }
        }
        
        selected = result;
    }
    
    Movement moveArrow(Vector3f camDir, Vector3f rayChange) {
        camDir.normalize(normDir);
        float dot = normDir.dot(directions[arrowID]);
        
        Movement movement = new Movement();
        
        switch(arrowID) {
            case 0 -> {
                if(Math.abs(dot) < 0.65f) {
                    movement.axis  = "X";
                    movement.value = rayChange.x * (distance * sensitivityX);
                } else {
                    movement.axis  = "X";
                    movement.value = ((dot > 0) ? rayChange.y * -1 : rayChange.y) * (distance * sensitivityY);
                }
            }
            
            case 1 -> {
                if(Math.abs(dot) < 0.65f) {
                    movement.axis  = "X";
                    movement.value = rayChange.x * (distance * sensitivityX);
                } else {
                    movement.axis  = "X";
                    movement.value = ((dot > 0) ? rayChange.y : rayChange.y * -1) * (distance * sensitivityY);
                }
            }
            
            case 2, 3 -> {
                movement.axis = "Y";
                movement.value = rayChange.y * (distance * ((Math.abs(dot) < 0.65f) ? sensitivityX : sensitivityY));
            }
            
            case 4 -> {
                if(Math.abs(dot) < 0.65f) {
                    movement.axis  = "Z";
                    movement.value = rayChange.z * (distance * sensitivityX);
                } else {
                    movement.axis  = "Z";
                    movement.value = ((dot > 0) ? rayChange.y * -1 : rayChange.y) * (distance * sensitivityY);
                }
            }
            
            case 5 -> {
                if(Math.abs(dot) < 0.65f) {
                    movement.axis  = "Z";
                    movement.value = rayChange.z * (distance * sensitivityX);
                } else {
                    movement.axis  = "Z";
                    movement.value = ((dot > 0) ? rayChange.y : rayChange.y * -1) * (distance * sensitivityY);
                }
            }
        }
        
        return movement;
    }
    
    boolean getSelected() { return selected; }
    
    void setSensitivityX(float value) {
        sensitivityX = value;
    }
    
    void setSensitivityY(float value) {
        sensitivityY = value;
    }
    
}