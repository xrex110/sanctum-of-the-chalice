package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sign extends Item implements Interactable{
    SpriteLoader sp = new SpriteLoader();
    private String text;
    
    public Sign(int x, int y, String text){
        super(x,y,new int[][] {{0,1,0},
	    		{1,1,1},
	    		{0,1,0}});
        this.text = text;
	
	
	System.out.println("Enemy: " + passiveLocs);

    }

    public GameObject cloneTo(int x, int y) {
	return new Sign(x, y, text);
    }
/*
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

*/
    public void draw(Graphics2D rend) {
        //rend.setColor(Color.blue);
        //rend.fillRect(getX(),getY(),Player.TILE_SIZE_X, Player.TILE_SIZE_Y);
        BufferedImage sprite = sp.getSprite("sign.png",0,32,32);
        rend.drawImage(sprite, null, getX(), getY());
    }
    public String getText() { return text; }
    public void setText(String text) { 
	synchronized (text) {
		this.text = text;
	}

    }
    /*
    public boolean interact(int currentX, int currentY) {
        return currentX == getX() && currentY == getY();
    }

    public boolean interact() {
        return Player.player.getX() == getX() && Player.player.getY() == getY();
    }*/

    public boolean interact(GameObject target) {
	if (target instanceof Player) {
	    //GameEngine.signSelected = this;
	    return true;
	}
	return false;
    }
}
