package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;


public class MusicTrigger extends Item implements Interactable {
	SpriteLoader spLoader = new SpriteLoader();
	boolean used;

	public boolean interact(GameObject target) {
		if (!used && target == Player.player) {
			System.out.println("Phoebus is sad");
			GameEngine.soundEngine.stopAllRequests();
			GameEngine.soundEngine.playLoop("../res/Ghostpocalypse.ogg","boss");
			used = true;
			return true;
		}
		return false;
	}

	public MusicTrigger(int x, int y) {
		super(x, y, new int[][] {{1}});
		used = false;
	}

	public GameObject cloneTo(int x, int y) {
		return new MusicTrigger(x, y);
	}

	public void draw(Graphics2D rend) {
		//rend.drawImage(spLoader.getSprite("chest.png", 0, 32, 32), null, getX(), getY()); 
	}
}