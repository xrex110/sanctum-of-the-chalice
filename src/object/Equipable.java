package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Equipable extends UsableItem implements Interactable {
	public enum EquipType {
		HELM,
		ARMOR,
		WEAPON,
		ACCESS
	}
	public EquipType type;

	public Equipable(int x, int y, String n, String img, EquipType eq){
		super(x,y, n, img);
		type = eq;
	}

	public Equipable(int x, int y, String n, String img, int maxDur, EquipType eq){
		super(x, y, n, img, maxDur);
		type = eq;
	}
	
	public Equipable(int x, int y, String n, String img, int maxDur, Stat mod, EquipType eq){
		super(x, y, n, img, maxDur,mod);
		type = eq;
	}

	public GameObject cloneTo(int x, int y) {
		return new Equipable(x, y, name, image, maxDurability, modifier.copyStats(), type);
	}

	public void pickUp() {
		TriggerList trig = (TriggerList)GameEngine.levelMap[1][getY()][getX()];
		trig.triggers.remove(this);
		trig.rendered.remove(this);
	}

	public void use() {
		//Do stuff with player stats
		System.out.println("Using " + name);
		if (GameEngine.equips[type.ordinal()] != null) {
			System.out.println("Current Equip: " + GameEngine.equips[type.ordinal()].name);
			GameEngine.equips[type.ordinal()].unEquip();
			if (GameEngine.equips[type.ordinal()] == this) {
				GameEngine.equips[type.ordinal()] = null;
			}
			else {
				GameEngine.equips[type.ordinal()] = this;
			}
			
		}
		else {
			System.out.println("No current equip");
			GameEngine.equips[type.ordinal()] = this;
			equip();
		}
	}

	public void equip() {
		System.out.println(modifier.getMaxHP());
		Player.player.stat.addStat(modifier);
	}

	public void unEquip() {
		//remove stat boosts;
		Player.player.stat.minusStat(modifier);
	}
}


