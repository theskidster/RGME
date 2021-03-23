package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.graphics.Atlas;
import dev.theskidster.rgme.graphics.Graphics;
import dev.theskidster.rgme.graphics.Texture;
import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Light;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Mar 23, 2021
 */

public class LightSource extends GameObject {
    
    public boolean enabled = true;
    
    private final Light light;
    private final Graphics g = new Graphics();
    private static final Texture texture;
    private static final Atlas atlas;
    
    static {
        texture = new Texture("spr_icons.png");
        
        glBindTexture(GL_TEXTURE_2D, texture.handle);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glBindTexture(GL_TEXTURE_2D, 0);
        
        atlas = new Atlas(texture, 30, 30);
    }
    
    public LightSource(Light light) {
        super("World Light");
        
        position   = new Vector3f(light.position);
        this.light = new Light(light.brightness, light.contrast, position, light.ambientColor, light.diffuseColor);
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            g.vertices = stack.mallocFloat(20);
            g.indices  = stack.mallocInt(6);
            float size = 0.5f;
            
            //(vec3 position), (vec2 texCoords)
            g.vertices.put(-size).put(-size).put(0) .put(atlas.subImageWidth * 0).put(atlas.subImageHeight * 4);
            g.vertices.put( size).put(-size).put(0) .put(atlas.subImageWidth * 1).put(atlas.subImageHeight * 4);
            g.vertices.put( size).put( size).put(0) .put(atlas.subImageWidth * 1).put(atlas.subImageHeight * 3);
            g.vertices.put(-size).put( size).put(0) .put(atlas.subImageWidth * 0).put(atlas.subImageHeight * 3);
            
            g.indices.put(0).put(1).put(2);
            g.indices.put(2).put(3).put(0);
            
            g.vertices.flip();
            g.indices.flip();
        }
        
        init();
    }
    
    public LightSource() {
        super("Light");
        
        position = new Vector3f();
        light    = new Light(0.5f, 0.1f, position, Color.WHITE, Color.WHITE);
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            g.vertices = stack.mallocFloat(20);
            g.indices  = stack.mallocInt(6);
            float size = 0.5f;
            
            //(vec3 position), (vec2 texCoords)
            g.vertices.put(-size).put(-size).put(0) .put(atlas.subImageWidth * 1).put(atlas.subImageHeight * 4);
            g.vertices.put( size).put(-size).put(0) .put(atlas.subImageWidth * 2).put(atlas.subImageHeight * 4);
            g.vertices.put( size).put( size).put(0) .put(atlas.subImageWidth * 2).put(atlas.subImageHeight * 3);
            g.vertices.put(-size).put( size).put(0) .put(atlas.subImageWidth * 1).put(atlas.subImageHeight * 3);
            
            g.indices.put(0).put(1).put(2);
            g.indices.put(2).put(3).put(0);
            
            g.vertices.flip();
            g.indices.flip();
        }
        
        init();
    }
    
    private void init() {
        g.bindBuffers();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (5 * Float.BYTES), 0);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, (5 * Float.BYTES), (3 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(2);
    }
    
    public void update() {
        light.position = position;
        g.modelMatrix.translation(position);
    }
    
    public void render(Program sceneProgram, Vector3f camPos, Vector3f camUp) {
        g.modelMatrix.billboardSpherical(position, camPos, camUp);
        
        glBindVertexArray(g.vao);
        glBindTexture(GL_TEXTURE_2D, texture.handle);
        
        sceneProgram.setUniform("uType", 4);
        sceneProgram.setUniform("uModel", false, g.modelMatrix);
        sceneProgram.setUniform("uColor", light.ambient);

        glDrawElements(GL_TRIANGLES, g.indices.limit(), GL_UNSIGNED_INT, 0);

        App.checkGLError();
    }
    
    public float getBrightness()       { return light.brightness; }
    public float getContrast()         { return light.contrast; }
    public Vector3f getAmbientColor()  { return light.ambient; }
    public Vector3f getDiffuseColor()  { return light.diffuse; }
    
    public void setBrightness(float brightness) {
        light.brightness = brightness;
    }
    
    public void setContrast(float contrast) {
        light.contrast = contrast;
    }
    
    public void setAmbientColor(Color color) {
        light.ambientColor = color;
        light.ambient      = Color.convert(color);
    }
    
    public void setDiffuseColor(Color color) {
        light.diffuseColor = color;
        light.diffuse      = Color.convert(color);
    }

}