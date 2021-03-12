package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.graphics.Graphics;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Mar 11, 2021
 */

final class Origin {
    
    private final Graphics g;
    private final Vector3f colorVec = new Vector3f();
    
    Origin(int width, int height, int depth) {
        g = new Graphics();
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            g.vertices = stack.mallocFloat(6 * 3);
            
            float halfWidth = (width / 2);
            float halfDepth = (depth / 2);
            
            //(vec3 position)
            g.vertices.put(-halfWidth).put(0)     .put(0);
            g.vertices.put( halfWidth).put(0)     .put(0);
            g.vertices.put(0)         .put(0)     .put(0);
            g.vertices.put(0)         .put(height).put(0);
            g.vertices.put(0)         .put(0)     .put(-halfDepth);
            g.vertices.put(0)         .put(0)     .put( halfDepth);
            
            g.vertices.flip();
        }
        
        g.bindBuffers();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (3 * Float.BYTES), 0);
        
        glEnableVertexAttribArray(0);
    }
    
    void render(Program sceneProgram) {
        glBindVertexArray(g.vao);
        
        for(int i = 0; i < 3; i++) {
            switch(i) {
                case 0 -> colorVec.set(Color.RGME_RED.r, Color.RGME_RED.g, Color.RGME_RED.b);
                case 1 -> colorVec.set(Color.RGME_BLUE.r, Color.RGME_BLUE.g, Color.RGME_BLUE.b);
                case 2 -> colorVec.set(Color.RGME_GREEN.r, Color.RGME_GREEN.g, Color.RGME_GREEN.b);
            }
            
            sceneProgram.setUniform("uType", 1);
            sceneProgram.setUniform("uModel", false, g.modelMatrix);
            sceneProgram.setUniform("uColor", colorVec);
            
            glDrawArrays(GL_LINES, 2 * i, (2 * i) + 2);
        }
    }
    
    void destroy() {
        g.freeBuffers();
    }
    
}