package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;


public abstract class GameObject {
    private CoordinateManager cm;
	private boolean solid;
    
    public GameObject(int x, int y, boolean solid) {
        cm = new CoordinateManager(x,y);
		this.solid = solid;
    }
    
    public int getX() { return cm.getX(); }
    public int getY() { return cm.getY(); }
    public void setX(int x) { cm.setX(x); }
    public void setY(int y) { cm.setY(y); }
    public abstract void draw(Graphics2D rend);
	public boolean isSolid() {
		return solid;
	}
}
