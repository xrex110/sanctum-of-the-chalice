package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;


public class Chest extends Item implements Interactable {
	SpriteLoader spLoader = new SpriteLoader();

	public boolean interact(GameObject target) {
		return false;
	}

	public Chest(int x, int y) {
		super(x, y);
	}
	public void draw(Graphics2D rend) {
		rend.drawImage(spLoader.getSprite("chest.png", 0, 32, 32), null, getX(), getY()); 
	}
}
