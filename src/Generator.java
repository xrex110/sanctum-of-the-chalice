import java.util.Random;

public class Generator {
	private int[][] map;
	private Random rand;

	Coordinate signPos;

	public Generator(long seed) {
		rand = new Random(seed);
		map = new int[30][30];
		signPos = new Coordinate(0, 0);
	}

	public Generator() {
		rand = new Random();
		map = new int[30][30];
	}

	public void generateDungeon() {	
		//Spawn room
		Coordinate pt = new Coordinate(10, 10);
		Room room = generateRoom(pt, generateRoomBounds(5, 9, 30, 30));
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
				//System.out.printf("Putting tile at %d, %d\n", rowNum, i);
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
				//System.out.printf("Putting tile at %d, %d\n", rowNum, i);
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
				//System.out.printf("Putting tile at %d, %d\n", i, colNum);
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
				//System.out.printf("Putting tile at %d, %d\n", i, colNum);
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

		int srow = randWithinBounds(rm.origin.row + 1, rm.origin.row + rm.height - 2);
		int scol = randWithinBounds(rm.origin.col + 1, rm.origin.col + rm.width - 2);
		signPos = new Coordinate(srow, scol);

		System.out.println("Sign Row: " + signPos.row + " Col: " + signPos.col);

		addWalls(rm);
		fillRoom(rm);

		//VERY hacky and temporary fix to opening the new room to a corridor
		//TODO: Fix this please for godssake this needs to be better Oh god
		map[cursor.row][cursor.col] = 1;
	}

	public Room excavateRoom(Direction dir, Coordinate cursor) {
		//roomDim.row is the width of the room
		//roomDim.col is the height of the room
		
		//IMPORTANT: roomDim.row = width of room, roomDim.col = height of room!!!!
		//TODO: Fix the weird shit as described in line 201 LOL
		Coordinate roomDim = new Coordinate(0, 0);	//Magic numbers xD
		int row = 0, col = 0;

		//These variables try to ensure that we aren't generating
		//bigger than we have space for in the given direction
		int maxWidth = 0, maxHeight = 0; 
		
		//Pivot for new room in bottom left here
		if(dir == Direction.UP) {
			maxWidth = 30;					//The max possible width
			//Max possible height. This is cursor.row + 1 because the cursor moves one
			//space to the up, left, right, or down before being passed in
			maxHeight = cursor.row + 1;
			roomDim = generateRoomBounds(5, 9, maxWidth, maxHeight);
			int offset = randWithinBounds(1, roomDim.row - 2);		
			col = ensureRange(cursor.col - offset, 0, 29);
			row = ensureRange(cursor.row - roomDim.col + 1, 0, 29);
		}
		else if(dir == Direction.DOWN) {
			maxWidth = 30;
			maxHeight = 30 - cursor.row;
			roomDim = generateRoomBounds(5, 9, maxWidth, maxHeight);
			int offset = randWithinBounds(1, roomDim.row - 2);		
			col = ensureRange(cursor.col - offset, 0, 29);
			row = ensureRange(cursor.row, 0, 29);
		}

		//Pivot for new room in top left here
		else if(dir == Direction.RIGHT) {
			maxWidth = 30 - cursor.col;
			maxHeight = 30;
			roomDim = generateRoomBounds(5, 9, maxWidth, maxHeight);
			int offset = randWithinBounds(1, roomDim.col - 2);		
			row = ensureRange(cursor.row - offset, 0, 29);
			col = ensureRange(cursor.col, 0, 29);
		}
		else if(dir == Direction.LEFT) {
			maxWidth = cursor.col + 1;
			maxHeight = 30;
			roomDim = generateRoomBounds(5, 9, maxWidth, maxHeight);
			int offset = randWithinBounds(1, roomDim.col - 2);		
			row = ensureRange(cursor.row - offset, 0, 29);
			col = ensureRange(cursor.col - roomDim.row + 1, 0, 29);
		}

		//TODO: bug = if the room gets bounded by the ensureRange func
		//the width and height(roomDim row and col) don't get updated properly
		Coordinate roomOrigin = new Coordinate(row, col);
		
		//We must ensure that the width and height are proper, i.e.
		//If we end up bounding the room at the edges of the map
		//We need adjust the width and height to reflect it

		//roomDim.col = Math.abs(row - cursor.row) + 1;

		Room newRoom = generateRoom(roomOrigin, roomDim);

		return newRoom;
	}

	/*
	 *	What a fucking surprise this doesn't exist in java.math
	 *	It's a function to ensure that the result of a variable
	 *	remains within a certain bound
	 */
	private int ensureRange(int value, int min, int max) {
		//TODO: Bounds check for min < max
		return Math.max(min, Math.min(value, max));
	}

	public void addWalls(Room rm) {
		/*for(int i = rm.origin.row; i < rm.origin.row + rm.height; i++) {
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
		}*/	
		Coordinate[] left = rm.getDirectionWall(Direction.LEFT);
		Coordinate[] right = rm.getDirectionWall(Direction.RIGHT);
		Coordinate[] up = rm.getDirectionWall(Direction.UP);
		Coordinate[] bottom = rm.getDirectionWall(Direction.DOWN);
		Coordinate[] corners = rm.getCorners();

		for(Coordinate pt : left) map[pt.row][pt.col] = 2;
		for(Coordinate pt : right) map[pt.row][pt.col] = 2;
		for(Coordinate pt : up) map[pt.row][pt.col] = 2;
		for(Coordinate pt : bottom) map[pt.row][pt.col] = 2;
		for(Coordinate pt : corners) map[pt.row][pt.col] = 2;
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

	public Coordinate generateRoomBounds(int min, int max, int maxWidth, int maxHeight) {
		int width = ensureRange(randWithinBounds(min, max), -1, maxWidth);
		int height = ensureRange(randWithinBounds(min, max), -1, maxHeight);
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

	public int[][] getMap() {
		return map;
	}

	public int[] getSignCoords() {
		int[] coords = {signPos.row, signPos.col};
		return coords;
	}
}
