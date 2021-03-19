package dev.theskidster.rgme.utils;

import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Feb 24, 2021
 */

public final class Color {

    //XJGE color palette
    public static final Color WHITE = new Color(1);
    
    //RGME color palette
    public static final Color RGME_WHITE       = new Color(215, 216, 223);
    public static final Color RGME_SILVER      = new Color(161, 162, 179);
    public static final Color RGME_LIGHT_GRAY  = new Color(85, 93, 109);
    public static final Color RGME_MEDIUM_GRAY = new Color(53, 58, 76);
    public static final Color RGME_DARK_GRAY   = new Color(40, 45, 62);
    public static final Color RGME_SLATE_GRAY  = new Color(32, 35, 51);
    public static final Color RGME_BLACK       = new Color(19, 22, 29);
    public static final Color RGME_RED         = new Color(232, 17, 35);
    public static final Color RGME_GREEN       = new Color(4, 186, 0);
    public static final Color RGME_BLUE        = new Color(51, 102, 204);
    public static final Color RGME_YELLOW      = new Color(237, 251, 93);
    public static final Color RGME_NAVY        = new Color(19, 22, 29);
    
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
    
    public static Color random() {
        return new Color(
                (int) (Math.random() * 128),
                (int) (Math.random() * 128),
                (int) (Math.random() * 128));
    }
    
    public Vector3f asVec3() {
        return conversion;
    }
    
}