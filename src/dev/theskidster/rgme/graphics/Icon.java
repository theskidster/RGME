package dev.theskidster.rgme.graphics;

import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Logger;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Mar 2, 2021
 */

public final class Icon {

    private final Atlas atlas;
    private Color color            = Color.SILVER;
    private Vector2f currCell      = new Vector2f();
    public final Vector2f position = new Vector2f();
    private final Graphics g       = new Graphics();
    private static final Texture texture;
    
    static { 
        texture = new Texture("spr_icons.png"); 
        
        glBindTexture(GL_TEXTURE_2D, texture.handle);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    public Icon(int cellWidth, int cellHeight) {
        atlas = new Atlas(texture, cellWidth, cellHeight);
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            g.vertices = stack.mallocFloat(28);
            g.indices  = stack.mallocInt(6);
            
            //(vec2 position), (vec2 tex coords) (vec3 color)
            g.vertices.put(0)        .put(-cellHeight)  .put(0)                  .put(0)                   .put(1).put(1).put(1);
            g.vertices.put(cellWidth).put(-cellHeight)  .put(atlas.subImageWidth).put(0)                   .put(1).put(1).put(1);
            g.vertices.put(cellWidth).put(0)            .put(atlas.subImageWidth).put(atlas.subImageHeight).put(1).put(1).put(1);
            g.vertices.put(0)        .put(0)            .put(0)                  .put(atlas.subImageHeight).put(1).put(1).put(1);
            
            g.indices.put(0).put(1).put(2);
            g.indices.put(2).put(3).put(0);
            
            g.vertices.flip();
            g.indices.flip();
        }
        
        g.bindBuffers();
        
        glVertexAttribPointer(0, 2, GL_FLOAT, false, (7 * Float.BYTES), 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, (7 * Float.BYTES), (2 * Float.BYTES));
        glVertexAttribPointer(2, 3, GL_FLOAT, false, (7 * Float.BYTES), (4 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }
    
    public void setSubImage(int cellX, int cellY) {
        Vector2i cell = new Vector2i(cellX, cellY);
        
        if(atlas.subImageOffsets.containsKey(cell)) {
            currCell = atlas.subImageOffsets.get(cell);
        } else {
            Logger.logWarning(
                    "Failed to set icon sub-image. The cell location: (" + cellX + 
                    ", " + cellY + ") is out of bounds.", 
                    null);
        }
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public void render(Program uiProgram) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glBindTexture(GL_TEXTURE_2D, texture.handle);
        glBindVertexArray(g.vao);
        
        uiProgram.setUniform("uType", 2);
        uiProgram.setUniform("uTexCoords", currCell);
        uiProgram.setUniform("uPosition", position);
        uiProgram.setUniform("uColor", color.asVec3());
        
        glDrawElements(GL_TRIANGLES, g.indices.limit(), GL_UNSIGNED_INT, 0);
        
        glDisable(GL_BLEND);
        
        App.checkGLError();
    }
    
    public void freeIcon() {
        g.freeBuffers();
    }
    
}