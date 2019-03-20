import java.util.Random;

public class Coordinate {
	public int row, col;
	private static Random rand = new Random();

	public Coordinate(int y, int x) {
		row = y;
		col = x;
	}

	public void setValue(int y, int x) {
		row = y;
		col = x;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;

		if(!(o instanceof Coordinate)) return false;

		Coordinate a = (Coordinate) o;
		return (this.row == a.row && this.col == a.col);
	}

	public static Coordinate generateInBounds(Coordinate rowBounds, Coordinate colBounds) {
		//xBounds and yBounds are just a (min, max) pair for these values

		//General formula for rand in a bound is
		//(max - min + 1) + min. We need to +1 because the upper bound is
		//exclusive in rand.nextInt
		int trow = rand.nextInt((rowBounds.col - rowBounds.row) + 1) + rowBounds.row;
		int tcol = rand.nextInt((colBounds.col - colBounds.row) + 1) + colBounds.row;

		return new Coordinate(trow, tcol);
		//TODO: java.util.concurrent.ThreadLocalRandom might be better
		//but is only available is Java 1.7+
	}	
}
