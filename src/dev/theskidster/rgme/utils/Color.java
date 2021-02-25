package dev.theskidster.rgme.utils;

import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Feb 24, 2021
 */

public final class Color {

    public static final Color WHITE = new Color(1);
    public static final Color GRAY  = new Color(0.5f);
    public static final Color BLACK = new Color();
    
    public final float r;
    public final float g;
    public final float b;
    
    private Color() {
        r = g = b = 0;
    }
    
    private Color(float scalar) {
        r = g = b = scalar;
    }
    
    private Color(int r, int g, int b) {
        this.r = r / 255f;
        this.g = g / 255f;
        this.b = b / 255f;
    }
    
    public static Color create(int r, int g, int b) {
        return new Color(r, g, b);
    }
    
    public static Vector3f convert(Color color) {
        return new Vector3f(color.r, color.g, color.b);
    }
    
}