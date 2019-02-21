import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Player extends GameObject {
	 
    SpriteLoader sp = new SpriteLoader();
    static Player player = new Player(0,0);
    public static int TILE_SIZE_X = 32, TILE_SIZE_Y = 32;
    public Player(int x, int y) {
        super(x,y, true);
    }

    public boolean moveUp() {
        setY(getY() - TILE_SIZE_Y);
        return true;
    }
    public boolean moveDown() {
        System.out.println(getY());
        setY(getY() + TILE_SIZE_Y);
        return true;
    }

    public boolean moveLeft() {
        setX(getX() - TILE_SIZE_X);
        return true;
    }
    public boolean moveRight() {
        setX(getX() + TILE_SIZE_X);
        return true;
    }

    public void draw(Graphics2D rend) {
        //rend.fillRect(getX(),getY(), 32, 32);
		sp.cacheImage("wizard.png");
        BufferedImage sprite = sp.getSprite("wizard.png",0,32,32);
        rend.drawImage(sprite, null, getX(), getY());
    }

}
