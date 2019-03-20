import java.awt.image.BufferedImage;
public class FireAnimation extends Animation {
    
    public FireAnimation() {
        super("11_fire_spritesheet.png",100,100);
    }

    public BufferedImage getFrame() {
        return sprites.getSprite(getName(), (getFrameCount()/2) % MAX_FRAME, 100, 100);
    }

}
