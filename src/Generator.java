import java.util.Random;

public class Generator {

	public enum Direction {
		LEFT,
		RIGHT,
		UP,
		DOWN
	}

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
		public Coordinate origin;
		public int width, height;
		public int perimeter;
		public int area;
		
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
			left = new Coordinate[height - 2];	//Subtract 2 for corner block exclusion
			right = new Coordinate[height - 2];
			top = new Coordinate[width - 2];
			bottom = new Coordinate[width - 2];

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
				top[count] = new Coordinate(origin.row, i);
			}
			for(int i = origin.col + 1, count = 0; i < origin.col + width - 1; i++, count++) {
				//origin.row + 1 ignores the corner blocks
				int row = origin.row + height - 1;
				bottom[count] = new Coordinate(row, i);
			}	
		}

		public Coordinate[] getDirectionWall(Direction dir) {
			switch(dir) {
				case LEFT: return left;
				case RIGHT: return right;
				case UP: return top;
				case DOWN: return bottom;
			}
			return null;
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
		Coordinate pt = new Coordinate(10, 10);
		Room room = generateRoom(pt, generateRoomBounds(5, 9));
		addWalls(room);
		fillRoom(room);
		
		Direction dir;
		dir = Direction.values()[rand.nextInt(4)];
		startDigging(room.getDirectionWall(dir), dir);
		displayMap();
	}

	public void startDigging(Coordinate[] wall, Direction dir) {
		int indexPick = rand.nextInt(wall.length);
		int corridorLength = randWithinBounds(3, 7);
		Coordinate start = wall[indexPick];
	
		Coordinate cursor = new Coordinate(0, 0);	//To keep track of where we end up

		System.out.printf("Corridor of length %d generated\n", corridorLength);
		System.out.printf("At coordinate %d, %d\n", start.row, start.col);

		int rowNum = 0, colNum = 0;
		if(dir == Direction.RIGHT) {
			System.out.println("Direction right");
			colNum = start.col + 1;
			rowNum = start.row;
			for(int i = colNum; i < colNum + corridorLength; i++) {
				System.out.printf("Putting tile at %d, %d\n", rowNum, i);
				map[rowNum][i] = 1;
				cursor.setValue(rowNum, i);
				map[rowNum - 1][i] = 2;
				map[rowNum + 1][i] = 2;
			}
			cursor.col++;
		}
		else if(dir == Direction.LEFT) {
			System.out.println("Direction left");
			colNum = start.col - 1;
			rowNum = start.row;
			for(int i = colNum; i > colNum - corridorLength; i--) {
				System.out.printf("Putting tile at %d, %d\n", rowNum, i);
				map[rowNum][i] = 1;
				cursor.setValue(rowNum, i);
				map[rowNum - 1][i] = 2;
				map[rowNum + 1][i] = 2;
			}
			cursor.col--;
		}

		else if(dir == Direction.UP) {
			System.out.println("Direction up");
			colNum = start.col;
			rowNum = start.row - 1;
			for(int i = rowNum; i > rowNum - corridorLength; i--) {
				System.out.printf("Putting tile at %d, %d\n", i, colNum);
				map[i][colNum] = 1;
				cursor.setValue(i, colNum);
				map[i][colNum - 1] = 2;
				map[i][colNum + 1] = 2;
			}
			cursor.row--;
		}
		else if(dir == Direction.DOWN) {
			System.out.println("Direction down");
			colNum = start.col;
			rowNum = start.row + 1;
			for(int i = rowNum; i < rowNum + corridorLength; i++) {
				System.out.printf("Putting tile at %d, %d\n", i, colNum);
				map[i][colNum] = 1;
				cursor.setValue(i, colNum);
				map[i][colNum - 1] = 2;
				map[i][colNum + 1] = 2;
			}
			cursor.row++;
		}

		//Open up the corridor
		//TODO: Remove the coordinate that opens the corridor from the walls of the Room obj
		map[start.row][start.col] = 1;
		Room rm = excavateRoom(dir, cursor);
		//We generate a room at the end of every "DIG". If a room cannot
		//be generated, then we can dead end for now... Might want a better
		//solution for this in the future
		addWalls(rm);
		fillRoom(rm);
	}

	public Room excavateRoom(Direction dir, Coordinate cursor) {
		//roomDim.row is the width of the room
		//roomDim.col is the height of the room
		Coordinate roomDim = generateRoomBounds(5, 9);	//Magic numbers xD
		int row = 0, col = 0;
		if(dir == Direction.UP) {
			int offset = randWithinBounds(1, roomDim.row - 2);		
			col = ensureRange(cursor.col - offset, 0, 29);
			row = ensureRange(cursor.row - roomDim.col + 1, 0, 29);
		}
		else if(dir == Direction.DOWN) {
			int offset = randWithinBounds(1, roomDim.row - 2);		
			col = ensureRange(cursor.col - offset, 0, 29);
			row = ensureRange(cursor.row, 0, 29);
		}
		else if(dir == Direction.RIGHT) {
			int offset = randWithinBounds(1, roomDim.col - 2);		
			row = ensureRange(cursor.row - offset, 0, 29);
			col = ensureRange(cursor.col, 0, 29);
		}
		else if(dir == Direction.LEFT) {
			int offset = randWithinBounds(1, roomDim.col - 2);		
			row = ensureRange(cursor.row - offset, 0, 29);
			col = ensureRange(cursor.col - roomDim.row + 1, 0, 29);
		}

		//TODO: bug = if the room gets bounded by the ensureRange func
		//the width and height(roomDim row and col) don't get updated properly
		Coordinate roomOrigin = new Coordinate(row, col);

		return generateRoom(roomOrigin, roomDim);
	}

	/*
	 *	What a fucking surprise this doesn't exist in java.math
	 *	It's a function to ensure that the result of a variable
	 *	remains within a certain bound
	 */
	private int ensureRange(int value, int min, int max) {
		return Math.max(min, Math.min(value, max));
	}

	public void addWalls(Room rm) {
		for(int i = rm.origin.row; i < rm.origin.row + rm.height; i++) {
			map[i][rm.origin.col] = 2;
		}
		for(int i = rm.origin.row; i < rm.origin.row + rm.height; i++) {
			int col = rm.origin.col + rm.width - 1;
			map[i][col] = 2;
		}
		for(int i = rm.origin.col + 1; i < rm.origin.col + rm.width; i++) {
			map[rm.origin.row][i] = 2;
		}
		for(int i = rm.origin.col; i < rm.origin.col + rm.width; i++) {
			int row = rm.origin.row + rm.height - 1;
			map[row][i] = 2;
		}	
	}

	public void fillRoom(Room rm) {
		for(int i = rm.origin.row + 1; i < rm.origin.row + rm.height - 1; i++) {
			for(int j = rm.origin.col + 1; j < rm.origin.col + rm.width - 1; j++) {
				map[i][j] = 1;
			}
		}
	}

	public int randWithinBounds(int min, int max) {
		return rand.nextInt((max - min) + 1) + min;
	}

	public Coordinate generateRoomBounds(int min, int max) {
		int width = randWithinBounds(min, max);
		int height = randWithinBounds(min, max);
		return new Coordinate(width, height);
	}

	public Room generateRoom(Coordinate atPoint, Coordinate xyBound) {
		//Keep in mind, x axis is modulation in columns (j)
		//And y axis is modulation in rows (i) for any (i)(j) arr
		
		System.out.printf("Room generated with width %d and height %d at (%d row, %d col)\n", xyBound.row, xyBound.col, atPoint.row, atPoint.col);

		Room rm = new Room(atPoint, xyBound.row, xyBound.col);
		return rm;
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
