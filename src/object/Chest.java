package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;


public class Chest extends GameObject implements Interactable {
	SpriteLoader spLoader = new SpriteLoader();

	public boolean interact() {
		return false;
	}

	public Chest(int x, int y) {
		super(x, y, false);
	}
	public void draw(Graphics2D rend) {
		rend.drawImage(spLoader.getSprite("chest.png", 0, 32, 32), null, getX(), getY()); 
	}
}
