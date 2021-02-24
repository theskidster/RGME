package dev.theskidster.mesh.main;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

public final class App {

    private final Monitor monitor;
    private final Window window;
    
    App() {
        glfwInit();
        
        monitor = new Monitor();
        window  = new Window("Mesh Manipulator", monitor);
        
        
    }
    
    void start() {
        window.show(monitor);
        
        while(!glfwWindowShouldClose(window.handle)) {
            glfwPollEvents();
            glfwSwapBuffers(window.handle);
        }
        
        glfwTerminate();
    }
    
}