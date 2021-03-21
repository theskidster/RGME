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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author J Hoffman
 * Created: Mar 21, 2021
 */

public class TextArea extends TextInput implements PropertyChangeListener {

    private float viewportHeight;
    
    private final boolean borderVisible;
    private boolean prevClicked;
    private boolean currClicked;
    
    private final Icon leftBorder  = new Icon(15, 30);
    private final Icon rightBorder = new Icon(15, 30);
    
    public TextArea(float xOffset, float yOffset, float width, float parentPosX, float parentPosY, boolean borderVisible) {
        super(xOffset, yOffset, width, parentPosX, parentPosY);
        
        this.borderVisible = borderVisible;
        
        if(!borderVisible) {
            front.height     = 24;
            highlight.height = 24;
        } else {
            leftBorder.setSubImage(2, 2);
            leftBorder.setColor(Color.WHITE);

            rightBorder.setSubImage(3, 2);
            rightBorder.setColor(Color.WHITE);
        }
    }

    @Override
    protected void validate() {
        
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
        prevClicked = currClicked;
        currClicked = mouse.clicked;
        
        textPos.set(xOffset + parentPosX + PADDING, 
                    yOffset + parentPosY + 21);
        
        timer.update();
        if(timer.finished()) caratIdle = true;
        if(App.tick(18) && caratIdle) caratBlink = !caratBlink;
        
        if(hovered(mouse.cursorPos)) {
            mouse.setCursorShape(GLFW_IBEAM_CURSOR);
            
            if((prevClicked != currClicked && !prevClicked)) {
                if(hasFocus()) {
                    int newIndex = findClosestIndex(mouse.cursorPos.x - bounds.xPos - PADDING);
                    setIndex(newIndex);
                    scroll();
                    
                    firstIndex      = getIndex();
                    firstIndexSet   = true;
                    highlight.width = 0;
                } else {
                    focus();
                    prevCursorX = (int) mouse.cursorPos.x;
                }
            } else {
                if(mouse.cursorPos.x != prevCursorX) highlightText(mouse.cursorPos.x);
            }
        } else {
            if((prevClicked != currClicked && !prevClicked)) {
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
        if(borderVisible) background.drawRectangle(bounds, Color.RGME_LIGHT_GRAY, uiProgram);
        background.drawRectangle(front, Color.RGME_DARK_GRAY, uiProgram);
        
        glEnable(GL_SCISSOR_TEST);
        glScissor((int) scissorBox.xPos, (int) scissorBox.yPos, (int) scissorBox.width, (int) scissorBox.height);
            background.drawRectangle(highlight, Color.RGME_BLUE, uiProgram);
            font.drawString(typed.toString(), textPos.x + getTextOffset(), textPos.y, 1, Color.RGME_WHITE, uiProgram);
            if(hasFocus() && caratBlink) carat.render(uiProgram);
        glDisable(GL_SCISSOR_TEST);
        
        if(borderVisible) {
            leftBorder.render(uiProgram);
            rightBorder.render(uiProgram);
        }
    }

    @Override
    public void relocate(float parentPosX, float parentPosY) {
        this.parentPosX = parentPosX;
        this.parentPosY = parentPosY;
        
        bounds.xPos = xOffset + parentPosX;
        bounds.yPos = yOffset + parentPosY;
        front.xPos  = bounds.xPos + 1;
        front.yPos  = bounds.yPos + 1;
        
        highlight.yPos = front.yPos;
        
        scissorBox.xPos   = front.xPos;
        scissorBox.yPos   = Math.abs(viewportHeight - (bounds.yPos + HEIGHT));
        scissorBox.width  = bounds.width;
        scissorBox.height = HEIGHT;
        
        if(borderVisible) {
            leftBorder.position.set(bounds.xPos, bounds.yPos + HEIGHT);
            rightBorder.position.set(bounds.xPos + (bounds.width - 14), leftBorder.position.y);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()) {
            case "viewportHeight" -> {
                viewportHeight = (Float) evt.getNewValue();
            }
        }
    }

    private void highlightText(float cursorPosX) {
        if(typed.length() > 0 && currClicked && hasFocus()) {
            if(cursorPosX - bounds.xPos - PADDING >= bounds.width - (PADDING * 3)) {
                setIndex((getIndex() > typed.length() - 1) ? typed.length() : getIndex() + 1);
                scroll();
            }

            if(cursorPosX - bounds.xPos - PADDING <= (PADDING * 3)) {
                setIndex((getIndex() <= 0) ? 0 : getIndex() - 1);
                scroll();
            }

            if(!firstIndexSet) {
                firstIndex    = getIndex();
                firstIndexSet = true;
            } else {
                int newIndex = findClosestIndex(cursorPosX - bounds.xPos - PADDING);
                setIndex(newIndex);
                scroll();

                lastIndex = getIndex();

                int firstIndexPosX = FreeTypeFont.getLengthInPixels(typed.substring(0, firstIndex), 1);
                int lastIndexPosX  = FreeTypeFont.getLengthInPixels(typed.substring(0, lastIndex), 1);

                int minX = Math.min(firstIndexPosX, lastIndexPosX);
                int maxX = Math.max(firstIndexPosX, lastIndexPosX);

                highlight.xPos  = (minX + bounds.xPos + PADDING) + getTextOffset();
                highlight.width = (maxX - minX);
            }
        }
    }
    
    public void deleteSection() {
        int min = Math.min(firstIndex, lastIndex);
        int max = Math.max(firstIndex, lastIndex);

        typed.replace(min, max, "");

        setIndex(min);
        scroll();

        highlight.width = 0;
    }
    
}