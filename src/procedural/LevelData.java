package procedural;

import game.*;
import object.*;

public class LevelData {
	public GameObject[][][] levelMap;
	public int mapWidth;
	public int mapHeight;
	public Coordinate playerSpawnPosition;
	public Coordinate bossSpawnPosition;
	//Spawn position for the music change trigger
	public Coordinate bossRoomPosition;
	public Coordinate npcSpawnPosition;
}
