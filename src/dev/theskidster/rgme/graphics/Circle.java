package dev.theskidster.rgme.graphics;

import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Mar 30, 2021
 */

public class Circle {

    private final int NUM_SIDES = 32;
    private final int lineWidth;
    
    public final Vector3f position = new Vector3f();
    public final Vector3f rotation = new Vector3f();
    private final Graphics g       = new Graphics();
    
    public Circle(float radius, int lineWidth, Color color) {
        this.lineWidth = lineWidth;
        
        float doublePI = (float) (Math.PI * 2f);
        
        float[] vertX = new float[NUM_SIDES];
        float[] vertY = new float[NUM_SIDES];
        
        for(int v = 0; v < NUM_SIDES; v++) {
            vertX[v] = (float) (radius * Math.cos(v * doublePI / NUM_SIDES));
            vertY[v] = (float) (radius * Math.sin(v * doublePI / NUM_SIDES));
        }
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            g.vertices = stack.mallocFloat(NUM_SIDES * 6);
            
            for(int v = 0; v < NUM_SIDES; v++) {
                g.vertices.put(vertX[v]).put(vertY[v]).put(0);
                g.vertices.put(color.r).put(color.g).put(color.b);
            }
            
            g.vertices.flip();
        }
        
        g.bindBuffers();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (6 * Float.BYTES), 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, (6 * Float.BYTES), (3 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }
    
    public void update() {
        g.modelMatrix.translation(position);
        g.modelMatrix.rotateX((float) Math.toRadians(rotation.x));
        g.modelMatrix.rotateY((float) Math.toRadians(rotation.y));
        g.modelMatrix.rotateZ((float) Math.toRadians(rotation.z));
    }
    
    public void render(Program sceneProgram) {
        glLineWidth(lineWidth);
        glBindVertexArray(g.vao);
        
        sceneProgram.setUniform("uType", 0);
        sceneProgram.setUniform("uModel", false, g.modelMatrix);
        
        glDrawArrays(GL_LINE_LOOP, 0, NUM_SIDES);
        glLineWidth(1);
        
        App.checkGLError();
    }
    
    public void render(Program sceneProgram, Vector3f camPos, Vector3f camUp) {
        g.modelMatrix.billboardSpherical(position, camPos, camUp);
        render(sceneProgram);
    }
    
}