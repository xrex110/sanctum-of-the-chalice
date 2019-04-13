package procedural;

import game.*;

public class LevelMap {
	public int numRooms;
	public boolean linear;
	public int minRoomSize;
	public int maxRoomSize;
	public int minSpawnSize;
	public int maxSpawnSize;
	public String tileSpriteSheet;
	public int wallTileCode;
	public int floorTileCode;

	//Chests now:
	public double chestSpawnChance;	//per room
	public double chestSpawnPenalty;	//Amount deducted per chest spawned in current room
	


}
