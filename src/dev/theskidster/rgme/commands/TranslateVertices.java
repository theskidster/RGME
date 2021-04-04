package dev.theskidster.rgme.commands;

import dev.theskidster.rgme.scene.SculptableGameObject;
import java.util.LinkedHashMap;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Apr 4, 2021
 */

public class TranslateVertices extends Command {

    private final LinkedHashMap<Integer, Vector3f> prevVertPos;
    private final LinkedHashMap<Integer, Vector3f> currVertPos;
    private final SculptableGameObject sculptable;
    
    public TranslateVertices(SculptableGameObject sculptable, LinkedHashMap<Integer, Vector3f> prevVertPos, LinkedHashMap<Integer, Vector3f> currVertPos) {
        this.sculptable  = sculptable;
        this.prevVertPos = prevVertPos;
        this.currVertPos = currVertPos;
    }
    
    @Override
    void execute() {
        sculptable.setVertexPositions(currVertPos);
    }

    @Override
    void undo() {
        sculptable.setVertexPositions(prevVertPos);
    }

}