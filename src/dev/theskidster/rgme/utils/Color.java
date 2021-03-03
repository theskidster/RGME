package dev.theskidster.rgme.utils;

import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Feb 24, 2021
 */

public final class Color {

    public static final Color WHITE = new Color(1);
    public static final Color DARK_GRAY  = new Color(0.2f);
    public static final Color BLACK = new Color();
    
    public final float r;
    public final float g;
    public final float b;
    
    private final Vector3f conversion;
    
    private Color() {
        r = g = b = 0;
        conversion = new Vector3f();
    }
    
    private Color(float scalar) {
        r = g = b = scalar;
        conversion = new Vector3f(scalar);
    }
    
    private Color(int r, int g, int b) {
        this.r = (r / 255f);
        this.g = (g / 255f);
        this.b = (b / 255f);
        
        conversion = new Vector3f(this.r, this.g, this.b);
    }
    
    public static Color create(int r, int g, int b) {
        return new Color(r, g, b);
    }
    
    public Vector3f asVec3() {
        return conversion;
    }
    
}