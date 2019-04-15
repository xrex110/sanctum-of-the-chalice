package object;

import game.*;
import object.CoordinateManager;
import render.RenderLoop;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.Serializable;

public abstract class GameObject implements Serializable{
    private CoordinateManager cm;
	private boolean solid;
    
    public GameObject(int x, int y, boolean solid) {
        cm = new CoordinateManager(x,y);
		this.solid = solid;
    }
	
    public abstract GameObject cloneTo(int x, int y);

    public void moveTo(int x, int y) {
	    setX(x);
	    setY(y);
    }

    public int getX() { return cm.getX(); }
    public int getY() { return cm.getY(); }
    public void setX(int x) { cm.setX(x); }
    public void setY(int y) { cm.setY(y); }
    public void drawDebug(Graphics2D rend) {
        rend.setColor(Color.magenta);
        rend.drawRect(getX(), getY(), RenderLoop.tileSizeX, RenderLoop.tileSizeY);
    }
    public abstract void draw(Graphics2D rend);
	public boolean isSolid() {
		return solid;
	}
}
