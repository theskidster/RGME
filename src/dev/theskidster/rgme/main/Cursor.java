package dev.theskidster.rgme.main;

import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;
import static org.lwjgl.glfw.GLFW.glfwCreateStandardCursor;
import static org.lwjgl.glfw.GLFW.glfwDestroyCursor;
import static org.lwjgl.glfw.GLFW.glfwSetCursor;

/**
 * @author J Hoffman
 * Created: Mar 14, 2021
 */

final class Cursor {
    
    long handle;
    
    Cursor() {
        handle = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
    }
    
    void setShape(long windowHandle, int shape) {
        glfwDestroyCursor(handle);
        handle = glfwCreateStandardCursor(shape);
        glfwSetCursor(windowHandle, handle);
    }
    
}