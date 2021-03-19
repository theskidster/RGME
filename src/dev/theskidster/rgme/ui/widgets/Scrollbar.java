package dev.theskidster.rgme.ui.widgets;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.graphics.Background;
import dev.theskidster.rgme.graphics.Icon;
import dev.theskidster.rgme.main.Program;
import dev.theskidster.rgme.ui.FreeTypeFont;
import dev.theskidster.rgme.ui.LogicLoop;
import dev.theskidster.rgme.utils.Color;
import dev.theskidster.rgme.utils.Mouse;
import dev.theskidster.rgme.utils.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import org.joml.Vector2f;

/**
 * @author J Hoffman
 * Created: Mar 18, 2021
 */

public class Scrollbar extends Widget implements LogicLoop, PropertyChangeListener {

    private final int MARGIN = 24;
    private final int length;
    
    private final float xOffset;
    private final float yOffset;
    private final float viewportLength;
    private float currTotalContentLength;
    private float prevTotalContentLength;
    private float contentOffset;
    private float prevCursorChange;
    
    public boolean parentHovered;
    
    private final Icon[] icons           = new Icon[4];
    private final Rectangle[] rectangles = new Rectangle[5];
    private final Color[] buttonColors   = new Color[2];
    
    public Scrollbar(float xOffset, float yOffset, int length, float viewportLength) {
        super(0, 0, 24, length + (24 * 2));
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.length  = length;
        
        this.viewportLength = viewportLength;
        
        icons[0] = new Icon(24, 24);
        icons[1] = new Icon(24, 24);
        icons[2] = new Icon(20, 20);
        icons[3] = new Icon(20, 20);
        
        icons[0].setColor(Color.WHITE);
        icons[1].setColor(Color.WHITE);
        icons[2].setColor(Color.RGME_WHITE);
        icons[3].setColor(Color.RGME_WHITE);
        
        icons[0].setSubImage(4, 3);
        icons[1].setSubImage(4, 4);
        icons[2].setSubImage(6, 0);
        icons[3].setSubImage(7, 0);
        
        rectangles[0] = new Rectangle(0, 0, MARGIN, length);
        rectangles[1] = new Rectangle(0, 0, MARGIN - 2, length);
        rectangles[2] = new Rectangle(0, 0, MARGIN - 6, 0);
        rectangles[3] = new Rectangle(0, 0, MARGIN, MARGIN);
        rectangles[4] = new Rectangle(0, 0, MARGIN, MARGIN);
        
        buttonColors[0] = Color.RGME_SLATE_GRAY;
        buttonColors[1] = Color.RGME_SLATE_GRAY;
    }

    @Override
    public Command update(Mouse mouse) {
        float contentScale = currTotalContentLength / viewportLength;
        
        if(contentScale <= 1) {
            rectangles[2].yPos   = bounds.yPos + MARGIN;
            rectangles[2].height = bounds.height - (MARGIN * 2);
            contentOffset        = 0;
        } else {
            rectangles[2].height = length / contentScale;
            
            float change = 0;
            
            if(rectangles[2].contains(mouse.cursorPos) && mouse.clicked && mouse.button.equals("left")) {
                change = mouse.cursorPos.y - prevCursorChange;
                scroll(change, contentScale);
            } else if(parentHovered && mouse.scrolled) {
                change = mouse.scrollValue * -5;
                scroll(change, contentScale);
            } else if(rectangles[3].contains(mouse.cursorPos) && mouse.clicked && mouse.button.equals("left")) {
                scroll(-2, contentScale);
            } else if(rectangles[4].contains(mouse.cursorPos) && mouse.clicked && mouse.button.equals("left")) {
                scroll(2, contentScale);
            }
            
            prevCursorChange = mouse.cursorPos.y;
            
            /*
            Included the hack below just in case the viewport used to 
            dermine the length of the scrollbar is changed to the extant 
            that it reaches out of bounds.
            */
            if(prevTotalContentLength != currTotalContentLength) {
                if((rectangles[2].yPos + rectangles[2].height) > rectangles[0].yPos + length) {
                    rectangles[2].yPos = rectangles[0].yPos;
                    contentOffset      = 0;
                }
            }
        }
        
        updateButtonColor(3, 0, mouse);
        updateButtonColor(4, 1, mouse);
        
        return null;
    }

    @Override
    public void render(Program uiProgram, Background background, FreeTypeFont font) {
        background.drawRectangle(rectangles[0], Color.RGME_LIGHT_GRAY, uiProgram);
        background.drawRectangle(rectangles[1], Color.RGME_DARK_GRAY, uiProgram);
        background.drawRectangle(rectangles[2], Color.RGME_SILVER, uiProgram);
        background.drawRectangle(rectangles[3], buttonColors[0], uiProgram);
        background.drawRectangle(rectangles[4], buttonColors[1], uiProgram);
        
        for(Icon icon : icons) icon.render(uiProgram);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()) {
            case "viewportSize" -> {
                Vector2f size = (Vector2f) evt.getNewValue();
                
                bounds.xPos = xOffset + size.x;
                bounds.yPos = yOffset + size.y;

                icons[0].position.set(bounds.xPos, bounds.yPos + MARGIN);
                icons[1].position.set(bounds.xPos, bounds.yPos + bounds.height);
                icons[2].position.set(bounds.xPos + 2, bounds.yPos + 21);
                icons[3].position.set(bounds.xPos + 2, bounds.yPos + bounds.height - 1);

                rectangles[0].xPos = bounds.xPos;
                rectangles[0].yPos = bounds.yPos + MARGIN;
                rectangles[1].xPos = bounds.xPos + 1;
                rectangles[1].yPos = bounds.yPos + MARGIN;
                rectangles[2].xPos = bounds.xPos + 3;
                rectangles[3].xPos = bounds.xPos;
                rectangles[3].yPos = bounds.yPos;
                rectangles[4].xPos = bounds.xPos;
                rectangles[4].yPos = (int) (bounds.yPos + bounds.height - MARGIN);
            }
        }
    }
    
    private void scroll(float change, float contentScale) {
        boolean minLimitReached = rectangles[2].yPos + change < rectangles[0].yPos;
        boolean maxLimitReached = (rectangles[2].yPos + rectangles[2].height) + change > rectangles[0].yPos + length;

        if(!minLimitReached && !maxLimitReached) {
            float scaleFactor = ((currTotalContentLength / rectangles[2].height) / contentScale);

            rectangles[2].yPos += change;
            contentOffset = (rectangles[2].yPos - rectangles[0].yPos) * (1 + scaleFactor);
        }
    }
    
    private void updateButtonColor(int rectangleID, int colorID, Mouse mouse) {
        if(rectangles[rectangleID].contains(mouse.cursorPos)) {
            buttonColors[colorID] = (mouse.clicked) ? Color.RGME_LIGHT_GRAY : Color.RGME_MEDIUM_GRAY;
        } else {
            buttonColors[colorID] = Color.RGME_DARK_GRAY;
        }
    }

    public void setContentLength(Map<Integer, Float> widgetLengths) {
        prevTotalContentLength = currTotalContentLength;
        currTotalContentLength = 0;
                
        widgetLengths.forEach((index, elementLength) -> {
            currTotalContentLength += elementLength;
        });
    }
    
    public int getContentScrollOffset() {
        return (int) -(contentOffset);
    }
    
}