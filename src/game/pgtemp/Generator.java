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
	private RandomNumberGenerator rng;

	private Rectangle mapArea;

	private ArrayList<Rectangle> rectanglesInLevel;
	private ArrayList<Room> roomsInLevel;
	private ArrayList<Corridor> corridorsInLevel;
	private ArrayList<MapCoordinate> corridorTiles;
	private ArrayList<MapCoordinate> occupiedTiles;
	private Room spawnRoom;

	//Map generation fields
	/*int maxNumRooms;
	int minRoomSize;
	int maxRoomSize;
	boolean linear;
	int mapLevel;*/

	int mapSize = 50;

	public Generator() {
		rng = new RandomNumberGenerator();
		map = new int[mapSize][mapSize];
		mapArea = new Rectangle(0, 0, mapSize, mapSize);
		rectanglesInLevel = new ArrayList<>();
		roomsInLevel = new ArrayList<>();
		corridorsInLevel = new ArrayList<>();
		occupiedTiles = new ArrayList<>();
		corridorTiles = new ArrayList<>();
	}

	public void generateDungeon() {
		//First we generate a spawn room
		generateSpawn(10, 8, 12);
		
		generateSegment(selectRandomValidRoom());

		updateMap();
		printMap();
	}

	/*
	 *	PURPOSE: Generate a spawn room within sqrOffset bounds and minSize/maxSize
	 *	PARAMETERS:		sqrOffset: Room will be generated within a box the size of mapSize - 2sqrOffset
	 *					minSize: minimum side size of spawn room
	 *					maxSize: maximum side size of spawn room
	 */
	private void generateSpawn(int sqrOffset, int minSize, int maxSize) {
		//We first get a width and height
		int width = rng.getRandomWithinBounds(minSize, maxSize);
		int height = rng.getRandomWithinBounds(minSize, maxSize);

		//Since the room can only be within the box that is the centermost of size mapSize- 2sqrOffset
		int rMin = sqrOffset;
		int rMax = mapSize - sqrOffset - height;
		int cMin = sqrOffset;
		int cMax = mapSize - sqrOffset - width;

		MapCoordinate roomOrigin = rng.getRandomCoordinateWithinBounds(rMin, rMax, cMin, cMax);
		spawnRoom = new Room(roomOrigin, width, height);
		roomsInLevel.add(spawnRoom);
		rectanglesInLevel.add(spawnRoom.bounds);
		log("Spawn room generated at " + roomOrigin + ", with dimensions: (" + width + ", " + height + ")");
	}

	//A segment is classified as a corridor along with a Room
	//TODO: make it so that a segment can just be a corridor + dead end based on some chance
	private void generateSegment(Room startRoom) {
		//We select a random, unconnected side of the room
		boolean sideFound = false;
		Direction dir = null;
		while(!sideFound) {
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
		MapCoordinate endOfCorridor = generateCorridor(new Heading(start, dir));
		//Now that we've generated this room, we want to remove the block 
		//that was at coordinate start from the room's wall arrlists
		//We will add this block coord to another list for potential future usage
		wall.remove(start);
		corridorTiles.add(start);
		//Setting this side as connected
		startRoom.setSideConnected(dir);

		//Then we generate a room
		//TODO: have a dice-roll chance for dead ends?
		int minSize = 4;
		int maxSize = 10;
		generateRoom(endOfCorridor, dir, minSize, maxSize);
	}

	private void generateRoom(MapCoordinate start, Direction dir, int minSize, int maxSize) {
		int width = rng.getRandomWithinBounds(minSize, maxSize);
		int height = rng.getRandomWithinBounds(minSize, maxSize);
		int row = 0, col = 0;
		int offset = 0;
		//TODO: Overlap checking
		//We generate new rooms by getting a random offset from coordinate start
		if(dir == Direction.UP) {
			offset = rng.getRandomWithinBounds(1, width - 2);
			row = start.row - height + 1;
			col = start.col - offset;
		}
		else if(dir == Direction.DOWN) {
			offset = rng.getRandomWithinBounds(1, width - 2);
			row = start.row;
			col = start.col - offset;
		}
		else if(dir == Direction.RIGHT) {
			offset = rng.getRandomWithinBounds(1, height - 2);
			row = start.row - offset;
			col = start.col;
		}
		else if(dir == Direction.LEFT) {
			offset = rng.getRandomWithinBounds(1, width - 2);
			row = start.row - offset;
			col = start.col - width + 1;
		}

		Room newRoom = new Room(new MapCoordinate(row, col), width, height);
		//We do the required settings on this room
		newRoom.setSideConnected(dir);
		newRoom.getDirectionWall(dir).remove(start);
		corridorTiles.add(start);

		rectanglesInLevel(newRoom.bounds);	//Log the bounds of this room into the overlap arr

		//And add it to the list of rooms
		roomsInLevel.add(newRoom);
		return;
	}

	private MapCoordinate generateCorridor(Heading heading) {
		//Randomly generate some length and breadth parameters
		int length = rng.getRandomWithinBounds(4, 7); //4 to 7 long
		int breadth = 3;	//Keeping this 3 for now, TODO: randomize
		int oRow = 0, oCol = 0;
		//retVal returns the coordinate at the end of the corridor, from where
		//we will start generating the next object. 
		MapCoordinate retVal = null;
		String dirVal = "";

		//TODO: overlap detection
		if(heading.direction == Direction.UP) {
			dirVal = "UP";
			MapCoordinate start = new MapCoordinate(heading.position.row - 1, heading.position.col);
			oRow = start.row - length + 1;
			oCol = start.col - 1;
			retVal = new MapCoordinate(start.row - length, start.col);
		}
		else if(heading.direction == Direction.DOWN) {
			dirVal = "DOWN";
			MapCoordinate start = new MapCoordinate(heading.position.row + 1, heading.position.col);
			oRow = start.row;
			oCol = start.col - 1;
			retVal = new MapCoordinate(start.row + length, start.col);
		}
		else if(heading.direction == Direction.RIGHT) {
			dirVal = "RIGHT";
			MapCoordinate start = new MapCoordinate(heading.position.row, heading.position.col + 1);
			oRow = start.row - 1;
			oCol = start.col;
			retVal = new MapCoordinate(start.row, start.col + length);
		}
		else if(heading.direction == Direction.LEFT) {
			dirVal = "LEFT";
			MapCoordinate start = new MapCoordinate(heading.position.row, heading.position.col - 1);
			oRow = start.row - 1;
			oCol = heading.position.col - length;	// + 1
			retVal = new MapCoordinate(start.row, start.col - length);
		}

		Corridor gen = new Corridor(new MapCoordinate(oRow, oCol), heading.direction, breadth, length);
		log("Corridor generated with origin " + new MapCoordinate(oRow, oCol) + " Length: " + length + " and breadth " + breadth + ", in direction " + dirVal);

		//Now we add gen to the list of corridors, and then we return the Coordinate at the end
		//of the corridor
		corridorsInLevel.add(gen);

		rectanglesInLevel(gen.bounds);
		//retVal is the coordinate at the end of the corridor, just outside it.
		log("The RetVal for this coordinate is at " + retVal);
		return retVal;
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
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("=====Printing  Done=====");
	}

	private void log(String str) {
		//Can be changed later to write to a ProcGen log file perhaps
		System.out.println(str);
	}
	
}
