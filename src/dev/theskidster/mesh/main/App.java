package dev.theskidster.mesh.main;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

public final class App {

    public static final String DOMAIN = "mesh";
    
    private final Monitor monitor;
    private final Window window;
    
    App() {
        glfwInit();
        
        monitor = new Monitor();
        window  = new Window("Mesh Manipulator", monitor);
        
        glfwMakeContextCurrent(window.handle);
        GL.createCapabilities();
        
        //Establish the shader program that will be used to render the applications UI.
        {
            
        }
        
    }
    
    void start() {
        window.show(monitor);
        
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