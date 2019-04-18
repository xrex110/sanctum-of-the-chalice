package object;
public class AbilityY extends Ability {
    private static Pair[] deltas = new Pair[]{new Pair(-1,0),
                                              new Pair(-2,0),
                                              new Pair(1,0),
                                              new Pair(2,0)}; 
    public AbilityY(int x, int y, GameObject parent) {
        super(x,y,parent,deltas);
    }
    
}
