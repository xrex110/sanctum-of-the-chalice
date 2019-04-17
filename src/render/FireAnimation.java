package render;
import main.*;

import game.*;
import sound.*;
import object.*;


import java.awt.image.BufferedImage;
public class FireAnimation extends Animation {
    
    public FireAnimation() {
        super("angry_fireball.png",32,32);
    }

    public BufferedImage getFrame() {
        int f = (getFrameCount() / 2)%MAX_FRAME;
        
        //if(f == 47 || f == 55 || f == 63) f += 1;
		if(f == 8) f += 1;
        setCurrentFrame(f);
        if(getState() != Animation.AnimationState.SLEEP)
            return sprites.getSprite(getName(), getCurrentFrame(), 32, 32);
        return sprites.getSprite(getName(), 8, 32, 32);
    }

}
