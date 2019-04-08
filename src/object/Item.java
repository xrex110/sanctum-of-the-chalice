package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Item extends GameObject implements Interactable{
	SpriteLoader sp = new SpriteLoader();
	private String text;
	ArrayList<Pair<Integer,Integer>> passiveLocs;

	public Item(int x, int y){
		super(x,y, false);
		int [][] passiveMap = {{0}};

		passiveLocs = new ArrayList<Pair<Integer,Integer>>();
		for ( int i = 0; i < passiveMap.length; i++) {
			for (int j = 0; j < passiveMap[i].length; j++) {
				if (passiveMap[i][j] == 1) {
					int xOff = j - (passiveMap[i].length/2);
					int yOff = i - passiveMap.length/2;
					passiveLocs.add(new Pair<Integer,Integer>(xOff, yOff));
				}
			}
		}
		placePass();

	}

	public Item(int x, int y, int[][] passiveMap){
		super(x,y, false);

		passiveLocs = new ArrayList<Pair<Integer,Integer>>();
		for ( int i = 0; i < passiveMap.length; i++) {
			for (int j = 0; j < passiveMap[i].length; j++) {
				if (passiveMap[i][j] == 1) {
					int xOff = j - (passiveMap[i].length/2);
					int yOff = i - passiveMap.length/2;
					passiveLocs.add(new Pair<Integer,Integer>(xOff, yOff));
				}
			}
		}
		placePass();

	}
	public void moveTo(int x, int y) {
		clearPass();
		setX(x);
		setY(y);
		placePass();
	}

	public void clearPass() { 
		for (int i = 0; i < passiveLocs.size(); i++) {
			int xLoc = passiveLocs.get(i).x + getX();
			int yLoc = passiveLocs.get(i).y + getY();
			if (yLoc >= 0 && yLoc < GameEngine.levelMap[0].length
					&& xLoc >= 0 && xLoc < GameEngine.levelMap[0][0].length) {
				TriggerList trig = (TriggerList)GameEngine.levelMap[1][yLoc][xLoc];
				trig.triggers.remove(this);
					}

		}
	}

	public void placePass() {
		for (int i = 0; i < passiveLocs.size(); i++) {
			int xLoc = passiveLocs.get(i).x + getX();
			int yLoc = passiveLocs.get(i).y + getY();
			if (yLoc >= 0 && yLoc < GameEngine.levelMap[0].length
					&& xLoc >= 0 && xLoc < GameEngine.levelMap[0][0].length) {
				TriggerList trig = (TriggerList)GameEngine.levelMap[1][yLoc][xLoc];
				trig.triggers.add(this);
					}

		}
	}

	/*
	   public void draw(Graphics2D rend) {
//rend.setColor(Color.blue);
//rend.fillRect(getX(),getY(),Player.TILE_SIZE_X, Player.TILE_SIZE_Y);
BufferedImage sprite = sp.getSprite("sign.png",0,32,32);
rend.drawImage(sprite, null, getX(), getY());
	   }
	   */

	public boolean interact(int currentX, int currentY) {
		return currentX == getX() && currentY == getY();
	}

	public boolean interact() {
		return Player.player.getX() == getX() && Player.player.getY() == getY();
	}

	public boolean interact(GameObject target) {

		return false;
	}
}
