package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;
public class EnemyObject extends GameObject {
    SpriteLoader sp = new SpriteLoader();
    ArrayList<Pair<Integer,Integer>> path;
    FireAnimation animation = new FireAnimation();
    int awakenRange = 3;
    int aggroRange = 6;

    enum STATE { 
	SLEEP,
	AWAKE,
	AGGRO
    }

    public STATE state;

    public EnemyObject(int x, int y){
        super(x,y, false);
	    state = STATE.SLEEP;
        animation.setState(Animation.AnimationState.SLEEP);
        path = new ArrayList<Pair<Integer,Integer>>();

    }
    public void draw(Graphics2D rend) {
        //rend.setColor(Color.blue);
        //rend.fillRect(getX(),getY(),Player.TILE_SIZE_X, Player.TILE_SIZE_Y);
        //BufferedImage sprite = sp.getSprite("sign.png",0,32,32);
        
        BufferedImage sprite = animation.getUpdate();
        rend.drawImage(sprite, null, getX()-28, getY()-60);
    }
    
    public void setPath(ArrayList<Pair<Integer,Integer>> p) {
	path = p;
    }

    public Pair<Integer,Integer> nextLoc() {
	if (state == STATE.SLEEP) {
		if (path.size() > 0 && path.size() <= awakenRange) {
			state = STATE.AWAKE;
            animation.setState(Animation.AnimationState.AWAKE);
		}
	}
	if (state == STATE.AWAKE) {
		if (path.size() > 0 && path.size() <= aggroRange) {
			state = STATE.AGGRO;
		}
	}
	if (state == STATE.AGGRO) {
		if (path.size() > 0 && path.size() <= aggroRange) {
    	    		return path.get(0);
		}
		else {
			state = STATE.AWAKE;
		}
	}
	return null;
    }

    public boolean interact(int currentX, int currentY) {
        return currentX == getX() && currentY == getY();
    }

    public boolean interact() {
        return Player.player.getX() == getX() && Player.player.getY() == getY();
    }
}
