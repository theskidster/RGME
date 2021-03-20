package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.graphics.Graphics;
import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Mar 9, 2021
 */

public class TestObject extends GameObject {

    private Graphics g = new Graphics();
    
    public TestObject() {
        super("Shape");
        
        float x = (float) ((Math.random() > 0.5) ? Math.random() * 10 : Math.random() * -10);
        float y = (float) Math.random() * 5;
        float z = (float) ((Math.random() > 0.5) ? Math.random() * 10 : Math.random() * -10);
        
        this.position = new Vector3f(x, y, z);
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            g.vertices = stack.mallocFloat(18);
            
            //(vec3 position), (vec3 color)
            g.vertices.put(-1).put(-1).put(0)   .put(1).put(0).put(0);
            g.vertices .put(0) .put(1).put(0)   .put(0).put(1).put(0);
            g.vertices .put(1).put(-1).put(0)   .put(0).put(0).put(1);
            
            g.vertices.flip();
        }
        
        g.bindBuffers();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (6 * Float.BYTES), 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, (6 * Float.BYTES), (3 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    @Override
    void update() {
        g.modelMatrix.translation(position);
    }

    @Override
    void render(Program sceneProgram, Vector3f camPos, Vector3f camUp) {
        if(visible) {
            glBindVertexArray(g.vao);
            
            sceneProgram.setUniform("uType", 0);
            sceneProgram.setUniform("uModel", false, g.modelMatrix);

            glDrawArrays(GL_TRIANGLES, 0, 3);
            App.checkGLError();
        }
        
        //TODO: investigate (1281) invalid value here.
    }

}