package dev.theskidster.rgme.ui.elements;

import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.App;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.UI;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glScissor;

/**
 * @author J Hoffman
 * Created: Mar 2, 2021
 */

public final class TextArea extends TextInputElement {

    private int parentPosX;
    private int parentPosY;
    
    private final boolean borderVisible;
    
    private final Icon leftBorder;
    private final Icon rightBorder;
    
    public TextArea(int xOffset, int yOffset, int width, float parentPosX, float parentPosY, boolean borderVisible) {
        super(xOffset, yOffset, width, parentPosX, parentPosY);
        
        this.borderVisible = borderVisible;
        
        if(!borderVisible) rectFront.height = 24;
        
        leftBorder  = new Icon(15, 30);
        leftBorder.setSubImage(2, 2);
        leftBorder.setColor(Color.WHITE);
        
        rightBorder = new Icon(15, 30);
        rightBorder.setSubImage(3, 2);
        rightBorder.setColor(Color.WHITE);
    }

    @Override
    protected void validateInput() {
        
    }

    @Override
    public void processInput(int key, int action) {
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
                            int min = Math.min(firstIndex, lastIndex);
                            int max = Math.max(firstIndex, lastIndex);
                            
                            typed.replace(min, max, "");
                            
                            setIndex(min);
                            scroll();
                            
                            highlight.width = 0;
                        } else {
                            setIndex(getIndex() - 1);
                            typed.deleteCharAt(getIndex());
                            scroll();
                        }
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
    public void update(Mouse mouse) {
        setParentPos(parentPosX, parentPosY);
        
        rectBack.xPos = parentPosX + xOffset;
        rectBack.yPos = parentPosY + yOffset;
        
        rectFront.xPos = parentPosX + xOffset;
        rectFront.yPos = parentPosY + yOffset + 1;
        
        highlight.yPos = parentPosY + yOffset + 1;
        
        textPos.set(parentPosX + xOffset + PADDING, 
                    parentPosY + yOffset + 21);
        
        scissorBox.xPos   = parentPosX + xOffset + 1;
        scissorBox.yPos   = Math.abs(UI.getViewHeight() - (parentPosY + yOffset + HEIGHT));
        scissorBox.width  = width;
        scissorBox.height = HEIGHT;
        
        leftBorder.position.set(parentPosX + xOffset, 
                                parentPosY + yOffset + HEIGHT);
        
        rightBorder.position.set(parentPosX + xOffset + (width - 15), 
                                 parentPosY + yOffset + HEIGHT);
        
        timer.update();
        if(timer.finished()) caratIdle = true;
        if(App.tick(18) && caratIdle) caratBlink = !caratBlink;
        
        if(rectFront.contains(mouse.cursorPos)) {
            hovered = true;
            
            if(mouse.clicked && mouse.button.equals("left")) {
                if(hasFocus()) {
                    if(typed.length() > 0) {
                        if(prevCursorX != mouse.cursorPos.x) {
                            if(mouse.cursorPos.x - (parentPosX + xOffset) - PADDING >= width - (PADDING * 3)) {
                                setIndex((getIndex() > typed.length() - 1) ? typed.length() : getIndex() + 1);
                                scroll();
                            }
                            
                            if(mouse.cursorPos.x - (parentPosX + xOffset) - PADDING <= (PADDING * 3)) {
                                setIndex((getIndex() <= 0) ? 0 : getIndex() - 1);
                                scroll();
                            }
                        } else {
                            int newIndex = findClosestIndex(mouse.cursorPos.x - (parentPosX + xOffset) - PADDING);
                            setIndex(newIndex);
                            scroll();
                            
                            if(!firstIndexSet) {
                                firstIndex    = getIndex();
                                firstIndexSet = true;
                            } else {
                                lastIndex = getIndex();

                                int firstIndexPosX = FreeTypeFont.getLengthInPixels(typed.substring(0, firstIndex), 1);
                                int lastIndexPosX  = FreeTypeFont.getLengthInPixels(typed.substring(0, lastIndex), 1);

                                int minX = Math.min(firstIndexPosX, lastIndexPosX);
                                int maxX = Math.max(firstIndexPosX, lastIndexPosX);

                                highlight.xPos  = (minX + (parentPosX + xOffset) + PADDING) + getTextOffset();
                                highlight.width = (maxX - minX);
                            }
                        }
                        
                        prevCursorX = (int) mouse.cursorPos.x;
                    }
                } else {
                    focus();
                    prevCursorX = (int) mouse.cursorPos.x;
                }
            } else {
                firstIndexSet  = false;
            }
        } else {
            if(mouse.clicked && hasFocus()) {
                unfocus();
                highlight.width = 0;
            }
            
            hovered       = false;
            firstIndexSet = false;
        }
        
        if(hovered) mouse.setCursorShape(GLFW_IBEAM_CURSOR);
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        if(borderVisible) background.drawRectangle(rectBack, Color.RGME_LIGHT_GRAY, uiProgram);
        background.drawRectangle(rectFront, Color.RGME_DARK_GRAY, uiProgram);
        
        glEnable(GL_SCISSOR_TEST);
        glScissor((int) scissorBox.xPos, (int) scissorBox.yPos, (int) scissorBox.width, (int) scissorBox.height);
            background.drawRectangle(highlight, Color.RGME_BLUE, uiProgram);
            font.drawString(typed.toString(), textPos.x + getTextOffset(), textPos.y, 1, Color.WHITE, uiProgram);
            if(hasFocus() && caratBlink) carat.render(uiProgram);
        glDisable(GL_SCISSOR_TEST);
        
        if(borderVisible) {
            leftBorder.render(uiProgram);
            rightBorder.render(uiProgram);
        }
    }

    @Override
    public void updatePosX(int parentPosX) {
        this.parentPosX = parentPosX;
    }

    @Override
    public void updatePosY(int parentPosY) {
        this.parentPosY = parentPosY;
    }
    
}