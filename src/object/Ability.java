package object;

import java.awt.Graphics2D;
import java.awt.Color;

import game.CombatSys;
import game.GameEngine;
public class Ability implements java.io.Serializable{

    GameObject parent;
    int x,y; 
    Pair[] deltas;
    public Ability(int x, int y, GameObject parent, Pair<Integer, Integer>[] coordinates) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        deltas = coordinates;
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
            if(validate(p.x, p.y)) CombatSys.genericCombat(parent, entities[y+p.y][x+p.x]);
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
