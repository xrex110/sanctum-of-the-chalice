package object;
import main.*;

import game.*;
import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;


import render.SpriteLoader;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends GameObject implements Interactable {

	transient SpriteLoader sp = new SpriteLoader();
	public static Player player = new Player(0,0);
	public static int TILE_SIZE_X = 32, TILE_SIZE_Y = 32;
	ArrayList<Pair<Integer,Integer>> passiveLocs;
	public Stat stat;


	public Player(int x, int y,int event) {
		super(x,y, true);
		int[][] passiveMap = {{0,0,1,0,0},
			{0,1,1,1,0},
			{1,1,1,1,1},
			{0,1,1,1,0},
			{0,0,1,0,0}};
		 stat = new Stat(0,event);


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
		if (x >= 0 && y >= 0) {
			placePass();
		}

	}
	public Player(int x, int y) {
		super(x,y, true);
		int[][] passiveMap = {{0,0,1,0,0},
			{0,1,1,1,0},
			{1,1,1,1,1},
			{0,1,1,1,0},
			{0,0,1,0,0}};
		//stat = new Stat(0,event);


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
		if (x >= 0 && y >= 0) {
			placePass();
		}

	}

	public GameObject cloneTo(int x, int y) {
		return new Player(x, y);
	}
	
	public boolean moveUp() {
		setY(getY() - TILE_SIZE_Y);
		return true;
	}
	public boolean moveDown() {
		System.out.println(getY());
		setY(getY() + TILE_SIZE_Y);
		return true;
	}

	public boolean moveLeft() {
		setX(getX() - TILE_SIZE_X);
		return true;
	}
	public boolean moveRight() {
		setX(getX() + TILE_SIZE_X);
		return true;
	}
	public void draw(Graphics2D rend) {
		draw(rend, getX(), getY());
	}
	public void draw(Graphics2D rend, int copyX, int copyY) {
		//rend.fillRect(getX(),getY(), 32, 32);
		sp.cacheImage("wizard.png");
		BufferedImage sprite = sp.getSprite("player1.png",0,32,32);
		rend.drawImage(sprite, null, copyX, copyY);
	}
	public int getStr() {
		//TODO: Implement
		return 10;
	}
	public int getDex() {
		//TODO: Implement
		return 11;
	}
	public int getCon() {
		//TODO: Implement
		return 13;
	}
	public int getInt() {
		//TODO: Implement
		return 15;
	}
	public int getDef() {
		//TODO: Implement
		return 20;
	}
	public int getXP() {
		//TODO: Implement
		return 1000;
	}
	public int getLevel() {
		//TODO: Implement
		//Level is a difficulty scalar
		return 10;
	}
	public int getXPDelta() {
		//TODO: Implement
		//Returns xp until next level up
		return 293;
	}
	public int getFreePoints() {
		//TODO: Implement
		//Used to increase DEX/STR etcetc
		return 2;
	}
	public int getGold() {
		//TODO: Implement
		//Returns the amount of gold the player has
		return 1337;
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

	public boolean interact(GameObject target) {
		if (target != Player.player) {
			System.out.print("Played");
			return true;
		}
		return false;
	}

}

/*
<<<<<<< HEAD
		}
	}
=======
public class Player extends GameObject {

    transient SpriteLoader sp = new SpriteLoader();
    public static Player player = new Player(0,0);
    public static int TILE_SIZE_X = 32, TILE_SIZE_Y = 32;
	public Stat stat = new Stat(0);

    public Player(int x, int y) {
        super(x,y, true);
    }

    public boolean moveUp() {
        setY(getY() - TILE_SIZE_Y);
        return true;
    }
    public boolean moveDown() {
        System.out.println(getY());
        setY(getY() + TILE_SIZE_Y);
        return true;
    }

    public boolean moveLeft() {
        setX(getX() - TILE_SIZE_X);
        return true;
    }
    public boolean moveRight() {
        setX(getX() + TILE_SIZE_X);
        return true;
    }
    public void draw(Graphics2D rend) {
        draw(rend, getX(), getY());
    }
    public void draw(Graphics2D rend, int copyX, int copyY) {
        //rend.fillRect(getX(),getY(), 32, 32);
        sp.cacheImage("wizard.png");
        BufferedImage sprite = sp.getSprite("wizard.png",0,32,32);
        rend.drawImage(sprite, null, copyX, copyY);
    }
    public int getStr() {
        //TODO: Implement
        return 10;
    }
    public int getDex() {
        //TODO: Implement
        return 11;
    }
        public int getCon() {
        //TODO: Implement
        return 13;
    }
    public int getInt() {
        //TODO: Implement
        return 15;
    }
    public int getDef() {
        //TODO: Implement
        return 20;
    }
    public int getXP() {
        //TODO: Implement
        return 1000;
    }
    public int getLevel() {
        //TODO: Implement
        //Level is a difficulty scalar
        return 10;
    }
    public int getXPDelta() {
        //TODO: Implement
        //Returns xp until next level up
        return 293;
    }
    public int getFreePoints() {
        //TODO: Implement
        //Used to increase DEX/STR etcetc
        return 2;
    }
    public int getGold() {
        //TODO: Implement
        //Returns the amount of gold the player has
        return 1337;
    }
>>>>>>> combat
*/
