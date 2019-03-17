import java.util.Random;

public class Generator {
	private int[][] map;
	private Random rand;

	private int sideSize;

	Coordinate signPos;

	public Generator(long seed, int sideSize) {
		rand = new Random(seed);
		this.sideSize = sideSize;
		map = new int[sideSize][sideSize];
		signPos = new Coordinate(0, 0);
	}

	public Generator(int sideSize) {
		rand = new Random();
		this.sideSize = sideSize;
		map = new int[sideSize][sideSize];
	}

	private class Heading {
		public Coordinate pos;
		public Direction dir;

		public Heading(Coordinate pos, Direction dir) {
			this.pos = pos;
			this.dir = dir;
		}
	}

	public void generateDungeon() {	
		//Spawn room
		//Make spawn room inside a square 20 blocks inside the canvas map

		int minSpawn = 9;
		int maxSpawn = 14;

		Coordinate pt = Coordinate.generateInBounds(new Coordinate(0, sideSize - 10), new Coordinate(0, sideSize - 10));
		Room room = generateRoom(pt, generateRoomBounds(minSpawn, maxSpawn, sideSize - pt.col, sideSize - pt.row));
		addWalls(room);
		fillRoom(room);
		int numRooms = 0;
		
		while(numRooms != 5) {
			Direction dir = Direction.values()[rand.nextInt(4)];

			//We check if this direction on this room already has a connection
			if(room.isSideConnected(dir)) continue;		//Eventually, we will want to reroll after 4 continues

			Coordinate[] wall = room.getDirectionWall(dir);

			int indexPick = rand.nextInt(wall.length);
			Coordinate start = wall[indexPick];

			Coordinate corridorBounds = new Coordinate(3, 6);
			int corridorLength = randWithinBounds(corridorBounds.row, corridorBounds.col);

			if(dir == Direction.UP) {
				if(corridorLength >= start.row) {
					System.out.println("Corridor gen exceed");
					continue;
				}
			}
			if(dir == Direction.DOWN) {
				if(corridorLength >= sideSize - start.row) {
					System.out.println("Corridor gen exceed");
					continue;
				}
			}
			if(dir == Direction.LEFT) {
				if(corridorLength >= start.col) {
					System.out.println("Corridor gen exceed");
					continue;
				}
			}
			if(dir == Direction.RIGHT) {
				if(corridorLength >= sideSize - start.col) {
					System.out.println("Corridor gen exceed");
					continue;
				}
			}

			Heading head = startDigging(new Heading(start, dir), corridorLength);
			room.setConnectedSide(dir);

			Room rm = excavateRoom(head);
			addWalls(rm);
			fillRoom(rm);

			//Set connected side for new room too
			if(dir == Direction.UP) rm.setConnectedSide(Direction.DOWN);
			else if(dir == Direction.DOWN) rm.setConnectedSide(Direction.UP); 
			else if(dir == Direction.LEFT) rm.setConnectedSide(Direction.RIGHT); 
			else if(dir == Direction.RIGHT) rm.setConnectedSide(Direction.LEFT); 

			//Experimental line
			room = rm;

			//VERY hacky and temporary fix to opening the new room to a corridor
			map[head.pos.row][head.pos.col] = 1;
			numRooms++;
		}

	/*	int srow = randWithinBounds(rm.origin.row + 1, rm.origin.row + rm.height - 2);
		int scol = randWithinBounds(rm.origin.col + 1, rm.origin.col + rm.width - 2);
		signPos = new Coordinate(srow, scol);

		System.out.println("Sign Row: " + signPos.row + " Col: " + signPos.col);*/
		displayMap();
	}

	public Heading startDigging(Heading head, int corridorLength) {
		Coordinate start = head.pos;
		Direction dir = head.dir;
	
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
		return new Heading(cursor, dir);
	}

	public Room excavateRoom(Heading heading) {
		Coordinate cursor = heading.pos;
		Direction dir = heading.dir;
		
		//IMPORTANT: roomDim.row = width of room, roomDim.col = height of room!!!!
		Coordinate roomDim = new Coordinate(0, 0);	//Initialization numbers
		int row = 0, col = 0;

		//These variables try to ensure that we aren't generating
		//bigger than we have space for in the given direction
		int maxWidth = 0, maxHeight = 0; 

		int roomMin = 7;
		int roomMax = 12;
		
		//We determine the maximum possible width and height for the room from the cursor spot
		//Then, we choose a pivot based on direction, and we generate the width and height
		//Then we shift the room around until the pivot matches the block we want to open at
		//And finally, we get a col and row for the actual origin of the room

		//Pivot for new room in bottom left here
		if(dir == Direction.UP) {
			maxWidth = sideSize - cursor.col;					//The max possible width
			//Max possible height. This is cursor.row + 1 because the cursor moves one
			//space to the up, left, right, or down before being passed in
			maxHeight = cursor.row + 1;
			roomDim = generateRoomBounds(roomMin, roomMax, maxWidth, maxHeight);
			int offset = randWithinBounds(1, roomDim.row - 2);		
			col = ensureRange(cursor.col - offset, 0, sideSize);
			row = ensureRange(cursor.row - roomDim.col + 1, 0, sideSize);
		}
		else if(dir == Direction.DOWN) {
			maxWidth = sideSize - cursor.col;
			maxHeight = sideSize - cursor.row;
			roomDim = generateRoomBounds(roomMin, roomMax, maxWidth, maxHeight);
			int offset = randWithinBounds(1, roomDim.row - 2);		
			col = ensureRange(cursor.col - offset, 0, sideSize);
			row = ensureRange(cursor.row, 0, sideSize);
		}

		//Pivot for new room in top left here
		else if(dir == Direction.RIGHT) {
			maxWidth = sideSize - cursor.col;
			maxHeight = sideSize - cursor.row;
			roomDim = generateRoomBounds(roomMin, roomMax, maxWidth, maxHeight);
			int offset = randWithinBounds(1, roomDim.col - 2);		
			row = ensureRange(cursor.row - offset, 0, sideSize);
			col = ensureRange(cursor.col, 0, sideSize);
		}
		else if(dir == Direction.LEFT) {
			maxWidth = cursor.col + 1;
			maxHeight = sideSize - cursor.row;
			roomDim = generateRoomBounds(roomMin, roomMax, maxWidth, maxHeight);
			int offset = randWithinBounds(1, roomDim.col - 2);		
			row = ensureRange(cursor.row - offset, 0, sideSize);
			col = ensureRange(cursor.col - roomDim.row + 1, 0, sideSize);
		}

		Coordinate roomOrigin = new Coordinate(row, col);
		
		//We must ensure that the width and height are proper, i.e.
		//If we end up bounding the room at the edges of the map
		//We need adjust the width and height to reflect it

		Room newRoom = generateRoom(roomOrigin, roomDim);

		return newRoom;
	}

	public Coordinate generateRoomBounds(int min, int max) {
		int width = randWithinBounds(min, max);
		int height = randWithinBounds(min, max);
		return new Coordinate(width, height);
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

	/*
	 *	What a surprise this doesn't exist in java.math
	 *	It's a function to ensure that the result of a variable
	 *	remains within a certain bound
	 */
	private int ensureRange(int value, int min, int max) {
		if(min > max) {
			System.out.println("Ensure Range fail: min is greater than max lol");
			System.exit(1);
		}
		return Math.max(min, Math.min(value, max));
	}

	public void addWalls(Room rm) {
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
	public int[][] getMap() {
		return map;
	}

	public int[] getSignCoords() {
		int[] coords = {signPos.row, signPos.col};
		return coords;
	}
}
