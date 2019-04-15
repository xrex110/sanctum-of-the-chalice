package object;
import main.*;

import java.io.Serializable;
public class Pair<X,Y> implements Serializable{
    public X x;
    public Y y;
    public Pair(X a, Y b) {
        x = a;
        y = b;
    }
    @Override
        public String toString() {
            return "("+x+","+y+")";
        }
}
