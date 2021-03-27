package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.TextInput;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author J Hoffman
 * Created: Mar 26, 2021
 */

public class SpinBox extends TextInput {

    private final Icon leftBorder    = new Icon(15, 30);
    private final Icon middleSection = new Icon(15, 30);
    private final Icon rightBorder   = new Icon(15, 30);
    private final Icon upArrow       = new Icon(20, 20);
    private final Icon downArrow     = new Icon(20, 20);
    
    public SpinBox(float xOffset, float yOffset, float width, float parentPosX, float parentPosY) {
        super(xOffset, yOffset, width, parentPosX, parentPosY);
        
        leftBorder.setSubImage(2, 2);
        leftBorder.setColor(Color.WHITE);
        
        rightBorder.setSubImage(3, 2);
        rightBorder.setColor(Color.WHITE);
    }

    @Override
    protected void validate() {
        System.out.println("validate spinbox input");
    }

    @Override
    public void processKeyInput(int key, int action) {
        if(action == GLFW_PRESS || action == GLFW_REPEAT) {
            caratIdle  = false;
            caratBlink = true;
            timer.restart();
            
            keyChars.forEach((k, c) -> {
                if(key == k) {
                    if(highlight.width > 0) {
                        int min = Math.min(firstIndex, lastIndex);
                        int max = Math.max(firstIndex, lastIndex);

                        typed.replace(min, max, "");

                        setIndex(min);
                        scroll();

                        highlight.width = 0;
                    }
                    
                    insertChar(c.getChar(shiftHeld));
                }
            });
            
            switch(key) {
                case GLFW_KEY_BACKSPACE -> {
                    if(getIndex() > 0) {
                        if(highlight.width > 0) {
                            deleteSection();
                        } else {
                            setIndex(getIndex() - 1);
                            typed.deleteCharAt(getIndex());
                            scroll();
                        }
                    } else {
                        if(highlight.width > 0) deleteSection();
                    }
                }
                    
                case GLFW_KEY_RIGHT -> {
                    if(highlight.width > 0) {
                        setIndex(Math.max(firstIndex, lastIndex));
                        scroll();
                        highlight.width = 0;
                    } else {
                        setIndex((getIndex() > typed.length() - 1) ? typed.length() : getIndex() + 1);
                    }
                    
                    scroll();
                }
                    
                case GLFW_KEY_LEFT -> {
                    if(highlight.width > 0) {
                        setIndex(Math.min(firstIndex, lastIndex));
                        scroll();
                        highlight.width = 0;
                    } else {
                        setIndex((getIndex() <= 0) ? 0 : getIndex() - 1);
                    }
                    
                    scroll();
                }
                
                case GLFW_KEY_ENTER -> {
                    unfocus();
                }
            }
        } else {
            timer.start();
        }
        
        switch(key) {
            case GLFW_KEY_LEFT_SHIFT, GLFW_KEY_RIGHT_SHIFT -> shiftHeld = action == GLFW_PRESS;
        }
    }

    @Override
    public Command update(Mouse mouse) {
        prevPressed = currPressed;
        currPressed = mouse.clicked;
        
        textPos.set(xOffset + parentPosX + PADDING, 
                    yOffset + parentPosY + 21);
        
        timer.update();
        if(timer.finished()) caratIdle = true;
        if(App.tick(18) && caratIdle) caratBlink = !caratBlink;
        
        if(hovered(mouse.cursorPos)) {
            mouse.setCursorShape(GLFW_IBEAM_CURSOR);
            
            if((prevPressed != currPressed && !prevPressed)) {
                if(!hasFocus()) {
                    focus();
                    prevCursorX = (int) mouse.cursorPos.x;
                }
                
                if(typed.length() > 0) {
                    int newIndex = findClosestIndex(mouse.cursorPos.x - bounds.xPos - PADDING);
                    setIndex(newIndex);
                    scroll();

                    firstIndex      = getIndex();
                    firstIndexSet   = true;
                    highlight.width = 0;
                }
            } else {
                if(mouse.cursorPos.x != prevCursorX) highlightText(mouse.cursorPos.x);
            }
        } else {
            if((prevPressed != currPressed && !prevPressed)) {
                if(hasFocus()) {
                    unfocus();
                    highlight.width = 0;
                }
                
                firstIndexSet = false;
            } else {
                highlightText(mouse.cursorPos.x);
            }
        }
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(bounds, Color.RGME_LIGHT_GRAY, uiProgram);
        background.drawRectangle(front, Color.RGME_DARK_GRAY, uiProgram);
        
        glEnable(GL_SCISSOR_TEST);
        glScissor((int) scissorBox.xPos, (int) scissorBox.yPos, (int) scissorBox.width, (int) scissorBox.height);
            background.drawRectangle(highlight, Color.RGME_BLUE, uiProgram);
            font.drawString(typed.toString(), textPos.x + getTextOffset(), textPos.y, 1, Color.RGME_WHITE, uiProgram);
            if(hasFocus() && caratBlink) carat.render(uiProgram);
        glDisable(GL_SCISSOR_TEST);
        
        leftBorder.render(uiProgram);
        rightBorder.render(uiProgram);
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        
    }

}