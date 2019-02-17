import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
public class Player extends GameObject {
    
    static Player player = new Player(0,0);
    public static int TILE_SIZE_X = 32, TILE_SIZE_Y = 32;
    public Player(int x, int y) {
        super(x,y);
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
        rend.fillRect(getX(),getY(), 32, 32);
    }

}
