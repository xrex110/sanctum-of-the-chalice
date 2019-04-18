package object;
public class AbilityZ extends Ability {
    private static Pair[] deltas = new Pair[]{new Pair(0,-2),
                                              new Pair(0,-1),
                                              new Pair(0,1),
                                              new Pair(0,2)}; 
    public AbilityZ(int x, int y, GameObject parent) {
        super(x,y,parent,deltas);
    }
    
}
