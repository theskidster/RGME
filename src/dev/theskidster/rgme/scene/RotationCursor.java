package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.graphics.Circle;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import org.joml.Intersectionf;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 30, 2021
 */

final class RotationCursor {

    private int circleID;
    
    private final float RADIUS = 0.5f;
    private float epsilon;
    
    private final Vector3f normDir   = new Vector3f();
    private final Vector3f intersect = new Vector3f();
    private final Vector3f position  = new Vector3f();
    
    private final Vector3f[] planes = new Vector3f[6];
    private final Circle[] circles  = new Circle[3];
    
    RotationCursor() {
        circles[0] = new Circle(RADIUS, 3, Color.RGME_RED);
        circles[1] = new Circle(RADIUS, 3, Color.RGME_GREEN);
        circles[2] = new Circle(RADIUS, 3, Color.RGME_BLUE);
        
        circles[0].rotation.y = 90f;
        circles[2].rotation.x = 90f;
        
        planes[0] = new Vector3f(1, 0, 0);
        planes[1] = new Vector3f(0, 0, 1);
        planes[2] = new Vector3f(0, 1, 0);
        planes[3] = new Vector3f(-1, 0,  0);
        planes[4] = new Vector3f(0,  0, -1);
        planes[5] = new Vector3f(0, -1,  0);
    }
    
    public void update(Vector3f objectPos) {
        position.set(objectPos);
        
        for(Circle circle : circles) {
            circle.position.set(objectPos);
            circle.update();
        }
    }
    
    public void render(Program sceneProgram) {
        for(Circle circle : circles) circle.render(sceneProgram);
    }
    
    void selectCircle(Vector3f camPos, Vector3f camRay) {
        epsilon = (float) Math.sqrt(
                    Math.pow((position.x - camPos.x), 2) + 
                    Math.pow((position.y - camPos.y), 2) +
                    Math.pow((position.z - camPos.z), 2)) *
                    0.004f;
        
        for(int c = 0; c < circles.length; c++) {
            /*
            To determine which circle has been selected I first check if the ray 
            provided by the camera intersects the plane associated with the circle.
            */
            float distanceToPlane = Intersectionf.intersectRayPlane(camPos, camRay, position, planes[c], 1f);
            
            /*
            If an intersection between the ray and plane has been detected, we'll
            perform another test using the distance between the point of intersection
            and position of the circle to see if the ray falls close to its radius.
            */
            if(distanceToPlane > 0) {
                Vector3f intersectionPoint = new Vector3f();
                camPos.add(camRay.mul(distanceToPlane), intersectionPoint);

                float distanceToOrigin = intersectionPoint.distance(position);
                
                if(distanceToOrigin <= (RADIUS + epsilon) && distanceToOrigin >= (RADIUS - epsilon)) {
                    circleID  = c;
                    intersect.set(intersectionPoint);
                    break;
                }
            }
        }
    }
    
    Movement moveCircle(Vector3f camPos, Vector3f camRay, Vector3f camDir, Vector3f rayChange) {
        camDir.normalize(normDir);
        float dot = normDir.dot(planes[circleID]);
        
        Movement movement = new Movement();
        
        switch(circleID) {
            case 0 -> movement.axis = "X";
            case 1 -> movement.axis = "Z";
            case 2 -> movement.axis = "Y";    
        }
        
        Vector3f normal = (dot > 0) ? planes[circleID + 3] : planes[circleID];
        Vector3f point  = findIntersectionPoint(camPos, camRay, normal);

        movement.value = (float) -Math.toDegrees(point.angleSigned(intersect, planes[circleID]));
        
        return movement;
    }
    
    Vector3f findIntersectionPoint(Vector3f camPos, Vector3f camRay, Vector3f normal) {
        Vector3f intersectionPoint = new Vector3f();
        
        float distanceToPlane = Intersectionf.intersectRayPlane(camPos, camRay, position, normal, 0);
        camPos.add(camRay.mul(distanceToPlane), intersectionPoint);
        
        intersectionPoint.sub(position);
        
        return intersectionPoint;
    }
    
}