package dev.theskidster.rgme.main;

import dev.theskidster.rgme.commands.CommandHistory;
import dev.theskidster.rgme.scene.Scene;
import dev.theskidster.rgme.ui.UI;
import static dev.theskidster.rgme.ui.UI.TOOLBAR_WIDTH;
import dev.theskidster.rgme.utils.Color;
import java.util.LinkedList;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

public final class App {

    public static final int MAX_LIGHTS = 32;
    
    private static int tickCount = 0;
    
    private static boolean vSync = true;
    
    public static final String DOMAIN  = "rgme";
    public static final String VERSION = "0.0.0";
    
    private final Monitor monitor;
    private final Window window;
    private final Program uiProgram;
    private final Program sceneProgram;
    private final UI ui;
    private final Camera camera;
    private final Scene scene;
    
    private static final Vector3f noValue = new Vector3f();
    public final CommandHistory cmdHistory = new CommandHistory();
    
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
            uiProgram.addUniform(BufferType.VEC2, "uTexCoords");
            uiProgram.addUniform(BufferType.VEC2, "uPosition");
            uiProgram.addUniform(BufferType.VEC3, "uColor");
            uiProgram.addUniform(BufferType.MAT4, "uProjection");
        }
        
        {
            var shaderSourceFiles = new LinkedList<Shader>() {{
                add(new Shader("sceneVertex.glsl", GL_VERTEX_SHADER));
                add(new Shader("sceneFragment.glsl", GL_FRAGMENT_SHADER));
            }};
            
            sceneProgram = new Program(shaderSourceFiles, "scene");
            sceneProgram.use();
            
            sceneProgram.addUniform(BufferType.INT,  "uType");
            sceneProgram.addUniform(BufferType.INT,  "uNumLights");
            sceneProgram.addUniform(BufferType.VEC3, "uColor");
            sceneProgram.addUniform(BufferType.MAT3, "uNormal");
            sceneProgram.addUniform(BufferType.MAT4, "uModel");
            sceneProgram.addUniform(BufferType.MAT4, "uView");
            sceneProgram.addUniform(BufferType.MAT4, "uProjection");
            
            for(int i = 0; i < MAX_LIGHTS; i++) {
                sceneProgram.addUniform(BufferType.FLOAT, "uLights[" + i + "].brightness");
                sceneProgram.addUniform(BufferType.FLOAT, "uLights[" + i + "].contrast");
                sceneProgram.addUniform(BufferType.VEC3,  "uLights[" + i + "].position");
                sceneProgram.addUniform(BufferType.VEC3,  "uLights[" + i + "].ambient");
                sceneProgram.addUniform(BufferType.VEC3,  "uLights[" + i + "].diffuse");
            }
        }
        
        scene  = new Scene(16, 32, 16, Color.RGME_BLACK);
        ui     = new UI(window, scene);
        camera = new Camera();
    }
    
    void start() {
        window.show(monitor, scene, ui, camera, cmdHistory);
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
                
                camera.update(window.width - TOOLBAR_WIDTH, window.height);
                scene.update(ui.getCurrTool());
                ui.update(cmdHistory);
            }
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            if(!window.minimized) {
                sceneProgram.use();
                glViewport(0, 0, window.width - TOOLBAR_WIDTH, window.height);
                camera.render(sceneProgram);
                scene.render(sceneProgram, camera.position, camera.up, ui.getCurrTool());

                uiProgram.use();
                glViewport(0, 0, window.width, window.height);
                ui.render(uiProgram);
            }
            
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
    
    public static boolean tick(int cycles) {
        return tickCount % cycles == 0;
    }
    
    public static void setClearColor(Color color) {
        glClearColor(color.r, color.g, color.b, 0);
    }
    
    public static Vector3f noValue() {
        return noValue;
    }
    
}