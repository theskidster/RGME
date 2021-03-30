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
    
    private final Vector3f position = new Vector3f();
    private final Circle[] circles  = new Circle[1];
    
    RotationCursor() {
        circles[0] = new Circle(RADIUS, 3, Color.RGME_RED);
        //circles[1] = new Circle(RADIUS, 3, Color.RGME_GREEN);
        //circles[2] = new Circle(RADIUS, 3, Color.RGME_BLUE);
        
        circles[0].rotation.y = 90f;
        //circles[2].rotation.x = 90f;
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
        boolean result;
        
        
        
        //result = Intersectionf.testRaySphere(camPos, camRay, position, RADIUS);
        
        //System.out.println(result);
    }
    
}