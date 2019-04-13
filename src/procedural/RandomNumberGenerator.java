package procedural;

import java.util.Random;

public class RandomNumberGenerator {
	private Random rand;

	public RandomNumberGenerator(long seed) {
		rand = new Random(seed);
	}
	public RandomNumberGenerator() {
		rand = new Random();
	}

	//Returns random number in range [0, lim] (both inclusive)
	public int getRandomNumber(int lim) {
		return rand.nextInt(lim + 1);
	}

	//Returns random number in range [min, max]
	public int getRandomWithinBounds(int min, int max) {
		return rand.nextInt((max - min) + 1) + min;
	}

	//Returns Coordinate with random row and col within a range
	//Range: row -> [0, rLimit], col -> [0, cLimit]
	public Coordinate getRandomCoordinate(int rLimit, int cLimit) {
		return new Coordinate(getRandomNumber(rLimit), getRandomNumber(cLimit));
	}

	//Returns Coordinate with random row and col within a range
	//Range: row -> [rMin, rMax], col -> [cMin, cMax]
	public Coordinate getRandomCoordinateWithinBounds(int rMin, int rMax, int cMin, int cMax) {
		return new Coordinate(getRandomWithinBounds(rMin, rMax), getRandomWithinBounds(cMin, cMax));
	}

	//This function picks a random valid location inside Room rm and returns that coordinate
	public Coordinate getRandomRoomTile(Room rm) {
		int iRow = getRandomWithinBounds(rm.row + 2, rm.row + rm.bounds.height - 2);
		int iCol = getRandomWithinBounds(rm.col + 2, rm.col + rm.bounds.width - 2);
		Coordinate ret = new Coordinate(iRow, iCol);
		return ret;
	}
}
