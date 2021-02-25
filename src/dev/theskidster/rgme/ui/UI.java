package dev.theskidster.rgme.ui;

import com.mlomb.freetypejni.FreeType;
import com.mlomb.freetypejni.Library;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;

/**
 * @author J Hoffman
 * Created: Feb 24, 2021
 */

public final class UI {

    private final Library freeType;
    private FreeTypeFont font;
    
    public UI(long winHandle) {
        freeType = FreeType.newLibrary();
        
        setFont("fnt_karla_regular.ttf", 17);
    }
    
    public void update() {
        
    }
    
    public void render(Program uiProgram) {
        //TODO: needs projection matrix to be set
        //font.drawString("bleh", 10, 10, 1, Color.WHITE, uiProgram);
    }
    
    public void setFont(String filename, int size) {
        font = new FreeTypeFont(freeType, filename, size);
    }
    
    public void destroy() {
        freeType.delete();
    }
    
}