package dev.theskidster.rgme.main;

import java.nio.IntBuffer;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

final class Window {

    int xPos;
    int yPos;
    int width;
    int height;
    
    final long handle;
    
    final String title;
    
    Window(String title, Monitor monitor) {
        this.title = title;
        
        width  = 800;
        height = 600;
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer xStartBuf = stack.mallocInt(1);
            IntBuffer yStartBuf = stack.mallocInt(1);
            
            glfwGetMonitorPos(monitor.handle, xStartBuf, yStartBuf);
            
            xPos = Math.round((monitor.width - width) / 2) + xStartBuf.get();
            yPos = Math.round((monitor.height - height) / 2) + yStartBuf.get();
        }
        
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        
        handle = glfwCreateWindow(width, height, title, NULL, NULL);
    }
    
    void show(Monitor monitor) {
        glfwSetWindowMonitor(handle, NULL, xPos, yPos, width, height, monitor.refreshRate);
        glfwSetWindowPos(handle, xPos, yPos);
        glfwSwapInterval(1);
        glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        glfwShowWindow(handle);
    }
    
}