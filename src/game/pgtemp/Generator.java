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

	private ArrayList<Room> roomsInLevel;
	private ArrayList<MapCoordinate> occupiedTiles;
	private Room spawnRoom;

	//Map generation fields
	/*int maxNumRooms;
	int minRoomSize;
	int maxRoomSize;
	boolean linear;
	int mapLevel;*/
	int mapSize = 30;

	public Generator() {
		rng = new RandomNumberGenerator();
		map = new int[mapSize][mapSize];
		roomsInLevel = new ArrayList<>();
		occupiedTiles = new ArrayList<>();
	}

	
}
