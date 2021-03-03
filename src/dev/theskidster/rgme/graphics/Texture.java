package dev.theskidster.rgme.graphics;

import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.STBI_rgb_alpha;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * @author J Hoffman
 * Created: Mar 2, 2021
 */

public final class Texture {

    public final int handle;
    private int width;
    private int height;
    private int channels;
    
    public Texture(String filename) {
        handle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, handle);
        
        String filepath = "/dev/theskidster/" + App.DOMAIN + "/assets/";
        
        try(InputStream file = Texture.class.getResourceAsStream(filepath + filename)) {
            loadTexture(file);
        } catch(Exception e) {
            Logger.logWarning("Failed to load texture image: \"" + filename + "\"", e);
            loadTexture(Texture.class.getResourceAsStream(filepath + "img_null.png"));
        }
        
        App.checkGLError();
    }
    
    private void loadTexture(InputStream file) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            byte[] data = file.readAllBytes();
            
            ByteBuffer imageBuf  = MemoryUtil.memAlloc(data.length).put(data).flip();
            IntBuffer widthBuf   = stack.mallocInt(1);
            IntBuffer heightBuf  = stack.mallocInt(1);
            IntBuffer channelBuf = stack.mallocInt(1);
            
            ByteBuffer texture = stbi_load_from_memory(imageBuf, widthBuf, heightBuf, channelBuf, STBI_rgb_alpha);
            
            width    = widthBuf.get();
            height   = heightBuf.get();
            channels = channelBuf.get();
            
            if(texture != null) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
            } else {
                throw new NullPointerException("STBI failed to parse texture image data.");
            }
            
            stbi_image_free(texture);
            MemoryUtil.memFree(imageBuf);
            
        } catch(IOException e) {
            Logger.logSevere("Failed to load fallback texture.", e);
        }
    }
    
    public int getWidth()    { return width; }
    public int getHeight()   { return height; }
    public int getChannels() { return channels; }
    
    public void freeTexture() {
        glDeleteTextures(handle);
    }
    
}