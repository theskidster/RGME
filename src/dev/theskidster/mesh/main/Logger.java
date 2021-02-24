package dev.theskidster.mesh.main;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.lwjgl.glfw.GLFW.glfwGetVersionString;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

/**
 * @author J Hoffman
 * Created: Feb 23, 2021
 */

public final class Logger {
    
    private static PrintWriter writer;
    private final static StringBuilder builder = new StringBuilder();
    
    static void logSystemInfo() {
        logInfo("--------------------------------------------------------------------------------");
        logInfo("OS NAME:\t\t" + System.getProperty("os.name"));
        logInfo("JAVA VERSION:\t" + System.getProperty("java.version"));
        logInfo("GLFW VERSION:\t" + glfwGetVersionString());
        logInfo("OPENGL VERSION:\t" + glGetString(GL_VERSION));
        logInfo("APP VERSION:\t" + App.VERSION);
        logInfo("--------------------------------------------------------------------------------");
    }
    
    public static void logInfo(String message) {
        System.out.println("INFO: " + message);
                
        builder.append("INFO: ")
               .append(message)
               .append(System.lineSeparator());
    }
    
    public static void logWarning(String message, Exception e) {
        String timestamp = new SimpleDateFormat("MM-dd-yyyy h:mma").format(new Date());
                
        System.out.println(System.lineSeparator() + timestamp);
        System.out.println("WARNING: " + message + System.lineSeparator());

        builder.append(System.lineSeparator())
               .append(timestamp)
               .append(System.lineSeparator())
               .append("WARNING: ")
               .append(message)
               .append(System.lineSeparator())
               .append(System.lineSeparator());

        if(e != null) {
            var stackTrace = e.getStackTrace();

            System.out.println(e.toString());
            builder.append(e.toString())
                   .append(System.lineSeparator());

            for(StackTraceElement element : stackTrace) {
                System.out.println("\t" + element.toString());

                builder.append("\t")
                       .append(element.toString())
                       .append(System.lineSeparator());
            }

            System.out.println();
            builder.append(System.lineSeparator());
        }
    }
    
    public static void logSevere(String message, Exception e) {
        String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
        String time = new SimpleDateFormat("h:mma").format(new Date());
        
        String timestamp = date + " " + time;
                
        System.err.println(System.lineSeparator() + timestamp);
        System.err.println("ERROR: " + message + System.lineSeparator());

        builder.append(System.lineSeparator())
               .append(timestamp)
               .append(System.lineSeparator())
               .append("ERROR: ")
               .append(message)
               .append(System.lineSeparator())
               .append(System.lineSeparator());

        if(e == null) e = new RuntimeException();
        var stackTrace = e.getStackTrace();
        
        System.err.println(e.toString());
        builder.append(e.toString())
               .append(System.lineSeparator());

        for(StackTraceElement element : stackTrace) {
            System.err.println("\t" + element.toString());

            builder.append("\t")
                   .append(element.toString())
                   .append(System.lineSeparator());
        }

        System.err.println();
        builder.append(System.lineSeparator());
        
        File file     = new File("log " + date + ".txt");
        int duplicate = 0;

        while(file.exists()) {
            duplicate++;
            file = new File("log " + date + " (" + duplicate + ").txt");
        }

        try(FileWriter logFile = new FileWriter(file.getName())) {
            writer = new PrintWriter(logFile);
            writer.append(builder.toString());
            writer.close();
        } catch(Exception ex) {}

        System.exit(-1);
    }
    
}