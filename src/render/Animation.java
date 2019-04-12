package render;
import main.*;

import game.*;
import sound.*;
import object.*;


import java.awt.image.BufferedImage;
import java.io.Serializable;
public abstract class Animation implements Serializable{
    
    public enum AnimationState {
        DEFAULT,
        SLEEP,
        AWAKE,
        AGGRO;
    }

    //Stores all animation sprites in the whole memory bank
    public static SpriteLoader sprites = new SpriteLoader();

    private String name;
    private int xSize, ySize;
    public final int MAX_FRAME; //Stores the highest index of frame for the spriteloader
    private int currentFrame = 0;
    private int frameCount = 0;
    
    private AnimationState state = AnimationState.DEFAULT;

    public Animation(String sheetPath, int xSize, int ySize) {

        MAX_FRAME = sprites.cacheSheet(sheetPath, xSize, ySize);
        
        this.name = sheetPath;
        this.xSize = xSize;
        this.ySize = ySize;

    }
    public void setState(AnimationState state) {
        this.state = state;
    }
    public AnimationState getState() {
        return state;
    }

    public String getName()     { return name;  }
    public int getXSize()       { return xSize; }
    public int getYSize()       { return ySize; }
    public int getFrameCount()  { return frameCount; }
    public int getCurrentFrame(){ return currentFrame; }
    
    public void setCurrentFrame(int a){ currentFrame = a % MAX_FRAME; } 

    /* FOR THE LOVE OF GOD THIS ONLY GETS THE FRAME IMAGE
     * GET UPDATE DOES THE SAME THING BUT INCREMENTS A FRAME COUNTER
     * THIS IS NECESSARY TO MAKE ANIMATIONS WORK BUT I CANNOT
     * MAKE AN ABSTRACT METHOD PRIVATE AHHHHHHHHHHHHHHHH*/
    abstract BufferedImage getFrame();
    
    public BufferedImage getUpdate() {
        BufferedImage f = getFrame();
        frameCount++;
        return f;
    }
    
}
