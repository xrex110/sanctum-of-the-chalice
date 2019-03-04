import java.util.*;

public class MoveHistory {
	public ArrayList<int[]> history;
	private int maxSize;

	public MoveHistory(int max) {
		maxSize = max;
		history = new ArrayList<int[]>();
	}

	public void push(int y, int x) {
		history.add(new int[2]);
		history.get(0)[0] = y;
		history.get(0)[1] = x;
		if (history.size() > maxSize) {
			history.remove(history.size() - 1);
		}
	}

	public void push(int[] coor) {
		history.add(coor);
		
		if (history.size() > maxSize) {
			history.remove(history.size() - 1);
		}
	}

	public int[] pop()
	{
		return history.remove(0);
	}


}
