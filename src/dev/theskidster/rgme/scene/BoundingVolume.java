package dev.theskidster.rgme.scene;

import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.utils.Color;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;

/**
 * @author J Hoffman
 * Created: Mar 23, 2021
 */

public class BoundingVolume extends Polyhedron {

    Matrix4f modelMatrix = new Matrix4f();
    
    public BoundingVolume() {
        super("Volume", 3);
        
        findBufferSize();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (floatsPerVertex * Float.BYTES), 0);
        
        glEnableVertexAttribArray(0);
        
        position = new Vector3f();
    }

    @Override
    void addShape(float x, float y, float z) {
        genVertexPositions(x, y, z);
        
        shapeHeight = 1;
        fOffset     = faces.size();
        
        //FRONT:
        faces.put(fOffset,     new Face(new int[]{vpOffset,     vpOffset + 1, vpOffset + 2}));
        faces.put(fOffset + 1, new Face(new int[]{vpOffset + 2, vpOffset + 3, vpOffset}));
        //RIGHT:
        faces.put(fOffset + 2, new Face(new int[]{vpOffset + 1, vpOffset + 5, vpOffset + 6}));
        faces.put(fOffset + 3, new Face(new int[]{vpOffset + 6, vpOffset + 2, vpOffset + 1}));
        //BACK:
        faces.put(fOffset + 4, new Face(new int[]{vpOffset + 7, vpOffset + 6, vpOffset + 5}));
        faces.put(fOffset + 5, new Face(new int[]{vpOffset + 5, vpOffset + 4, vpOffset + 7}));
        //LEFT:
        faces.put(fOffset + 6, new Face(new int[]{vpOffset + 4, vpOffset,     vpOffset + 3}));
        faces.put(fOffset + 7, new Face(new int[]{vpOffset + 3, vpOffset + 7, vpOffset + 4}));
        //BOTTOM:
        faces.put(fOffset + 8, new Face(new int[]{vpOffset + 4, vpOffset + 5, vpOffset + 1}));
        faces.put(fOffset + 9, new Face(new int[]{vpOffset + 1, vpOffset,     vpOffset + 4}));
        //TOP:
        faces.put(fOffset + 10, new Face(new int[]{vpOffset + 3, vpOffset + 2, vpOffset + 6}));
        faces.put(fOffset + 11, new Face(new int[]{vpOffset + 6, vpOffset + 7, vpOffset + 3}));
        
        updateData = true;
    }

    @Override
    void update() {
        modelMatrix.translation(position);
    }
    
    @Override
    void draw(Program sceneProgram) {
        if(updateData) {
            findBufferSize();

            FloatBuffer vertices = MemoryUtil.memAllocFloat(bufferSizeInBytes);

            faces.forEach((id, face) -> {
                for(int i = 0; i < 3; i++) {
                    Vector3f pos = vertexPositions.get(face.vp[i]);
                    vertices.put(pos.x).put(pos.y).put(pos.z);
                }
            });

            vertices.flip();

            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            
            MemoryUtil.memFree(vertices);
            updateData = false;
        }
        
        //glEnable(GL_CULL_FACE);
        //glEnable(GL_DEPTH_TEST);
        
        glBindVertexArray(vao);
        
        sceneProgram.setUniform("uType", 3);
        sceneProgram.setUniform("uColor", Color.RGME_YELLOW.asVec3());
        sceneProgram.setUniform("uModel", false, modelMatrix);
        
        glDrawArrays(GL_TRIANGLES, 0, numVertices);
        
        //glDisable(GL_CULL_FACE);
        //glDisable(GL_DEPTH_TEST);
        
        App.checkGLError();
    }

}