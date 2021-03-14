package dev.theskidster.rgme.main;

import dev.theskidster.rgme.ui.UI;
import static dev.theskidster.rgme.ui.widgets.SceneGraph.TOOLBAR_WIDTH;
import dev.theskidster.rgme.utils.Observable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWImage;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.stb.STBImage.*;
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
    
    private boolean mouseLeftHeld;
    private boolean mouseMiddleHeld;
    private boolean mouseRightHeld;
    
    final String title;
    
    private final Observable observable = new Observable(this);
    
    Window(String title, Monitor monitor) {
        this.title = title;
        
        width  = 1200;
        height = 800;
        
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
    
    private void setIcon(String filename) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            String filepath  = "/dev/theskidster/" + App.DOMAIN + "/assets/" + filename;
            InputStream file = Window.class.getResourceAsStream(filepath);
            byte[] data      = file.readAllBytes();
            
            IntBuffer widthBuf   = stack.mallocInt(1);
            IntBuffer heightBuf  = stack.mallocInt(1);
            IntBuffer channelBuf = stack.mallocInt(1);
            
            ByteBuffer icon = stbi_load_from_memory(
                    stack.malloc(data.length).put(data).flip(),
                    widthBuf,
                    heightBuf,
                    channelBuf,
                    STBI_rgb_alpha);
            
            glfwSetWindowIcon(handle, GLFWImage.mallocStack(1, stack)
                    .width(widthBuf.get())
                    .height(heightBuf.get())
                    .pixels(icon));
            
            stbi_image_free(icon);
            
        } catch(IOException e) {
            Logger.logWarning("Failed to set window icon: \"" + filename + "\"", e);
        }
    }
    
    void show(Monitor monitor, UI ui, Camera camera) {
        setIcon("img_logo.png");
        glfwSetWindowMonitor(handle, NULL, xPos, yPos, width, height, monitor.refreshRate);
        glfwSetWindowPos(handle, xPos, yPos);
        glfwSwapInterval(1);
        glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        glfwShowWindow(handle);
        
        observable.properties.put("windowWidth", width);
        observable.properties.put("windowHeight", height);
        
        ui.initializeObservers(observable);
        
        glfwSetWindowSizeCallback(handle, (window, w, h) -> {
            width  = w;
            height = h;
            
            glViewport(0, 0, width, height);
            
            observable.notifyObservers("windowWidth", width);
            observable.notifyObservers("windowHeight", height);
        });
        
        glfwSetCursorPosCallback(handle, (window, x, y) -> {
            ui.setMouseCursorPos(x, y);
            
            camera.castRay((float) ((2f * x) / (width - TOOLBAR_WIDTH) - 1f), (float) (1f - (2f * yPos) / height));
            
            if(!ui.getWidgetHovered()) {
                if(mouseLeftHeld ^ mouseMiddleHeld ^ mouseRightHeld) {
                    if(mouseMiddleHeld) camera.setPosition(x, y);
                    if(mouseRightHeld)  camera.setDirection(x, y);
                } else {
                    camera.prevX = x;
                    camera.prevY = y;
                }
            }
        });
        
        glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
            ui.setMouseAction(button, action);
            
            switch(button) {
                case GLFW_MOUSE_BUTTON_LEFT   -> mouseLeftHeld = (action == GLFW_PRESS);
                case GLFW_MOUSE_BUTTON_MIDDLE -> mouseMiddleHeld = (action == GLFW_PRESS);
                case GLFW_MOUSE_BUTTON_RIGHT  -> mouseRightHeld = (action == GLFW_PRESS);
            }
        });
        
        glfwSetScrollCallback(handle, (window, xOffset, yOffset) -> {
            ui.setMouseScroll((int) yOffset);
            
            if(!ui.getWidgetHovered()) camera.dolly((float) yOffset);
        });
        
        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            ui.captureKeyInput(key, action);
        });
    }
    
}