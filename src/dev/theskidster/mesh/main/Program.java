package dev.theskidster.mesh.main;

import dev.theskidster.mesh.main.Logger;
import dev.theskidster.mesh.shader.BufferType;
import dev.theskidster.mesh.shader.Shader;
import static dev.theskidster.mesh.shader.BufferType.*;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

public class Program {
    
    public final int handle;
    
    private final Map<String, UniformVariable> uniforms = new HashMap<>();
    private static final Map<BufferType, Integer> bufferSizes;
    
    static {
        bufferSizes = new HashMap<>() {{
            put(VEC2, 2);
            put(VEC3, 3);
            put(MAT3, 12);
            put(MAT4, 16);
        }};
    }
    
    public Program(LinkedList<Shader> shaders, String name) {
        handle = glCreateProgram();
        shaders.forEach(shader -> glAttachShader(handle, shader.handle));
        glLinkProgram(handle);
        
        if(glGetProgrami(handle, GL_LINK_STATUS) != GL_TRUE) {
            Logger.logSevere("Failed to link shader program: \"" + name + "\"" , null);
        }
    }
    
    private UniformVariable createUniform(String name, Buffer buffer) {
        return new UniformVariable(glGetUniformLocation(handle, name), buffer);
    }
    
    void addUniform(BufferType type, String name) {
        if(glGetUniformLocation(handle, name) == -1) {
            Logger.logSevere("Failed to find uniform: \"" + name + "\". Check " + 
                             "variable name or GLSL source file where it is declared.", 
                             null);
        }
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            switch(type) {
                case INT        -> uniforms.put(name, createUniform(name, stack.mallocInt(1)));
                case FLOAT      -> uniforms.put(name, createUniform(name, stack.mallocFloat(1)));
                case VEC2, VEC3 -> uniforms.put(name, createUniform(name, stack.mallocFloat(bufferSizes.get(type))));
                case MAT3, MAT4 -> uniforms.put(name, createUniform(name, stack.mallocFloat(bufferSizes.get(type) * Float.BYTES)));
            }
        }
    }
    
    void use() {
        glUseProgram(handle);
    }
    
    public void setUniform(String name, int value) {
        glUniform1i(uniforms.get(name).location, value);
    }
    
    public void setUniform(String name, float value) {
        glUniform1f(uniforms.get(name).location, value);
    }
    
    public void setUniform(String name, Vector2f value) {
        glUniform2fv(
                uniforms.get(name).location,
                value.get(uniforms.get(name).asFloatBuffer()));
    }
    
    public void setUniform(String name, Vector3f value) {
        glUniform3fv(
                uniforms.get(name).location,
                value.get(uniforms.get(name).asFloatBuffer()));
    }
    
    public void setUniform(String name, boolean transpose, Matrix3f value) {
        glUniformMatrix3fv(
                uniforms.get(name).location,
                transpose,
                value.get(uniforms.get(name).asFloatBuffer()));
    }
    
    public void setUniform(String name, boolean transpose, Matrix4f value) {
        glUniformMatrix4fv(
                uniforms.get(name).location,
                transpose,
                value.get(uniforms.get(name).asFloatBuffer()));
    }
    
}