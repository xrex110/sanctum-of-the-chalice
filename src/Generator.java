import java.util.Random;

public class Generator {

	public int[][] map;
	private Random rand;

	private class Coordinate {
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

	private class Room {
		private Coordinate origin;
		private int width, height;
		private int perimeter;
		private int area;
		
		private Coordinate[] left;
		private Coordinate[] right;
		private Coordinate[] top;
		private Coordinate[] bottom;

		public Room(Coordinate ul, int width, int height) {
			origin = ul;
			this.width = width;
			this.height = height;
			perimeter = (2 * width) + (2 * height);
			area = width * height;
			left = new Coordinate[height];
			right = new Coordinate[height];
			top = new Coordinate[width];
			bottom = new Coordinate[width];

			for(int i = origin.row + 1, count = 0; i < origin.row + height - 1; i++, count++) {
				//origin.row + 1 ignores the corner blocks
				left[count] = new Coordinate(i, origin.col);
			}
			for(int i = origin.row + 1, count = 0; i < origin.row + height - 1; i++, count++) {
				//origin.row + 1 ignores the corner blocks
				int col = origin.col + width - 1;
				right[count] = new Coordinate(i, col);
			}
			for(int i = origin.col + 1, count = 0; i < origin.col + width - 1; i++, count++) {
				//origin.row + 1 ignores the corner blocks
				top[count] = new Coordinate(i, origin.row);
			}
			for(int i = origin.col + 1, count = 0; i < origin.col + width - 1; i++, count++) {
				//origin.row + 1 ignores the corner blocks
				int row = origin.row + height - 1;
				left[count] = new Coordinate(i, row);
			}
			
		}
	}

	public Generator(long seed) {
		rand = new Random(seed);
		map = new int[30][30];
	}

	public Generator() {
		rand = new Random();
		map = new int[30][30];
	}

	public void generateDungeon() {
		
	}

	public void generateRoom(Coordinate xyBound) {
		//Keep in mind, x axis is modulation in columns (j)
		//And y axis is modulation in rows (i) for any (i)(j) arr
		
		
	}

	public void displayMap() {
		System.out.println("=====Printing World=====");
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[1].length; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("=====Printing  Done=====");
	}

}
