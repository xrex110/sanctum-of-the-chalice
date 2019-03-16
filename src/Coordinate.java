import java.util.Random;

public class Coordinate {
	public int row, col;
	private Random rand;

	public Coordinate(int a, int b) {
		row = a;
		col = b;
		rand = new Random();
	}

	public void setValue(int a, int b) {
		row = a;
		col = b;
	}

	public boolean equals(Coordinate a) {
		return (this.row == a.row && this.col == a.col);
	}

	public void generateInBounds(Coordinate xBounds, Coordinate yBounds) {
		//xBounds and yBounds are just a (min, max) pair for these values

		//General formula for rand in a bound is
		//(max - min + 1) + min. We need to +1 because the upper bound is
		//exclusive in rand.nextInt
		row = rand.nextInt((xBounds.col - xBounds.row) + 1) + xBounds.row;
		col = rand.nextInt((yBounds.col - yBounds.row) + 1) + yBounds.row;

		//TODO: java.util.concurrent.ThreadLocalRandom might be better
		//but is only available is Java 1.7+
	}	
}
