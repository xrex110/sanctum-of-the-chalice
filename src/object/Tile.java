package object;
import main.*;



import render.*;
import game.*;
import sound.*;

import java.awt.Graphics2D;

public class Tile extends GameObject{
    private final String spriteIdentifier;
    private final static SpriteLoader spLoader;
    private final int tileID;

    static {
        spLoader = new SpriteLoader();
    }

    public Tile(int x, int y, String spriteIdentifier, int tileID, boolean solid) {
        super(x, y, solid);
        this.spriteIdentifier = spriteIdentifier;
        this.tileID = tileID;
    }
    public void draw(Graphics2D rend) {
        rend.drawImage(spLoader.getSprite(spriteIdentifier, tileID, 32, 32), null, getX(), getY());
    }

}