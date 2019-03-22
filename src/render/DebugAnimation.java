package render;
import main.*;

import game.*;
import sound.*;
import object.*;


import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
public class DebugAnimation extends Animation {

    public DebugAnimation(String s, int x, int y) {
        super(s,x,y);
    }

    @Override
    public BufferedImage getFrame() {
        
        switch(getState()) {
            case DEFAULT: {
                /* Changes sprite every 30 frames */
                int frame = getFrameCount() / 30 % MAX_FRAME;
                setCurrentFrame(frame);
            }
        }

        return sprites.getSprite(getName(), getCurrentFrame(), getXSize(), getYSize()); 
    }

    public void draw(Graphics2D rend, int x, int y) {
        rend.setColor(Color.white);
        rend.fillRect(x,y,32,32);
        rend.drawImage(getUpdate(), null, x, y);
    }
}
