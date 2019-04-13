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

	//Returns MapCoordinate with random row and col within a range
	//Range: row -> [0, rLimit], col -> [0, cLimit]
	public MapCoordinate getRandomCoordinate(int rLimit, int cLimit) {
		return new MapCoordinate(getRandomNumber(rLimit), getRandomNumber(cLimit));
	}

	//Returns MapCoordinate with random row and col within a range
	//Range: row -> [rMin, rMax], col -> [cMin, cMax]
	public MapCoordinate getRandomCoordinateWithinBounds(int rMin, int rMax, int cMin, int cMax) {
		return new MapCoordinate(getRandomWithinBounds(rMin, rMax), getRandomWithinBounds(cMin, cMax));
	}
}
