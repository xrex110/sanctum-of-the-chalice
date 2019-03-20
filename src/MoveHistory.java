import java.util.*;

public class MoveHistory {
	public ArrayList<Pair<Integer,Integer>> history;
	public int maxSize;

	public MoveHistory(int max) {
		maxSize = max;
		history = new ArrayList<Pair<Integer,Integer>>();
	}

	public void push(int x, int y) {
		history.add(0, new Pair<Integer,Integer>(x,y));
		//history.get(0)[0] = y;
		//history.get(0)[1] = x;
		if (history.size() > maxSize) {
			history.remove(history.size() - 1);
		}
	}

	public void push(Pair<Integer,Integer> coor) {
		history.add(0, coor);
		
		if (history.size() > maxSize) {
			history.remove(history.size() - 1);
		}
	}

	public Pair<Integer,Integer> pop()
	{
		if (history.size() > 0)
			return history.remove(0);

		return null;
	}
	
	@Override
	public String toString() {
		String str = "";
		str+= "maxSize: " + maxSize + ", size: " + history.size() + "\n";
		for (int i = 0; i < history.size(); i++) {
			str+= history.get(i) + ", ";
		}
		str+= "\n";
		return str;
	}

}
