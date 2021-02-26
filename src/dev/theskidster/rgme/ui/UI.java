package dev.theskidster.rgme.ui;

import com.mlomb.freetypejni.FreeType;
import com.mlomb.freetypejni.Library;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import org.joml.Matrix4f;

/**
 * @author J Hoffman
 * Created: Feb 24, 2021
 */

public final class UI {

    int viewportWidth;
    int viewportHeight;
    
    private final Library freeType;
    private FreeTypeFont font;
    private final Background background = new Background();
    private final Matrix4f projMatrix   = new Matrix4f();
    
    public UI(long windowHandle, int windowWidth, int windowHeight) {
        freeType = FreeType.newLibrary();
        
        setFont("fnt_karla_regular.ttf", 17);
        setViewportSize(windowWidth, windowHeight);
    }
    
    public void update() {
        float hw = (2f / viewportWidth);
        float hh = (-2f / viewportHeight);
        
        projMatrix.set(hw,  0,  0, 0, 
                        0, hh,  0, 0, 
                        0,  0, -1, 0, 
                       -1,  1,  0, 1);
    }
    
    public void render(Program uiProgram) {
        uiProgram.setUniform("uProjection", false, projMatrix);
        
        font.drawString("bleh", 100, 200, 1, Color.WHITE, uiProgram);
    }
    
    public void setFont(String filename, int size) {
        font = new FreeTypeFont(freeType, filename, size);
    }
    
    public void setViewportSize(int windowWidth, int windowHeight) {
        viewportWidth  = windowWidth;
        viewportHeight = windowHeight;
        
        projMatrix.setPerspective((float) Math.toRadians(45), 
                                  (float) viewportWidth / viewportHeight, 
                                  0.1f, 
                                  Float.POSITIVE_INFINITY);
    }
    
    public void destroy() {
        freeType.delete();
        font.freeBuffers();
        
    }
    
}