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

/**
 * @author J Hoffman
 * Created: Mar 2, 2021
 */

public class TextArea extends TextInputElement {

    private final Icon leftBorder;
    private final Icon rightBorder;
    
    public TextArea(int xOffset, int yOffset, int width, int parentPosX, int parentPosY) {
        super(xOffset, yOffset, width, parentPosX, parentPosY);
        
        leftBorder  = new Icon(15, 30);
        leftBorder.setSubImage(2, 2);
        leftBorder.setColor(Color.WHITE);
        
        rightBorder = new Icon(15, 30);
        rightBorder.setSubImage(3, 2);
        rightBorder.setColor(Color.WHITE);
    }

    @Override
    void validateInput() {
        
    }

    @Override
    public void processInput(int key, int action) {
        if(action == GLFW_PRESS || action == GLFW_REPEAT) {
            caratIdle  = false;
            caratBlink = true;
            timer.restart();
            
            keyChars.forEach((k, c) -> {
                if(key == k) insertChar(c.getChar(shiftHeld));
            });
            
            switch(key) {
                case GLFW_KEY_BACKSPACE -> {
                    if(getIndex() > 0) {
                        setIndex(getIndex() - 1);
                        typed.deleteCharAt(getIndex());
                        scroll();
                    }
                }
                    
                case GLFW_KEY_RIGHT -> {
                    setIndex((getIndex() > typed.length() - 1) ? typed.length() : getIndex() + 1);
                    scroll();
                }
                    
                case GLFW_KEY_LEFT -> {
                    setIndex((getIndex() <= 0) ? 0 : getIndex() - 1);
                    scroll();
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
    public void update(int parentPosX, int parentPosY, Mouse mouse) {
        setParentPos(parentPosX, parentPosY);
        
        rectBack.xPos = parentPosX + xOffset;
        rectBack.yPos = parentPosY + yOffset;
        
        rectFront.xPos = parentPosX + xOffset;
        rectFront.yPos = parentPosY + yOffset + 1;
        
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
            
            if(mouse.clicked) {
                if(hasFocus()) {
                    if(typed.length() > 0) {
                        int newIndex = findClosestIndex(mouse.cursorPos.x - (parentPosX + xOffset) - PADDING);
                        setIndex(newIndex);
                        scroll();
                    }
                } else {
                    focus();
                }
            }
        } else {
            if(mouse.clicked && hasFocus()) unfocus();
            hovered = false;
        }
        
        if(hovered) mouse.setCursorShape(GLFW_IBEAM_CURSOR);
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(rectBack, Color.RGME_LIGHT_GRAY, uiProgram);
        background.drawRectangle(rectFront, Color.RGME_DARK_GRAY, uiProgram);
        
        leftBorder.render(uiProgram);
        rightBorder.render(uiProgram);
        
        font.drawString(typed.toString(), textPos.x + getTextOffset(), textPos.y, 1, Color.RGME_WHITE, scissorBox, uiProgram);
        
        if(hasFocus() && caratBlink) carat.render(uiProgram);
    }
    
}