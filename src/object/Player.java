package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Player extends GameObject {

    SpriteLoader sp = new SpriteLoader();
    public static Player player = new Player(0,0);
    public static int TILE_SIZE_X = 32, TILE_SIZE_Y = 32;

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




}
