package object;

import java.awt.Graphics2D;
import java.awt.Color;

import game.CombatSys;
import game.GameEngine;
public class Ability implements java.io.Serializable{
    ImageObject display;
    GameObject parent;
    int x,y; 
    Pair[] deltas;
    public Ability(int x, int y, GameObject parent, Pair<Integer, Integer>[] coordinates) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        deltas = coordinates;
	display = new ImageObject(-1,-1,"sparkles.png");
	//display = new ImageObject(-1,-1,"magicVortex.png");
    }
    boolean validate(int deltaX, int deltaY) {
        try {
            GameObject temp = GameEngine.levelMap[2][y+deltaY][x+deltaX];
        }catch(Exception e) { return false; }
        
        return GameEngine.levelMap[2][y+deltaY][x+deltaX] instanceof EnemyObject
            || GameEngine.levelMap[2][y+deltaY][x+deltaX] instanceof Player;
    }
    public void check() {
        GameObject[][] entities = GameEngine.levelMap[2];
        for(Pair<Integer, Integer> p : deltas) {
            if(validate(p.x, p.y)) {
		    	CombatSys.genericCombat(parent, entities[y+p.y][x+p.x]);
			}
	    	int dx = x+p.x;
	    	int dy = y+p.y;
	    	if (dy >= 0 && dy < entities.length && dx >= 0 && dx < entities[0].length) {
				TriggerList trig = (TriggerList)GameEngine.levelMap[1][y+p.y][x+p.x];
		    	ImageObject clnDisp = (ImageObject)display.cloneTo(x+p.x,y+p.y);
		    	trig.rendered.add(clnDisp);
		    	GameEngine.transientRenders.add(clnDisp);
	    	}
        }
    }

    public void check(int x, int y) {
	this.x = x;
	this.y = y;
	check();
    }
    
    public void draw(Graphics2D rend) {
        int x = parent.getX();
        int y = parent.getY();

        rend.setColor(Color.red);
        for(Pair<Integer, Integer> p : deltas) {
            
            rend.drawRect(x+32*p.x, y+32*p.y, 32, 32);

        }
    }

    
}
