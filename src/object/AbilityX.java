package object;
import game.CombatSys;
import game.GameEngine;
public class AbilityX extends Ability {
    private static Pair[] deltas = new Pair[]{new Pair(-1,-1),
                                              new Pair(-1,1),
                                              new Pair(1,-1),
                                              new Pair(1,1)}; 

    public AbilityX(int x, int y, GameObject parent) {
        super(x,y,parent, deltas);
    }
}
