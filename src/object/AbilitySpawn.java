package object;

import game.GameEngine;

public class AbilitySpawn extends Ability {
    private static Pair[] deltas = new Pair[]{new Pair(0,0),
                                              new Pair(0,-1),
                                              new Pair(0,1),
                                              new Pair(1,0),
    										  new Pair(-1,0)}; 
    EnemyObject minion;
	
	public AbilitySpawn(int x, int y, GameObject parent) {
        super(x,y,parent,deltas);
		minion = new EnemyObject(-1,-1,EnemyObject.STATE.AWAKE);
		minion.stat.setExp(0);
    }

    public void check() {
		for (int i = 0; i < deltas.length; i++) {
			int dx = x + (int)deltas[i].x;
			int dy = y + (int)deltas[i].y;
			if (minion.checkBounds(dx, dy)) {
				GameEngine.levelMap[2][dy][dx] = minion.cloneTo(dx, dy);
				return;
			}
		}
	}
    

}
