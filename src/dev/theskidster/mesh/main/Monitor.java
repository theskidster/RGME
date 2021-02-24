package dev.theskidster.mesh.main;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import org.lwjgl.glfw.GLFWVidMode;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

final class Monitor {

    int width;
    int height;
    int refreshRate;
    
    final long handle;
    
    private final GLFWVidMode videoMode;
    
    Monitor() {        
        handle    = glfwGetPrimaryMonitor();
        videoMode = glfwGetVideoMode(handle);
        
        width       = videoMode.width();
        height      = videoMode.height();
        refreshRate = videoMode.refreshRate();
    }
    
}