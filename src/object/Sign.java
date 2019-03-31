package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
public class Sign extends GameObject implements Interactable{
    SpriteLoader sp = new SpriteLoader();
    private String text;
    public Sign(int x, int y, String text){
        super(x,y, false);
        this.text = text;

    }
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
    public boolean interact(int currentX, int currentY) {
        return currentX == getX() && currentY == getY();
    }

    public boolean interact() {
        return Player.player.getX() == getX() && Player.player.getY() == getY();
    }

    public boolean interact(GameObject target) {
	if (target instanceof Player) {
	    GameEngine.signSelected = this;
	    return true;
	}
	return false;
    }
}
