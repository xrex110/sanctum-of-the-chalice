import java.awt.image.BufferedImage;
public class FireAnimation extends Animation {
    
    public FireAnimation() {
        super("11_fire_spritesheet.png",100,100);
    }

    public BufferedImage getFrame() {
        int f = (getFrameCount() / 2)%MAX_FRAME;
        
        if(f == 47 || f == 55 || f == 63) f += 1; 
        if(getState() != Animation.AnimationState.SLEEP)
            return sprites.getSprite(getName(), f%MAX_FRAME, 100, 100);
        return sprites.getSprite(getName(), 47, 100, 100);
    }

}
