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
public class NPC extends EnemyObject implements Serializable, Interactable {
        public NPC(int x, int y){
        super(x,y);
		actionCool = 1;
		aggroRange = 6;
		awakenRange=2;
		stat.setObjectType(0);

    }

    public NPC(int x, int y, STATE st) {
		super(x,y,st);
		actionCool = 1;
		aggroRange = 6;
		awakenRange=2;
		stat.setObjectType(0);

    }

	public GameObject cloneTo(int x, int y) {
		return new NPC(x, y, state);
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
				cooldown=actionCool;
				if (path.size() > 2) {
					return path.get(1);
				}
				return null;
				
			}
			//cooldown--;
		}
		else {
			state = STATE.AWAKE;
		}
	}
	cooldown--;
	return null;
    }

	public void draw(Graphics2D rend) {
		rend.drawImage(sp.getSprite("regalWizard.png",0,32,32),null, getX(), getY());
	}

    public void death() {
		clearPass();

    }

    public boolean interact(GameObject target) {
	if (cooldown <= 0 && target == Player.player) {
		Player.player.stat.setHP(stat.getWis() * 5);
		return true;
	}
	return false;
    }
}
