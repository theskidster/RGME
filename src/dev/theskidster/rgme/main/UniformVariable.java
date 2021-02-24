package dev.theskidster.rgme.main;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

final class UniformVariable {

    final int location;
    private Buffer buffer;
    
    UniformVariable(int location, Buffer buffer) {
        this.location = location;
        this.buffer   = buffer;
    }
    
    IntBuffer asIntBuffer()     { return (IntBuffer) buffer; }
    FloatBuffer asFloatBuffer() { return (FloatBuffer) buffer; }
    
}