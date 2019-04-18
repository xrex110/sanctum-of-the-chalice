package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;
public class Boss extends EnemyObject implements Interactable {

	public int width = 2;
	public Pair<Integer,Integer> corner;
	public Pair<Integer,Integer> lead;

    public Boss(int x, int y){
        super(x,y);
	actionCool = 2;
	corner = new Pair<Integer,Integer>(x,y);
	lead = new Pair<Integer,Integer>(x, y);
	moveTo(x, y);
    }

	public GameObject cloneTo(int x, int y) {
		return new EnemyObject(x, y);
	}

	public void moveTo(int x, int y) {
		clearPass();
		int dy = y - lead.y;
		int dx = x - lead.x;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if (i != lead.y-corner.y || j != lead.x-corner.x) {
					if (i - dy < 0 || i - dy >= width || j - dx < 0 || j - dx >= width) {
						GameEngine.levelMap[2][corner.y+i][corner.x+j] = null;
					}
					GameEngine.levelMap[2][corner.y+i+dy][corner.x+j+dx]=this;
				}
			}
		}
		corner.x += dx;
		corner.y += dy;
		setX(x);
		setY(y);
		placePass();
	}

	public boolean checkBounds(int x, int y) {
		int dy = y - lead.y;
		int dx = x - lead.x;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if (!super.checkBounds(corner.x+j+dx, corner.y+i+dy)) {
					System.out.println("failed bounds");
					return false;
				}
			}
		}
		System.out.println("good bounds");
		return true;
	}


    public void draw(Graphics2D rend) {
        //rend.setColor(Color.blue);
        //rend.fillRect(getX(),getY(),Player.TILE_SIZE_X, Player.TILE_SIZE_Y);
        //BufferedImage sprite = sp.getSprite("sign.png",0,32,32);
        
        BufferedImage sprite = sp.getSprite("evilMirror.png",0,64,64);
        rend.drawImage(sprite, null, corner.x*32, corner.y*32);
    }
    
    public void setPath(ArrayList<Pair<Integer,Integer>> p) {
	    	if (path == null) {
			System.out.println("\nCorner: " + corner + "\nLead: " + lead);
			path = p;
			lead = path.get(0);
		}
    }

    public Pair<Integer,Integer> nextLoc() {
	 	if (path != null) {
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
						Pair<Integer,Integer> loc = path.get(1);
						path = null;
    	    			return loc;
					}
					cooldown--;
				}
				else {
				state = STATE.AWAKE;
				}
			}
			path = null;
	    }
		return null;
    }

    public void death() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				GameEngine.levelMap[2][corner.y+i][corner.x+j] = null;
			}
		}
		clearPass();

    }

    public boolean interact(GameObject target) {
	if (target == Player.player) {
		System.out.print("Bossem");
		return true;
	}
	return false;
    }
}
