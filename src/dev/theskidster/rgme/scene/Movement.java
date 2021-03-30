package dev.theskidster.rgme.scene;

/**
 * @author J Hoffman
 * Created: Mar 30, 2021
 */

class Movement {
    
    String axis;
    float value;
    
    Movement() {
        axis  = "";
        value = 0;
    }
    
    Movement(String axis, float value) {
        this.axis  = axis;
        this.value = value;
    }
    
}