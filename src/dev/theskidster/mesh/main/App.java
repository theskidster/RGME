package dev.theskidster.mesh.main;

import java.util.LinkedList;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

public final class App {

    public static final String DOMAIN  = "mesh";
    public static final String VERSION = "0.0.0";
    
    private final Monitor monitor;
    private final Window window;
    private final Program uiProgram;
    
    App() {
        glfwInit();
        
        monitor = new Monitor();
        window  = new Window("Mesh Manipulator", monitor);
        
        glfwMakeContextCurrent(window.handle);
        GL.createCapabilities();
        
        //Establish the shader program that will be used to render the applications UI.
        {
            var shaderSourceFiles = new LinkedList<Shader>() {{
                add(new Shader("uiVertex.glsl", GL_VERTEX_SHADER));
                add(new Shader("uiFragment.glsl", GL_FRAGMENT_SHADER));
            }};
            
            uiProgram = new Program(shaderSourceFiles, "ui");
            uiProgram.use();
            
            uiProgram.addUniform(BufferType.MAT4, "uProjection");
        }
        
    }
    
    void start() {
        window.show(monitor);
        Logger.logSystemInfo();
        
        while(!glfwWindowShouldClose(window.handle)) {
            glfwPollEvents();
            glfwSwapBuffers(window.handle);
        }
        
        GL.destroy();
        glfwTerminate();
    }
    
    void finish() {
        glfwSetWindowShouldClose(window.handle, true);
    }
    
}