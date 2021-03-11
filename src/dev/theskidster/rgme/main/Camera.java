package dev.theskidster.rgme.main;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 10, 2021
 */

public final class Camera {
    
    private float pitch;
    private float yaw = -90f;
    
    double prevX;
    double prevY;
    
    final Vector3f position  = new Vector3f();
    final Vector3f direction = new Vector3f(0, 0, -1);
    final Vector3f up        = new Vector3f(0, 1, 0);
    
    private final Vector3f tempVec1 = new Vector3f();
    private final Vector3f tempVec2 = new Vector3f();
    
    private final Matrix4f view = new Matrix4f();
    private final Matrix4f proj = new Matrix4f();
    
    void update(int viewportWidth, int viewportHeight) {
        proj.setPerspective((float) Math.toRadians(60f), (float) viewportWidth / viewportHeight, 0.1f, Float.POSITIVE_INFINITY);
    }
    
    void render(Program sceneProgram) {
        view.setLookAt(position, position.add(direction, tempVec1), up);
        
        sceneProgram.setUniform("uView", false, view);
        sceneProgram.setUniform("uProjection", false, proj);
    }
    
    private float getChangeIntensity(double currValue, double prevValue, float sensitivity) {
        return (float) (currValue - prevValue) * sensitivity;
    }
    
    public void setPosition(double xPos, double yPos) {
        if(xPos != prevX || yPos != prevY) {
            float speedX = getChangeIntensity(-xPos, -prevX, 0.017f);
            float speedY = getChangeIntensity(-yPos, -prevY, 0.017f);
            
            position.add(direction.cross(up, tempVec1).normalize().mul(speedX));
            
            tempVec1.set(
                    (float) (Math.cos(Math.toRadians(yaw + 90)) * Math.cos(Math.toRadians(pitch))), 
                    0, 
                    (float) (Math.sin(Math.toRadians(yaw + 90)) * Math.cos(Math.toRadians(pitch))));
            
            position.add(0, direction.cross(tempVec1, tempVec2).normalize().mul(speedY).y, 0);
            
            prevX = xPos;
            prevY = yPos;
        }
    }
    
    public void setDirection(double xPos, double yPos) {
        if(xPos != prevX || yPos != prevY) {
            yaw   += getChangeIntensity(xPos, prevX, 0.35f);
            pitch += getChangeIntensity(yPos, prevY, 0.35f);
            
            if(pitch > 89f)  pitch = 89f;
            if(pitch < -89f) pitch = -89f;
            
            direction.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            direction.y = (float) Math.sin(Math.toRadians(pitch)) * -1;
            direction.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            
            prevX = xPos;
            prevY = yPos;
        }
    }
    
    public void dolly(float speed) {
        position.add(direction.mul(speed, tempVec1));
    }
    
}