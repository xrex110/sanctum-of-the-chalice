package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class UsableItem extends GameObject implements Interactable {
	SpriteLoader sp = new SpriteLoader();
	public int maxDurability;
	public int durability;
	public String name;
	public Stat modifier;

	public UsableItem(int x, int y, String n){
		this(x,y, n, 10);

	}

	public UsableItem(int x, int y, String n, int maxDur){
		super(x,y, false);
		//if x or y are negative, item will not be placed in map triggers on initialization
		if (x >= 0 && y >= 0) {
			putDown();
		}
		name = n;
		maxDurability = maxDur;
		durability = maxDurability;
		modifier = new Stat();
		modifier.zeroStats();
	}
	
	public UsableItem(int x, int y, String n, int maxDur, Stat mod){
		super(x,y, false);
		if (x >= 0 && y >= 0) {
			putDown();
		}
		name = n;
		maxDurability = maxDur;
		durability = maxDurability;
		modifier = mod;
	}

	public GameObject cloneTo(int x, int y) {
		return new UsableItem(x, y, name, maxDurability, modifier.copyStats());
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


