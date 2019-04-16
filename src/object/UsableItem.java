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
	public String image;
	public Stat modifier;

	public UsableItem(int x, int y, String n, String img){
		this(x,y, n, img, 10);

	}

	public UsableItem(int x, int y, String n, String img, int maxDur){
		super(x,y, false);
		//if x or y are negative, item will not be placed in map triggers on initialization
		if (x >= 0 && y >= 0) {
			putDown();
		}
		name = n;
		image = img;
		maxDurability = maxDur;
		durability = maxDurability;
		modifier = new Stat();
		modifier.zeroStats();
	}
	
	public UsableItem(int x, int y, String n, String img, int maxDur, Stat mod){
		super(x,y, false);
		if (x >= 0 && y >= 0) {
			putDown();
		}
		name = n;
		image = img;
		maxDurability = maxDur;
		durability = maxDurability;
		modifier = mod;
	}

	public GameObject cloneTo(int x, int y) {
		return new UsableItem(x, y, name, image, maxDurability, modifier.copyStats());
	}

	public void moveTo(int x, int y) {
		//TriggerList trig = (TriggerList)GameEngine.levelMap[1][getY()][getX()];
		//trig.triggers.remove(this);
		pickUp();
		//super(x, y);
		setX(x);
		setY(y);
		//trig = (TriggerList)GameEngine.levelMap[1][getY()][getX()];
		//trig.triggers.add(this);
		putDown();

	}

	public void pickUp() {
		TriggerList trig = (TriggerList)GameEngine.levelMap[1][getY()][getX()];
		trig.triggers.remove(this);
		trig.rendered.remove(this);
	}

	public void putDown() {
		TriggerList trig = (TriggerList)GameEngine.levelMap[1][getY()][getX()];
		trig.triggers.add(this);
		trig.rendered.add(this);
	}

	public void draw(Graphics2D rend) {
		rend.drawImage(sp.getSprite(image, 0, 32, 32), null, getX(), getY()); 
	}

	public BufferedImage getBufImage() {
		return sp.getSprite(image, 0, 32,32);
	}

	public boolean interact(GameObject other) {
		if (other == Player.player) {
			
			//add to player inventory
			if (GameEngine.addToInventory(this)) {
				pickUp();
				return true;
			}
		}
		return false;
	}

	public void use() {
		//Do stuff with player stats
		if (useEffect()) {
			durability--;
			if (durability <= 0) {
				//remove from inventory and destroy
			}
		}
	}

	public boolean useEffect() {
		return Player.player.stat.addStat(modifier);
	}
}


