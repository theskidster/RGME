package dev.theskidster.rgme.graphics;

import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Rectangle;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Feb 25, 2021
 */

public final class Background {
    
    private final int FLOATS_PER_RECTANGLE = 12;
    
    private final int vao = glGenVertexArrays();
    private final int vbo = glGenBuffers();
    
    public Background() {
        glBindVertexArray(vao);
        
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, FLOATS_PER_RECTANGLE * Float.BYTES, GL_DYNAMIC_DRAW);
        
        glVertexAttribPointer(0, 2, GL_FLOAT, false, (2 * Float.BYTES), 0);
        
        glEnableVertexAttribArray(0);
    }
    
    public void drawRectangle(Rectangle r, Color color, Program uiProgram) {
        glBindVertexArray(vao);
        
        uiProgram.setUniform("uType", 1);
        uiProgram.setUniform("uColor", color.asVec3());
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer vertexBuf = stack.mallocFloat(FLOATS_PER_RECTANGLE);
            
            //(vec2 position)
            vertexBuf.put(r.xPos)          .put(r.yPos);
            vertexBuf.put(r.xPos + r.width).put(r.yPos);
            vertexBuf.put(r.xPos + r.width).put(r.yPos + r.height);
            vertexBuf.put(r.xPos + r.width).put(r.yPos + r.height);
            vertexBuf.put(r.xPos)          .put(r.yPos + r.height);
            vertexBuf.put(r.xPos)          .put(r.yPos);
            
            vertexBuf.flip();
            
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertexBuf);
        }
        
        glDrawArrays(GL_TRIANGLES, 0, 6);
        App.checkGLError();
    }
    
}