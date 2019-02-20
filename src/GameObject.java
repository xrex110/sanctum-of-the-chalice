
import java.awt.Graphics2D;

public abstract class GameObject {
    private int xPos, yPos;
    
    public GameObject(int x, int y) {
        xPos = x;
        yPos = y;
    }
    
    public int getX() { return xPos; }
    public int getY() { return yPos; }
    public void setX(int x) { xPos = x; }
    public void setY(int y) { yPos = y; }
    public abstract void draw(Graphics2D rend);
}

