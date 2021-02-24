package dev.theskidster.mesh.shaders;

import dev.theskidster.mesh.main.App;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

final class Source {
    
    final int handle;
    
    Source(String filename, int type) {
        String filepath       = "/dev/theskidster/" + App.DOMAIN + "/shaders/" + filename;
        StringBuilder builder = new StringBuilder();
        InputStream file      = Source.class.getResourceAsStream(filepath);
        
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file, "UTF-8"))) {
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch(Exception e) {
            //TODO: add logger
            System.out.println("failed to read");
        }
        
        CharSequence sourceCode = builder.toString();
        
        handle = glCreateShader(type);
        glShaderSource(handle, sourceCode);
        glCompileShader(handle);
        
        if(glGetShaderi(handle, GL_COMPILE_STATUS) != GL_TRUE) {
            //TODO: add logger
            System.out.println("failed to compile");
        }
    }
    
}