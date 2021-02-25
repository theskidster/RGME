package dev.theskidster.rgme.ui;

import com.mlomb.freetypejni.FreeType;
import com.mlomb.freetypejni.Library;
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
    private final Matrix4f projMatrix = new Matrix4f();
    
    public UI(long winHandle) {
        freeType = FreeType.newLibrary();
        
        setFont("fnt_karla_regular.ttf", 17);
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
    
    public void setViewport(int width, int height) {
        viewportWidth  = width;
        viewportHeight = height;
        
        projMatrix.setPerspective((float) Math.toRadians(45), (float) width / height, 0.1f, Float.POSITIVE_INFINITY);
    }
    
    public void destroy() {
        freeType.delete();
    }
    
}