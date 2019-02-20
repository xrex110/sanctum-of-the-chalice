import java.awt.Graphics2D;

public abstract class GameObject {
    private int xPos, yPos;
	private boolean solid;
    
    public GameObject(int x, int y, boolean solid) {
        xPos = x;
        yPos = y;
		this.solid = solid;
    }
    
    public int getX() { return xPos; }
    public int getY() { return yPos; }
    public void setX(int x) { xPos = x; }
    public void setY(int y) { yPos = y; }
    public abstract void draw(Graphics2D rend);
	public boolean isSolid() {
		return solid;
	}
}
