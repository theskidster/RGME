package dev.theskidster.rgme.scene;

/**
 * @author J Hoffman
 * Created: Mar 23, 2021
 */

final class Face {
    
    int[] vp = new int[3];
    int[] tc = new int[3];
    
    int n;
    
    Face(int[] vp) {
        this.vp = vp;
    }
    
    Face(int[] vp, int[] tc, int n) {
        this.vp = vp;
        this.tc = tc;
        this.n  = n;
    }
    
}