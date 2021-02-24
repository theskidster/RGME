package dev.theskidster.mesh.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

final class Shader {
    
    final int handle;
    
    Shader(String filename, int type) {
        String filepath       = "/dev/theskidster/" + App.DOMAIN + "/shaders/" + filename;
        StringBuilder builder = new StringBuilder();
        InputStream file      = Shader.class.getResourceAsStream(filepath);
        
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file, "UTF-8"))) {
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch(Exception e) {
            Logger.logSevere("Failed to pase GLSL file: \"" + filename + "\"", e);
        }
        
        CharSequence sourceCode = builder.toString();
        
        handle = glCreateShader(type);
        glShaderSource(handle, sourceCode);
        glCompileShader(handle);
        
        if(glGetShaderi(handle, GL_COMPILE_STATUS) != GL_TRUE) {
            Logger.logSevere("Failed to compile GLSL file: \"" + filename + "\" " + glGetShaderInfoLog(handle), null);
        }
    }
    
}