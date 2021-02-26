package dev.theskidster.rgme.utils;

import org.joml.Vector2i;

/**
 * @author J Hoffman
 * Created: Feb 25, 2021
 */

public class Rectangle {

    public int xPos;
    public int yPos;
    
    public float width;
    public float height;
    
    public Rectangle(int xPos, int yPos, int width, int height) {
        this.xPos   = xPos;
        this.yPos   = yPos;
        this.width  = width;
        this.height = height;
    }
    
    public boolean contains(Vector2i point) {
        return (point.x > xPos && point.x < xPos + width) && 
               (point.y > yPos && point.y < yPos + height);
    }
    
}