package dev.theskidster.rgme.ui;

import com.mlomb.freetypejni.Face;
import static com.mlomb.freetypejni.FreeTypeConstants.FT_LOAD_RENDER;
import com.mlomb.freetypejni.Library;
import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Logger;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Feb 24, 2021
 */

/*
    TODO: !IMPORTANT
    to make use of the freetype binding the JRE needed to be retrofitted with 
    an additional dll file (freetype-jni-64.dll). Launch4J will need to be used 
    to distribute the application as an executable.
*/

public final class FreeTypeFont {
    
    private final class Glyph {
        int texHandle;
        int advance;
        int width;
        int height;
        int bearingX;
        int bearingY;
    }
    
    private final int FLOATS_PER_GLYPH = 24;
    
    private final int vao = glGenVertexArrays();
    private final int vbo = glGenBuffers();
    
    private static final Map<Character, Glyph> glyphs = new HashMap<>();
    
    FreeTypeFont(Library freeType, String filename, int size) {
        String filepath = "/dev/theskidster/" + App.DOMAIN + "/assets/" + filename;
        
        try(InputStream file = FreeTypeFont.class.getResourceAsStream(filepath)) {
            loadFont(freeType, file, size);
            
            glBindVertexArray(vao);
            
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, FLOATS_PER_GLYPH * Float.BYTES, GL_DYNAMIC_DRAW);
            
            glVertexAttribPointer(0, 2, GL_FLOAT, false, (4 * Float.BYTES), 0);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, (4 * Float.BYTES), (2 * Float.BYTES));
            
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            
        } catch(Exception e) {
            Logger.logSevere("Failed to load font: \"" + filename + "\"", e);
        }
    }
    
    private void loadFont(Library freeType, InputStream file, int size) {
        try {
            Face face = freeType.newFace(file.readAllBytes(), 0);
            face.setPixelSizes(0, size);
            
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            
            for(char c = 0; c < 128; c++) {
                face.loadChar(c, FT_LOAD_RENDER);
                
                Glyph g     = new Glyph();
                g.texHandle = glGenTextures();
                g.advance   = face.getGlyphSlot().getAdvance().getX();
                g.width     = face.getGlyphSlot().getBitmap().getWidth();
                g.height    = face.getGlyphSlot().getBitmap().getRows();
                g.bearingX  = face.getGlyphSlot().getBitmapLeft();
                g.bearingY  = face.getGlyphSlot().getBitmapTop();
                
                glBindTexture(GL_TEXTURE_2D, g.texHandle);
                glTexImage2D(GL_TEXTURE_2D, 
                             0, 
                             GL_RED, 
                             g.width, 
                             g.height, 
                             0, 
                             GL_RED, 
                             GL_UNSIGNED_BYTE, 
                             face.getGlyphSlot().getBitmap().getBuffer());
                
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                
                glyphs.put(c, g);
            }
            
            face.delete();
            
        } catch(IOException e) {
            Logger.logSevere("Failed to parse font data from ttf file", e);
        }
    }
    
    public void drawString(String text, float xPos, float yPos, float scale, Color color, Program uiProgram) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glBindVertexArray(vao);
        glActiveTexture(GL_TEXTURE0);
        
        uiProgram.setUniform("uType", 0);
        uiProgram.setUniform("uColor", color.asVec3());
        
        for(char c : text.toCharArray()) {
            Glyph g = glyphs.get(c);
            
            float x = xPos + g.bearingX * scale;
            float y = yPos + (g.height - g.bearingY) * scale;
            float w = g.width * scale;
            float h = g.height * scale;
            
            glBindTexture(GL_TEXTURE_2D, g.texHandle);
            
            try(MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer vertexBuf = stack.mallocFloat(FLOATS_PER_GLYPH);
                
                //(vec2 position), (vec2 texCoords)
                vertexBuf.put(x)    .put(y - h).put(0).put(0);
                vertexBuf.put(x)    .put(y)    .put(0).put(1);
                vertexBuf.put(x + w).put(y)    .put(1).put(1);
                vertexBuf.put(x)    .put(y - h).put(0).put(0);
                vertexBuf.put(x + w).put(y)    .put(1).put(1);
                vertexBuf.put(x + w).put(y - h).put(1).put(0);
                
                vertexBuf.flip();
                
                glBindBuffer(GL_ARRAY_BUFFER, vbo);
                glBufferSubData(GL_ARRAY_BUFFER, 0, vertexBuf);
            }
            
            glDrawArrays(GL_TRIANGLES, 0, 6);
            xPos += (g.advance >> 6) * scale;
        }
        
        glDisable(GL_BLEND);
        App.checkGLError();
    }
    
    public void drawString(String text, float x, float y, float scale, Color color, Rectangle scissorBox, Program uiProgram) {
        glEnable(GL_SCISSOR_TEST);
        
        glScissor((int) scissorBox.xPos, (int) scissorBox.yPos, (int) scissorBox.width, (int) scissorBox.height);
        drawString(text, x, y, scale, color, uiProgram);
        
        glDisable(GL_SCISSOR_TEST);
    }
    
    public static int getLengthInPixels(String text, float scale) {
        int length = 0;
        for(char c : text.toCharArray()) length += (glyphs.get(c).advance >> 6) * scale;
        
        return length;
    }
    
    void freeBuffers() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
    }
    
}