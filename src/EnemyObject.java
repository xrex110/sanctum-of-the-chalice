import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;
public class EnemyObject extends GameObject {
    SpriteLoader sp = new SpriteLoader();
    ArrayList<Pair<Integer,Integer>> path;
    FireAnimation animation = new FireAnimation();
    public EnemyObject(int x, int y){
        super(x,y, false);
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
	if (path.size() > 0) {
    	    return path.get(0);
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
