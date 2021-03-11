package dev.theskidster.rgme.main;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 10, 2021
 */

public final class Camera {
    
    final Vector3f position  = new Vector3f();
    final Vector3f direction = new Vector3f(0, 0, -1);
    final Vector3f up        = new Vector3f(0, 1, 0);
    
    private final Vector3f tempVec = new Vector3f();
    
    private final Matrix4f view = new Matrix4f();
    private final Matrix4f proj = new Matrix4f();
    
    void update(int viewportWidth, int viewportHeight) {
        proj.setPerspective((float) Math.toRadians(60f), (float) viewportWidth / viewportHeight, 0.1f, Float.POSITIVE_INFINITY);
    }
    
    void render(Program sceneProgram) {
        view.setLookAt(position, position.add(direction, tempVec), up);
        
        sceneProgram.setUniform("uView", false, view);
        sceneProgram.setUniform("uProjection", false, proj);
    }
    
}