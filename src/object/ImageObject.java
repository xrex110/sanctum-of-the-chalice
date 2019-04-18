package object;

import game.*;
import render.*;
import object.CoordinateManager;
import render.RenderLoop;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.Serializable;

public class ImageObject extends GameObject implements Serializable{
    public String image;
    SpriteLoader spLoader = new SpriteLoader();


    public ImageObject(int x, int y, String img) {
        super(x,y,false);
	image = img;
    }
	
    public GameObject cloneTo(int x, int y) {
	return new ImageObject(x,y,image);
    }

    public void draw(Graphics2D rend) {
		rend.drawImage(spLoader.getSprite(image, 0, 32, 32), null, getX(), getY()); 
	}
}
