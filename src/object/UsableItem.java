package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class UsableItem extends GameObject implements Interactable {
	SpriteLoader sp = new SpriteLoader();
	public int maxDurability;
	public int durability;
	public String name;

	public UsableItem(int x, int y, String n){
		this(x,y, n, 10);

	}

	public UsableItem(int x, int y, String n, int maxDur){
		super(x,y, false);
		putDown();
		name = n;
		maxDurability = maxDur;
		durability = maxDurability;

	}

	public void moveTo(int x, int y) {
		TriggerList trig = (TriggerList)GameEngine.levelMap[1][getY()][getX()];
		trig.triggers.remove(this);
		super(x, y);
		trig = (TriggerList)GameEngine.levelMap[1][getY()][getX()];
		trig.triggers.add(this);

	}

	public void pickUp() {
		trig = (TriggerList)GameEngine.levelMap[1][getY()][getX()];
		trig.triggers.remove(this);

	}

	public void putDown() {
		trig = (TriggerList)GameEngine.levelMap[1][getY()][getX()];
		trig.triggers.add(this);

	}

	public boolean interact(GameObject other) {
		if (other == Player.player) {
			pickUp();
			//add to player inventory
			//
			//
			//
			return true;
		}
		return false;
	}

	public void use() {
		//Do stuff with player stats
		useEffect();

		durability--;
		if (durability <= 0) {
			//remove from inventory and destroy
		}
	}

	public void useEffect() {

	}
}


