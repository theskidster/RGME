package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.main.Program;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Mar 9, 2021
 */

public class PointLight extends GameObject {

    public PointLight() {
        super("Point Light");
    }
    
    @Override
    void update() {
    }

    @Override
    void render(Program sceneProgram, Vector3f camPos, Vector3f camUp) {
    }

}