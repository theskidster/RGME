package dev.theskidster.rgme.main;

import dev.theskidster.rgme.ui.UI;
import java.util.LinkedList;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

public final class App {

    private static int tickCount = 0;
    
    private static boolean vSync = true;
    
    public static final String DOMAIN  = "rgme";
    public static final String VERSION = "0.0.0";
    
    private final Monitor monitor;
    private final Window window;
    private final Program uiProgram;
    private final UI ui;
    
    App() {
        glfwInit();
        
        monitor = new Monitor();
        window  = new Window("RGME v" + VERSION, monitor);
        
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
            
            uiProgram.addUniform(BufferType.INT,  "uType");
            uiProgram.addUniform(BufferType.VEC3, "uColor");
            uiProgram.addUniform(BufferType.MAT4, "uProjection");
        }
        
        //TODO: initialize scene shader
        {
            
        }
        
        ui = new UI(window.handle);
    }
    
    void start() {
        window.show(monitor, ui);
        Logger.logSystemInfo();
        
        final double TARGET_DELTA = 1 / 60.0;
        double prevTime = glfwGetTime();
        double currTime;
        double delta = 0;
        boolean ticked;
        
        while(!glfwWindowShouldClose(window.handle)) {
            currTime = glfwGetTime();
            
            delta += currTime - prevTime;
            if(delta < TARGET_DELTA && vSync) delta = TARGET_DELTA;
            
            prevTime = currTime;
            ticked   = false;
            
            while(delta >= TARGET_DELTA) {
                delta -= TARGET_DELTA;
                ticked = true;
                tickCount = (tickCount == Integer.MAX_VALUE) ? 0 : tickCount + 1;
                
                glfwPollEvents();
                
                ui.update(window.width, window.height);
            }
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            uiProgram.use();
            ui.render(uiProgram);
            
            glfwSwapBuffers(window.handle);
            
            if(!ticked) {
                try {
                    Thread.sleep(1);
                } catch(InterruptedException e) {}
            }
        }
        
        ui.destroy();
        GL.destroy();
        glfwTerminate();
    }
    
    void finish() {
        glfwSetWindowShouldClose(window.handle, true);
    }
    
    public static void checkGLError() {
        int glError = glGetError();
        
        if(glError != GL_NO_ERROR) {
            String desc = "";
            
            switch(glError) {
                case GL_INVALID_ENUM      -> desc = "invalid enum";
                case GL_INVALID_VALUE     -> desc = "invalid value";
                case GL_INVALID_OPERATION -> desc = "invalid operation";
                case GL_STACK_OVERFLOW    -> desc = "stack overflow";
                case GL_STACK_UNDERFLOW   -> desc = "stack underflow";
                case GL_OUT_OF_MEMORY     -> desc = "out of memory";
            }
            
            Logger.logSevere("OpenGL Error: (" + glError + ") " + desc, null);
        }
    }
    
}