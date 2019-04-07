/*package game;
  import main.*;
  import render.*;
  import object.*;*/
//Uncomment above when required

import java.util.Random;
import java.util.ArrayList;
import java.awt.Rectangle;

public class Generator {
	private int[][] map;

	/////////////////////////////////////////////
	private RandomNumberGenerator rng;
	private Rectangle mapArea;
	private MapCoordinate cursor;
	/////////////////////////////////////////////

	//These variables try to keep track of the topleftmost corner
	//and the bottomrightmost corner of the rooms generated in the
	//java.awt abstract coordinate space
	private int minX, minY, maxX, maxY;
	private int mapWidth, mapHeight;

	private ArrayList<Rectangle> rectanglesInLevel;
	private ArrayList<Room> roomsInLevel;
	private ArrayList<Corridor> corridorsInLevel;
	private ArrayList<MapCoordinate> corridorTiles;
	private ArrayList<MapCoordinate> occupiedTiles;
	private Room spawnRoom;		//spawn gets special designation

	private int numRooms;
	//Map generation fields
	/*int maxNumRooms;
	  int minRoomSize;
	  int maxRoomSize;
	  boolean linear;
	  int mapLevel;*/

	//int mapSize = 50;

	public Generator(int numRooms) {
		rng = new RandomNumberGenerator();
		//map = new int[mapSize][mapSize];
		//mapArea = new Rectangle(0, 0, mapSize, mapSize);
		rectanglesInLevel = new ArrayList<>();
		roomsInLevel = new ArrayList<>();
		corridorsInLevel = new ArrayList<>();
		occupiedTiles = new ArrayList<>();
		corridorTiles = new ArrayList<>();

		//We set these to min max value, since we don't really expect something dramatic
		//like the size of our array exceeding IntMax or Min 
		minX = minY = Integer.MAX_VALUE;
		maxX = maxY = Integer.MIN_VALUE;

		this.numRooms = numRooms;
	}

	public void generateDungeon() {
		//First we generate a spawn room
		long startTime = System.currentTimeMillis();
		generateSpawn(50, 8, 12);

		//A segment is described as an attempt to generate a corridor and room
		//TODO: Better definition for Segment? Something with more versitality
		int numberOfRoomsGenerated = 1;
		while(numberOfRoomsGenerated != numRooms) {
			boolean genResult = generateSegment(selectRandomValidRoom());
			//If its a fail, just continue on
			//If its a success, go on to the next
			if(!genResult) continue;
			log("Segment " + numberOfRoomsGenerated + " generated successfully");
			numberOfRoomsGenerated++;
		}

		adjustMapSize();
		updateMap();
		long endTime = System.currentTimeMillis();

		//Print map!
		printMap();

		termColorRed();
		System.out.println("=========================");
		System.out.println("Time taken to generate level (not counting printing)\n\t\t" + (endTime - startTime) + "ms");
		System.out.println("=========================");
		termClearColor();
		
	}

	//This function used to current values of minX, minY, maxX, and maxY to
	//determine the bestfit size for the map, and loads the variables mapWidth and mapHeight
	//with the bestfit size
	private void adjustMapSize() {
		log(String.format("minX = %d, minY = %d\nmaxX = %d, maxY = %d\n(Note this is x,y. Note row, col\n", minX, minY, maxX, maxY));
		//+1 for the 0 indexing stuff
		mapWidth = maxX - minX + 1;
		mapHeight = maxY - minY + 1;

		//May come of use later, in here in case
		mapArea = new Rectangle(0, 0, mapWidth, mapHeight);

		map = new int[mapHeight][mapWidth];
		//We negate minX and minY and then translate every element by them
		//This so the the topleftmost point is always flush against the edge of the map,
		//at 0,0
		//For Room and corridor, its translate(offsetX, offsetY);
		log(String.format("mapWidth = %d, mapHeight = %d\n", mapWidth, mapHeight));
		for(Room rm : roomsInLevel) rm.translate(-minX, -minY);	
		for(Corridor cor: corridorsInLevel) cor.translate(-minX, -minY);
		//For MapCoordinate, its translate(rowOffset, colOffset);
		for(MapCoordinate mc : corridorTiles) mc.translate(-minY, -minX);

	}

	/*
	 *	PURPOSE: Generate a spawn room within sqrOffset bounds and minSize/maxSize
	 *	PARAMETERS:		maxOffset: Room will be generated between (0, 0) and (maxOffset, maxOffset);
	 *					minSize: minimum side size of spawn room
	 *					maxSize: maximum side size of spawn room
	 */
	private void generateSpawn(int maxOffset, int minSize, int maxSize) {
		//We first get a width and height
		int width = rng.getRandomWithinBounds(minSize, maxSize);
		int height = rng.getRandomWithinBounds(minSize, maxSize);

		//Since the room can only be within the box that is the centermost of size mapSize- 2sqrOffset
		MapCoordinate roomOrigin = rng.getRandomCoordinateWithinBounds(0, maxOffset, 0, maxOffset);
		spawnRoom = new Room(roomOrigin, width, height);
		roomsInLevel.add(spawnRoom);
		addToCollisionList(spawnRoom.bounds);
		log("Spawn room generated at " + roomOrigin + ", with dimensions: (" + width + ", " + height + ")");
	}

	//A segment is classified as a corridor along with a Room
	//TODO: make it so that a segment can just be a corridor + dead end based on some chance
	private boolean generateSegment(Room startRoom) {
		//We select a random, unconnected side of the room
		boolean genSuccess = false;

		while(!genSuccess) {
			Direction dir = null;
			boolean sideFound = false;

			while(!sideFound) {
				//If the room has all sides connected, move on with your life,
				//let parent function decide what to do
				if(startRoom.isAllConnected()) return genSuccess;
				dir = Direction.values()[rng.getRandomNumber(3)];	//Random dir
				if(!startRoom.isSideConnected(dir)) sideFound = true;
			}
			//dir is the heading
			//Now we get the index on the wall we want to start at
			ArrayList<MapCoordinate> wall = startRoom.getDirectionWall(dir);
			int indexPick = rng.getRandomNumber(wall.size() - 1);
			MapCoordinate start = wall.get(indexPick);

			//Now we have the coordinate start and the direction dir, from a wall of startRoom
			//Now we start generating a corridor here
			Corridor newCorridor = generateCorridor(new Heading(start, dir));

			//Now we check whether this collides with somethimg
			if(checkForCollision(newCorridor.bounds)) {
				startRoom.setSideConnected(dir);
				continue;
			}

			//Then we generate a room
			//TODO: have a dice-roll chance for dead ends?
			int minSize = 4;
			int maxSize = 10;
			Room newRoom = generateRoom(dir, minSize, maxSize);

			//Now we check the new room generated for any collisions
			if(checkForCollision(newRoom.bounds)) {
				startRoom.setSideConnected(dir);
				continue;
			}

			//Set the side of the startRoom to connected
			startRoom.setSideConnected(dir);
			//Remove the point where the corridor starts from startRoom
			wall.remove(start);
			//Add that same point to the corridorTiles list
			corridorTiles.add(start);
			//Add the cursor tile(where the newRoom opens) to the corridorTiles
			corridorTiles.add(cursor);

			//Add newCorridor to the relevant lists
			corridorsInLevel.add(newCorridor);
			addToCollisionList(newCorridor.bounds);

			//Do newRoom settings
			newRoom.setSideConnected(dir);
			addToCollisionList(newRoom.bounds);	//Log the bounds of this room into the overlap arr
			newRoom.getDirectionWall(dir).remove(cursor);
			roomsInLevel.add(newRoom);

			//Generation successful!
			genSuccess = true;
		}
		return genSuccess;
	}

	private Room generateRoom(Direction dir, int minSize, int maxSize) {
		int width = rng.getRandomWithinBounds(minSize, maxSize);
		int height = rng.getRandomWithinBounds(minSize, maxSize);
		int row = 0, col = 0;
		int offset = 0;
		//TODO: Overlap checking
		//We generate new rooms by getting a random offset from coordinate start
		//Cursor is a global variable in this class that contains the coordinate that
		//this room must be generated from. Cursor is updated by generateCorridor.
		if(dir == Direction.UP) {
			offset = rng.getRandomWithinBounds(1, width - 2);
			row = cursor.row - height + 1;
			col = cursor.col - offset;
		}
		else if(dir == Direction.DOWN) {
			offset = rng.getRandomWithinBounds(1, width - 2);
			row = cursor.row;
			col = cursor.col - offset;
		}
		else if(dir == Direction.RIGHT) {
			offset = rng.getRandomWithinBounds(1, height - 2);
			row = cursor.row - offset;
			col = cursor.col;
		}
		else if(dir == Direction.LEFT) {
			offset = rng.getRandomWithinBounds(1, height - 2);
			row = cursor.row - offset;
			col = cursor.col - width + 1;
		}

		log("Generated room at " + new MapCoordinate(row, col) + "with width = " + width + ", height = " + height);
		Room newRoom = new Room(new MapCoordinate(row, col), width, height);
		//We do the required settings on this room
		return newRoom;
	}

	private Corridor generateCorridor(Heading heading) {
		//Randomly generate some length and breadth parameters
		int length = rng.getRandomWithinBounds(4, 7); //4 to 7 long
		int breadth = 3;	//Keeping this 3 for now
		int oRow = 0, oCol = 0;
		//cursor hold the coordinate at the end of the corridor, from where
		//we will start generating the next object. 
		String dirVal = "";

		//TODO: overlap detection
		if(heading.direction == Direction.UP) {
			dirVal = "UP";
			MapCoordinate start = new MapCoordinate(heading.position.row - 1, heading.position.col);
			oRow = start.row - length + 1;
			oCol = start.col - 1;
			cursor = new MapCoordinate(start.row - length, start.col);
		}
		else if(heading.direction == Direction.DOWN) {
			dirVal = "DOWN";
			MapCoordinate start = new MapCoordinate(heading.position.row + 1, heading.position.col);
			oRow = start.row;
			oCol = start.col - 1;
			cursor = new MapCoordinate(start.row + length, start.col);
		}
		else if(heading.direction == Direction.RIGHT) {
			dirVal = "RIGHT";
			MapCoordinate start = new MapCoordinate(heading.position.row, heading.position.col + 1);
			oRow = start.row - 1;
			oCol = start.col;
			cursor = new MapCoordinate(start.row, start.col + length);
		}
		else if(heading.direction == Direction.LEFT) {
			dirVal = "LEFT";
			MapCoordinate start = new MapCoordinate(heading.position.row, heading.position.col - 1);
			oRow = start.row - 1;
			oCol = heading.position.col - length;	// + 1
			cursor = new MapCoordinate(start.row, start.col - length);
		}

		Corridor newCor = new Corridor(new MapCoordinate(oRow, oCol), heading.direction, breadth, length);
		log("Corridor generated with origin " + new MapCoordinate(oRow, oCol) + " Length: " + length + " and breadth " + breadth + ", in direction " + dirVal);

		//cursor is the coordinate at the end of the corridor, just outside it.
		log("The cursor for this corridor is at " + cursor);
		return newCor;
	}

	public boolean checkForCollision(Rectangle rect) {
		boolean result = false;
		for(Rectangle rect2 : rectanglesInLevel) {
			if(rect2.intersects(rect)) {
				result = true;
				break;
			}
		}
		return result;
	}

	//This function returns a random valid room
	//where a valid room is defined as a room with atleast one unconnected
	//side
	private Room selectRandomValidRoom() {
		boolean found = false;
		Room selectedRoom = null;
		int r = 0;
		while(!found) {
			r = rng.getRandomNumber(roomsInLevel.size() - 1);
			selectedRoom = roomsInLevel.get(r);	
			if(!selectedRoom.isAllConnected()) found = true;
		}
		return selectedRoom;
	}

	//This function adds the bounds of whatever's passed in into the
	//rectanglesInLevel list. Furthermore, it keeps track of the upperleftmost
	//and bottomrightmost coordinate on the abstract space constructed by the 
	//aforementioned list
	private void addToCollisionList(Rectangle rect) {
		if(rect.x < minX) minX = rect.x;
		if(rect.y < minY) minY = rect.y;
		//Adding -1s here to account for 0 indexed arrays in Java
		if(rect.x + rect.width - 1 > maxX) maxX = rect.x + rect.width - 1;
		if(rect.y + rect.height - 1 > maxY) maxY = rect.y + rect.height - 1;

		rectanglesInLevel.add(rect);
	}

	//This function updates the map[][] object with all the rooms that have been
	//Generated so far
	private void updateMap() {
		for(Room room: roomsInLevel) drawRoom(room);
		for(Corridor cor : corridorsInLevel) drawCorridor(cor);
		for(MapCoordinate coord: corridorTiles) map[coord.row][coord.col] = 1;
	}

	private void drawCorridor(Corridor cor) {
		for(MapCoordinate pt : cor.getFirstWall()) map[pt.row][pt.col] = 2;	
		for(MapCoordinate pt : cor.getSecondWall()) map[pt.row][pt.col] = 2;	

		if(cor.direction == Direction.UP || cor.direction == Direction.DOWN) {
			for(int i = cor.row; i < cor.row + cor.bounds.height; i++) {
				for(int j = cor.col + 1; j < cor.col + cor.bounds.width - 1; j++) {
					map[i][j] = 1;
				}
			}
		}
		else {
			for(int i = cor.row + 1; i < cor.row + cor.bounds.height - 1; i++) {
				for(int j = cor.col; j < cor.col + cor.bounds.width; j++) {
					map[i][j] = 1;
				}
			}
		}
	}

	private void drawRoom(Room rm) {
		//Write out the walls of the room
		for(MapCoordinate pt : rm.getDirectionWall(Direction.LEFT)) map[pt.row][pt.col] = 2;
		for(MapCoordinate pt : rm.getDirectionWall(Direction.RIGHT)) map[pt.row][pt.col] = 2;
		for(MapCoordinate pt : rm.getDirectionWall(Direction.UP)) map[pt.row][pt.col] = 2;
		for(MapCoordinate pt : rm.getDirectionWall(Direction.DOWN)) map[pt.row][pt.col] = 2;
		for(MapCoordinate pt : rm.getCorners()) map[pt.row][pt.col] = 2;
		//for(MapCoordinate pt : corners) map[pt.row][pt.col] = 2;

		//Fill out the inside of a room
		for(int i = rm.row + 1; i < rm.row + rm.bounds.height - 1; i++) {
			for(int j = rm.col + 1; j < rm.col + rm.bounds.width - 1; j++) {
				map[i][j] = 1;
			}
		}
	}

	private void printMap() {
		System.out.println("=====Printing World=====");
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[1].length; j++) {
				if(map[i][j] == 1) termColorGreen();
				else if(map[i][j] == 2) termColorRed();
				else termColorBlack();
				System.out.print(map[i][j] + " ");
				termClearColor();
			}
			System.out.println();
		}
		System.out.println("=====Printing  Done=====");
	}

	private void log(String str) {
		//Can be changed later to write to a ProcGen log file perhaps
		System.out.println(str);
	}

	private void termColorRed() {
		System.out.print("\033[31m");
	}	

	private void termColorGreen() {
		System.out.print("\033[32m");
	}	

	private void termColorBlack() {
		System.out.print("\033[30m");
	}	

	private void termClearColor() {
		System.out.print("\033[0m");
	}	
}
