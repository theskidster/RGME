package dev.theskidster.rgme.ui.elements;

import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.UI;
import dev.theskidster.rgme.utils.Rectangle;
import dev.theskidster.rgme.utils.Timer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author J Hoffman
 * Created: Mar 2, 2021
 */

public abstract class TextInputElement extends Element {
    
    protected final int HEIGHT  = 30;
    protected final int PADDING = 4;
    
    protected final int width;
    private int currIndex;
    private int prevIndex;
    private int lengthToIndex;
    private int textOffset;
    protected int prevCursorX;
    protected int firstIndex;
    protected int lastIndex;
    
    private float parentPosX;
    private float parentPosY;
    
    private boolean hasFocus;
    protected boolean shiftHeld;
    protected boolean caratIdle;
    protected boolean caratBlink;
    protected boolean firstIndexSet;
    
    protected final StringBuilder typed = new StringBuilder();
    protected final Vector2f textPos    = new Vector2f();
    
    protected Rectangle rectBack;
    protected Rectangle rectFront;
    protected Rectangle highlight;
    protected final Timer timer;
    protected final Icon carat;
    public final Rectangle scissorBox = new Rectangle();
    
    protected static Map<Integer, Key> keyChars;
    
    static {
        keyChars = new HashMap<>() {{
            put(GLFW_KEY_SPACE,      new Key(' ', ' '));
            put(GLFW_KEY_APOSTROPHE, new Key('\'', '\"'));
            put(GLFW_KEY_COMMA,      new Key(',', '<'));
            put(GLFW_KEY_MINUS,      new Key('-', '_'));
            put(GLFW_KEY_PERIOD,     new Key('.', '>'));
            put(GLFW_KEY_SLASH,      new Key('/', '?'));
            put(GLFW_KEY_0, new Key('0', ')'));
            put(GLFW_KEY_1, new Key('1', '!'));
            put(GLFW_KEY_2, new Key('2', '@'));
            put(GLFW_KEY_3, new Key('3', '#'));
            put(GLFW_KEY_4, new Key('4', '$'));
            put(GLFW_KEY_5, new Key('5', '%'));
            put(GLFW_KEY_6, new Key('6', '^'));
            put(GLFW_KEY_7, new Key('7', '&'));
            put(GLFW_KEY_8, new Key('8', '*'));
            put(GLFW_KEY_9, new Key('9', '('));
            put(GLFW_KEY_SEMICOLON, new Key(';', ':'));
            put(GLFW_KEY_EQUAL,     new Key('=', '+'));
            put(GLFW_KEY_A, new Key('a', 'A'));
            put(GLFW_KEY_B, new Key('b', 'B'));
            put(GLFW_KEY_C, new Key('c', 'C'));
            put(GLFW_KEY_D, new Key('d', 'D'));
            put(GLFW_KEY_E, new Key('e', 'E'));
            put(GLFW_KEY_F, new Key('f', 'F'));
            put(GLFW_KEY_G, new Key('g', 'G'));
            put(GLFW_KEY_H, new Key('h', 'H'));
            put(GLFW_KEY_I, new Key('i', 'I'));
            put(GLFW_KEY_J, new Key('j', 'J'));
            put(GLFW_KEY_K, new Key('k', 'K'));
            put(GLFW_KEY_L, new Key('l', 'L'));
            put(GLFW_KEY_M, new Key('m', 'M'));
            put(GLFW_KEY_N, new Key('n', 'N'));
            put(GLFW_KEY_O, new Key('o', 'O'));
            put(GLFW_KEY_P, new Key('p', 'P'));
            put(GLFW_KEY_Q, new Key('q', 'Q'));
            put(GLFW_KEY_R, new Key('r', 'R'));
            put(GLFW_KEY_S, new Key('s', 'S'));
            put(GLFW_KEY_T, new Key('t', 'T'));
            put(GLFW_KEY_U, new Key('u', 'U'));
            put(GLFW_KEY_V, new Key('v', 'V'));
            put(GLFW_KEY_W, new Key('w', 'W'));
            put(GLFW_KEY_X, new Key('x', 'X'));
            put(GLFW_KEY_Y, new Key('y', 'Y'));
            put(GLFW_KEY_Z, new Key('z', 'Z'));
            put(GLFW_KEY_LEFT_BRACKET,  new Key('[', '{'));
            put(GLFW_KEY_BACKSLASH,     new Key('\\', '|'));
            put(GLFW_KEY_RIGHT_BRACKET, new Key(']', '}'));
            put(GLFW_KEY_GRAVE_ACCENT,  new Key('`', '~'));
        }};
    }
    
    public TextInputElement(int xOffset, int yOffset, int width, float parentPosX, float parentPosY) {
        super(xOffset, yOffset);
        this.width = width;
        
        setParentPos(parentPosX, parentPosY);
        
        rectBack  = new Rectangle(xOffset, yOffset, width, HEIGHT);
        rectFront = new Rectangle(xOffset, yOffset + 1, width, HEIGHT - 2);
        highlight = new Rectangle(0, 0, 0, HEIGHT - 2);
        timer     = new Timer(1, 18);
        carat     = new Icon(15, 30);
        
        carat.setSubImage(5, 2);
        
        carat.position.set(
                (parentPosX + xOffset) + (lengthToIndex + textOffset) + PADDING, 
                (parentPosY + yOffset) + HEIGHT - 5);
    }
    
    private int getClosest(float value1, float value2, float target) {
        return (int) ((target - value1 >= value2 - target) ? value2 : value1);
    }
    
    private int search(int[] values, float cursorX) {
        int n = values.length;
        
        if(cursorX <= values[0])     return values[0];
        if(cursorX >= values[n - 1]) return values[n - 1];
        
        int i   = 0;
        int j   = n;
        int mid = 0;
        
        while(i < j) {
            mid = (i + j) / 2;
            
            if(values[mid] == cursorX) return values[mid]; 
            
            if(cursorX < values[mid]) {
                if(mid > 0 && cursorX > values[mid - 1]) {
                    return getClosest(values[mid - 1], values[mid], cursorX);
                }
                
                j = mid;
            } else {
                if(mid < n - 1 && cursorX < values[mid + 1]) {
                    return getClosest(values[mid], values[mid + 1], cursorX);
                }
                
                i = mid + 1;
            }
        }
        
        return values[mid];
    }
    
    protected int findClosestIndex(float cursorX) {
        if(typed.length() <= 1) {
            int charWidth = FreeTypeFont.getLengthInPixels(typed.toString(), 1);
            return (cursorX < (charWidth / 2)) ? 0 : 1;
        }
        
        List<Integer> culled = new ArrayList<>();
        
        //Remove numbers that are outside of the carats range
        for(int i = 0; i < typed.length() + 1; i++) {
            int position = FreeTypeFont.getLengthInPixels(typed.substring(0, i), 1) + textOffset;
            
            if(position >= 0 && position < width) {
                culled.add(position);
            }
        }
        
        int[] values = culled.stream().mapToInt(Integer::intValue).toArray();
        int result   = 0;
        
        for(int i = 0; i < typed.length() + 1; i++) {
            if(FreeTypeFont.getLengthInPixels(typed.substring(0, i), 1) + textOffset == search(values, cursorX)) {
                result = i;
            }
        }
        
        return result;
    }
    
    protected void insertChar(char c) {
        typed.insert(currIndex, c);
        prevIndex = currIndex; //TODO: move this into method
        currIndex++;
        scroll();
    }
    
    public void scroll() {
        lengthToIndex = FreeTypeFont.getLengthInPixels(typed.substring(0, currIndex), 1);
        
        int result = (int) ((width - PADDING) - (lengthToIndex + textPos.x - (parentPosX + xOffset + PADDING)));
        
        if(prevIndex < currIndex) {
            if(carat.position.x >= (parentPosX + xOffset + width) - (PADDING * 3)) {
                textOffset = result - PADDING;
                if(textOffset > 0) textOffset = 0;
            }
        } else {
            if(carat.position.x <= parentPosX + xOffset + (PADDING * 3)) {
                textOffset = result - width + PADDING;
            }
        }
        
        carat.position.set(
                (parentPosX + xOffset) + (lengthToIndex + textOffset) + PADDING, 
                (parentPosY + yOffset) + HEIGHT - 5);
    }
    
    protected void focus() {
        hasFocus = true;
        UI.setTextInputElement(this);
        timer.start();
    }
    
    protected void unfocus() {
        hasFocus = false;
        
        if(UI.getTextInputElement() != null && UI.getTextInputElement().equals(this)) {
            UI.setTextInputElement(null);
        }
        
        validateInput();
    }
    
    protected void setIndex(int index) {
        prevIndex = currIndex;
        currIndex = index;
    }
    
    protected final void setParentPos(float parentPosX, float parentPosY) {
        this.parentPosX = parentPosX;
        this.parentPosY = parentPosY;
    }
    
    public void setText(String text) {
        typed.setLength(0);
        currIndex = 0;
        
        for(char c : text.toCharArray()) insertChar(c);
    }
    
    protected int getIndex()         { return currIndex; }
    protected int getTextOffset()    { return textOffset; }
    protected int getLengthToIndex() { return lengthToIndex; }
    public boolean hasFocus()        { return hasFocus; }
    public String getText()          { return typed.toString(); }
    
    protected abstract void validateInput();
    
    public abstract void processInput(int key, int action);
    
}