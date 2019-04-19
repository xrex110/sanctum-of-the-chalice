package object;
import main.*;
import java.io.Serializable;

import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;
public class EnemyObject extends GameObject implements Serializable, Interactable {
    SpriteLoader sp = new SpriteLoader();
    ArrayList<Pair<Integer,Integer>> path;
    FireAnimation animation = new FireAnimation();
    int awakenRange = 4;
    int aggroRange = 7;
    int actionCool = 0;
    public boolean knockBack = true;
    public int cooldown = 0;
        ArrayList<Pair<Integer,Integer>> passiveLocs;

	//stat
	public Stat stat = new Stat(1,4); // enemy type;

    enum STATE { 
	SLEEP,
	AWAKE,
	AGGRO
    }

    public STATE state;

    public EnemyObject(int x, int y){
        super(x,y, false);
	int[][] passiveMap = {{0,1,0},
	    		{1,1,1},
			{0,1,0}};

	    state = STATE.SLEEP;
        animation.setState(Animation.AnimationState.SLEEP);
        path = new ArrayList<Pair<Integer,Integer>>();
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
	//System.out.println("Enemy: " + passiveLocs);

    }

    public EnemyObject(int x, int y, STATE st) {
		this(x,y);
	    state = st;
	    if (st != STATE.SLEEP) {
			animation.setState(Animation.AnimationState.AWAKE);
	    }
    }

    public EnemyObject(int x, int y, STATE st, Stat prevStats) {
		this(x,y);
	    state = st;
	    stat = prevStats;
	    if (st != STATE.SLEEP) {
			animation.setState(Animation.AnimationState.AWAKE);
	    }
    }


	public GameObject cloneTo(int x, int y) {
		return new EnemyObject(x, y, state, stat.copyStats());
	}

	public void moveTo(int x, int y) {
		clearPass();
		setX(x);
		setY(y);
		placePass();
	}

	public void hitStun() {
		cooldown++;
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

    public void draw(Graphics2D rend) {
        //rend.setColor(Color.blue);
        //rend.fillRect(getX(),getY(),Player.TILE_SIZE_X, Player.TILE_SIZE_Y);
        //BufferedImage sprite = sp.getSprite("sign.png",0,32,32);
        
        BufferedImage sprite = animation.getUpdate();
        rend.drawImage(sprite, null, getX(), getY());
    }
    
	public boolean checkBounds(int x, int y) {
		return (y >= 0 && y < GameEngine.levelMap[0].length 
			&& x >= 0 && x < GameEngine.levelMap[0][0].length
			&& GameEngine.levelMap[0][y][x] != null
			&& !GameEngine.levelMap[0][y][x].isSolid()
			&& (GameEngine.levelMap[2][y][x] == null 
				|| GameEngine.levelMap[2][y][x] == this));
	}

    public void setPath(ArrayList<Pair<Integer,Integer>> p) {
	path = p;
    }

    public Pair<Integer,Integer> nextLoc() {
	if (state == STATE.SLEEP) {
		if (path.size() > 1 && path.size() <= awakenRange+1) {
			state = STATE.AWAKE;
            animation.setState(Animation.AnimationState.AWAKE);
		}
	}
	if (state == STATE.AWAKE) {
		if (path.size() > 1 && path.size() <= aggroRange+1) {
			state = STATE.AGGRO;
		}
	}
	if (state == STATE.AGGRO) {
		if (path.size() > 1 && path.size() <= aggroRange+1) {
			if (cooldown <= 0) {
				cooldown = actionCool;
    	    			return path.get(1);
			}
			cooldown--;
		}
		else {
			state = STATE.AWAKE;
		}
	}
	return null;
    }

    public int getCurrRange() {
	if (state == STATE.SLEEP) {
		return awakenRange;
	}
	return aggroRange;
    }

    public void death() {
	clearPass();

    }

    public boolean interact(int currentX, int currentY) {
        return currentX == getX() && currentY == getY();
    }

    public boolean interact() {
        return Player.player.getX() == getX() && Player.player.getY() == getY();
    }

    public boolean interact(GameObject target) {
	if (target == Player.player) {
		//System.out.print("Lit");
		return true;
	}
	return false;
    }
}
