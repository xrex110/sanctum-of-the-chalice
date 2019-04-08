package game;
import main.*;

import render.*;
import sound.*;
import object.*;


import java.util.*;

public class GameEngine {
	public final float FASTRATE = 31.25f;
	public static final float SLOWRATE = 500f;
	private final float MILLITONANO = 1_000_000;
	private final int MAXHISTORY = 30;
	public float currSlowRate;

	public enum MODE {
		GAME,
		PAUSE,
		REVERSION
	}
	public static MODE gameMode;
	public static MODE prevMode;

	private float slowCount;
	private int fastIts;
	private int slowIts;
	private float gameStart;
	private boolean running;
	private String backgroundMusic = "../res/Twisting.ogg";
	//private String enterSound = "../res/Mario.ogg";
	private Sign levelEnd;

	//private Player player;

	private static String currentInput;
	private RenderLoop renderEngine;
	private SoundEngine soundEngine;
	private ScoreTracker tracker;

	private Generator levelGen;
	/*
	   private GameObject[][] levelMap;
	   private GameObject[][] entityMap;
	   */
	//map, row, column; tile, trigger, entity
	public static GameObject[][][] levelMap;
	private ArrayList<EnemyObject> enemyUpdateList;
	private MoveHistory moveHist;

	private EnemyObject theEnemy;

	public static Timer playtime = new Timer();
	public static int mapSize = 100;
	public static Sign signSelected;

	public GameEngine() {
		/*
		   levelMap = new GameObject[30][30];
		   entityMap = new GameObject[30][30];
		   */
		GameEngine.levelMap = new GameObject[3][mapSize][mapSize];

		//entityMap[12][12] = Player.player;
		int numRooms = 20;
		//levelMap = new GameObject[mapSize][mapSize];
		//entityMap = new GameObject[mapSize][mapSize];

		//entityMap[12][12] = Player.player;

		//Recommended that mapSize be 10x number of Rooms
		//last arg is for linearty. Set true for linear levels,
		//false for random radially generated levels
		levelGen = new Generator(mapSize, numRooms, true);
		generateMap();

		for (int i = 0; i < GameEngine.levelMap[1].length; i++) {
			for (int j = 0; j < GameEngine.levelMap[1][i].length; j++) {
				GameEngine.levelMap[1][i][j] = new TriggerList(j, i);
			}
		}

		int[] playPos = levelGen.getSpawnPos();
		Player.player.moveTo(playPos[1]+3, playPos[0]+3);

		//Player.player.setX((playPos[1] + 3));
		//Player.player.setY((playPos[0] + 3));

		//getSignCoordinates generates randomized coordinates for 2 signs,
		//one at spawn, and one at the end of the map, and returns them in a Coordinate array
		//of size 2, with index 0 containing the spawn size coords, and index 1 containing the end sign
		//coordinates
		Coordinate[] signPositions = levelGen.getSignCoords();
		System.out.println("GE Row: " + signPositions[0].row + " GE Col: " + signPositions[0].col);
		String help = "Use the W A S D keys to move around the map!";

		//levelEnd = new Sign(signPos[1] * 32, signPos[0] * 32, "Insert end stats here");
		/*
		   entityMap[signPos[0]][signPos[1]] = levelEnd;
		   entityMap[12][12] = new Sign(12*32, 12*32, help);
		   */
		//levelMap[1][signPos[0]][signPos[1]]=levelEnd;
		//levelMap[1][12][12] = new Sign(12*32, 12*32, help);

		enemyUpdateList = new ArrayList<EnemyObject>();

		theEnemy = new EnemyObject(signPositions[1].col, (signPositions[1].row));

		GameEngine.levelMap[2][signPositions[1].row][signPositions[1].col] = theEnemy;

		moveHist = new MoveHistory(MAXHISTORY);
		levelEnd = new Sign(signPositions[1].col, signPositions[1].row, "Insert end stats here");
		TriggerList signTrig = (TriggerList)levelMap[1][signPositions[1].row][signPositions[1].col];
		signTrig.rendered = levelEnd;
		signTrig.triggers.add(levelEnd);
		Sign intro = new Sign(signPositions[0].col, signPositions[0].row, help);
		signTrig = (TriggerList)levelMap[1][signPositions[0].row][signPositions[0].col];
		signTrig.rendered = intro; 
		signTrig.triggers.add(intro);



		//Populate chests!
		//First arg is the chance to spawn a chest per non spawn room of the level
		//Second arg dictates the penalty that the % chance of chest spawn will suffer
		//per chest after the first one per room
		//and third one is the max number of chests allowed per floor
		//last one is for max number of chests allowed per room
		ArrayList<Coordinate> chestCoords = levelGen.getChestCoordinates(40, 15, 11, 3);
		for(Coordinate coord : chestCoords) {
			Chest ch = new Chest(coord.col, coord.row);
			TriggerList tr = (TriggerList)GameEngine.levelMap[1][coord.row][coord.col];
			tr.rendered = ch;
			tr.triggers.add(ch);

			GameEngine.levelMap[2][coord.row][coord.col] = new EnemyObject(coord.col, coord.row);

		}

		renderEngine = new RenderLoop();
		renderEngine.setName("RenderEngine");

		soundEngine = new SoundEngine();
		tracker = new ScoreTracker();
		//player = new Player(12*32, 12*32);
		gameMode = MODE.PAUSE;
		prevMode = MODE.GAME;
		currSlowRate = SLOWRATE;
		currentInput = "";
	}


	public RenderLoop getRenderEngine() {
		return renderEngine;
	}

	public void generateMap() {
		levelGen.generateDungeon();
		int[][] rawMap = levelGen.getMap();

		for(int i = 0; i < rawMap.length; i++) {
			for(int j = 0; j < rawMap[i].length; j++) {
				int tileType = rawMap[i][j];
				if(tileType == 2) {
					GameEngine.levelMap[0][i][j] = new Tile(j, i, "test_tile.png", 0, true);
				}
				else if(tileType == 1) {
					GameEngine.levelMap[0][i][j] = new Tile(j, i, "test_tile.png", 1, false);
				}
				else if(tileType == 0) {
					GameEngine.levelMap[0][i][j] = null;
				}
			}
		}
	}

	public void startLoop() {
		fastIts = 0;
		slowIts = 0;
		slowCount = 0;
		running = true;
		gameStart = System.nanoTime() / MILLITONANO;
		renderEngine.updateMap(GameEngine.levelMap);
		//renderEngine.updateEntityMap(entityMap);
		renderEngine.start();		//Starts the renderengine thread!

		//soundEngine.play(enterSound, "enter");
		soundEngine.playLoop(backgroundMusic, "background");
		//GameEngine.unPause();
		gameLoop();
	}

	public void gameLoop() {
		float timeStart = 0;
		while (running)
		{
			timeStart += System.nanoTime() / MILLITONANO;

			fastTick();
			if (slowCount >= currSlowRate && gameMode != MODE.PAUSE)
			{
				slowTick();
				slowCount -= currSlowRate;
			}

			float timeElapsed = System.nanoTime() / MILLITONANO - timeStart;
			float timeToNext = FASTRATE - timeElapsed;
			if (gameMode != MODE.PAUSE) {
				slowCount += timeElapsed;
				if (timeToNext > 0)
				{
					sleepForMilli(timeToNext);
					slowCount += timeToNext;
				}
				else {
					System.out.print("lag");
				}
			}
			else {
				sleepForMilli(timeToNext);
			}
			timeStart = timeToNext-(long)timeToNext;
		}
		//getTickRates();
	}


	public void fastTick() {

		fastIts += 1;
		//fillerOperations(100_000);
		/*if(!currentInput.equals("")) {
		  System.out.println("Key is " + currentInput);
		  }*/
	}

	/*This method serves as a benchmark function to see statistics on the performance of the gameloop
	*/
	public void getTickRates() {

		float totTime = System.nanoTime() / MILLITONANO - gameStart;
		System.out.println("Total Time: " + totTime
				+ ", Slow ticks: " + slowIts + ", Fast ticks: " + fastIts);
		System.out.println("Avg ft/s: "+(fastIts/totTime*1000)+", Avg ms/ft: "+(totTime/fastIts) 
				+", Avg st/s: "+(slowIts/totTime*1000)+", Avg ms/st: "+(totTime/slowIts));


	}

	/*This method runs a number of arbitrary operations in order to test the response of the gameloop to
	 * loads that it cannot handle
	 */
	private void fillerOperations(int num) {
		double[] x = new double[num];
		for (int i = 0; i < num; i++)
		{
			x[i] = Math.random();
		}
		Arrays.sort(x);

	}

	public void slowTick() {
		//fillerOperations(100_000);

		//System.out.print(" 1");
		slowIts++;
		signSelected = null;
		//Update player and stuff
		if (currentInput.equals("Q") && moveHist.history.size() > 0) {
			setState(MODE.REVERSION);
		}
		else {
			setState(MODE.GAME);
			currSlowRate = SLOWRATE;
		}

		if (gameMode == MODE.GAME) {
			if(prevMode == MODE.REVERSION) {
				//TODO: Handle revert collision checks
				GameEngine.levelMap[2][Player.player.getY()][Player.player.getX()] = Player.player;
			}
			updatePlayer();

			if (!levelEnd.interact()); {
				levelEnd.setText(("Congratulations! Tutorial Complete\n" +
							"Level Stats:\n" +
							"\tNumber of Up Moves: " + tracker.getUpScore()));
			}

			//System.out.println("Slow tick: "+slowIts+"\n"+moveHist);
			pathAll(10);
			while (enemyUpdateList.size() > 0) {
				//System.out.println("enemy");
				EnemyObject en = enemyUpdateList.remove(0);
				Pair<Integer,Integer> nextLoc = en.nextLoc();
				if (nextLoc != null 
						&& !GameEngine.levelMap[0][nextLoc.y][nextLoc.x].isSolid() 
						&& GameEngine.levelMap[2][nextLoc.y][nextLoc.x] == null) {
					GameEngine.levelMap[2][en.getY()][en.getX()] = null;
					GameEngine.levelMap[2][nextLoc.y][nextLoc.x] = en;
					en.moveTo(nextLoc.x, nextLoc.y);
				
					//en.setX(nextLoc.x);
					//en.setY(nextLoc.y);
						}
				TriggerList trig = (TriggerList)GameEngine.levelMap[1][en.getY()][en.getX()];
				for (int i = 0; i < trig.triggers.size(); i++) {
					trig.triggers.get(i).interact(en);
				}	
			}

		}
		else if (gameMode == MODE.REVERSION) { 
			revert();
		}
		prevMode = gameMode;
		//System.out.println(moveHist);

		//attempt to move all relevant enemies to their desired locations
		//System.out.println(theEnemy.getX()/32 + ", "+ theEnemy.getY()/32);

		//Clear currentInput at end of every slowTick
		currentInput = "";
		/*if (slowIts >= 30)
		  {
		  end();
		  System.out.println();
		  }*/

	}

	private void sleepForMilli(float t) {
		try {
			Thread.sleep((long)t);
		} catch (Exception e)
		{

		}
	}	

	public void end() {
		soundEngine.stopAllRequests();

		while (moveHist.history.size() > 0)
		{
			System.out.print(moveHist.pop() + " ");
		}
		System.out.println("end");

		running = false;
	}

	public void updatePlayer() {
		int xPos = Player.player.getX();
		int yPos = Player.player.getY();
		if(currentInput.equals("W"))
		{
			//Player.player.moveUp();
			yPos--;
		}
		else if (currentInput.equals("A"))
		{
			//Player.player.moveLeft();
			xPos--;
		}
		else if (currentInput.equals("S"))
		{
			//Player.player.moveDown();
			yPos++;
		}
		else if (currentInput.equals("D"))
		{
			//Player.player.moveRight();
			xPos++;
		}


		//System.out.println(xPos + " " + yPos);


		if(GameEngine.levelMap[0][yPos][xPos] != null) {
			if (!GameEngine.levelMap[0][yPos][xPos].isSolid() && GameEngine.levelMap[2][yPos][xPos] == null)
			{
				int[] displace = {yPos - Player.player.getY(), xPos - Player.player.getX()};
				GameEngine.levelMap[2][Player.player.getY()][Player.player.getX()] = null;

				tracker.notify(displace, ScoreTracker.MOVEEVENT);
				Player.player.moveTo(xPos, yPos);
				//Player.player.setX(xPos);
				//Player.player.setY(yPos);
				//System.out.println(xPos + " " + yPos);

				GameEngine.levelMap[2][yPos][xPos] = Player.player;
			}
		}

		//handle triggers
		TriggerList trig = (TriggerList)GameEngine.levelMap[1][Player.player.getY()][Player.player.getX()];
		for (int i = 0; i < trig.triggers.size(); i++) {
			trig.triggers.get(i).interact(Player.player);
		}

		moveHist.push(Player.player.getX() , Player.player.getY());



	}

	public void revert() {
		currSlowRate = SLOWRATE / 2;
		Pair<Integer, Integer> prevPos = moveHist.pop();
		if (prevPos != null) {
			//do stuff
			if(prevMode == MODE.GAME)
				GameEngine.levelMap[2][Player.player.getY()][Player.player.getX()] = null;
			Player.player.moveTo(prevPos.x, prevPos.y);
			//Player.player.setX(prevPos.x);
			//Player.player.setY(prevPos.y);

			//levelMap[2][prevPos.y][prevPos.x] = Player.player;


			return;
		}

		setState(MODE.GAME);
		currSlowRate = SLOWRATE;
	}

	public static void setState(MODE m) {
		if (m != MODE.PAUSE);
		prevMode = gameMode;
		gameMode = m;
	}

	public static boolean setPause() { 
		//returns true if sets to pause
		//returns false if already paused
		if (gameMode != MODE.PAUSE) {
			prevMode = gameMode;
			gameMode = MODE.PAUSE;
			playtime.stop();
			return true;
		}
		return false;
	}

	public static void unPause() {
		playtime.start();
		gameMode = prevMode;
	}
	//TODO Make the maxDist parameter actually matter
	public void pathAll(int maxDist) {
		int height = GameEngine.levelMap[0].length;
		int width = GameEngine.levelMap[0][0].length;
		int[][][] pathMap = new int[height][width][5];
		ArrayList<int[]> queue = new ArrayList<int[]>();
		//initialize all distances to -1
		for (int i = 0; i < pathMap.length; i++) {
			for (int j = 0; j < pathMap[i].length; j++) {
				pathMap[i][j][4] = -1;
			}
		}
		//{currentY, currentX, prevY, prevX, dist}
		int[] current = {Player.player.getY(),Player.player.getX(),-1,-1,0};
		pathMap[Player.player.getY()][Player.player.getX()][0] = current[0];
		pathMap[Player.player.getY()][Player.player.getX()][1] = current[1];
		pathMap[Player.player.getY()][Player.player.getX()][2] = current[2];
		pathMap[Player.player.getY()][Player.player.getX()][3] = current[3];
		pathMap[Player.player.getY()][Player.player.getX()][4] = current[4];

		queue.add(current);
		//Begin BFS
		while (queue.size() > 0) {
			current = queue.remove(0);
			//TODO replace signs with enemies and do more stuff
			if (levelMap[2][current[0]][current[1]] instanceof EnemyObject) {
				((EnemyObject)GameEngine.levelMap[2][current[0]][current[1]]).setPath(extractPath(pathMap, current[0], current[1]));
				enemyUpdateList.add((EnemyObject)GameEngine.levelMap[2][current[0]][current[1]]);
				//System.out.println("Found");
			}

			if (current[4] >= maxDist) { continue; }

			//look up
			if (current[0] > 0 && pathMap[current[0]-1][current[1]][4] < 0) {
				int[] next = {current[0] - 1, current[1], current[0], current[1], current[4]+1};
				pathMap[next[0]][next[1]][0] = next[0];
				pathMap[next[0]][next[1]][1] = next[1];
				pathMap[next[0]][next[1]][2] = next[2];
				pathMap[next[0]][next[1]][3] = next[3];
				pathMap[next[0]][next[1]][4] = next[4];

				if (GameEngine.levelMap[0][next[0]][next[1]] != null && 
						!GameEngine.levelMap[0][next[0]][next[1]].isSolid()) {
					queue.add(next);
						}
			}
			//look left
			if (current[1] > 0 && pathMap[current[0]][current[1]-1][4] < 0) {
				int[] next = {current[0], current[1]-1, current[0], current[1], current[4]+1};
				pathMap[next[0]][next[1]][0] = next[0];
				pathMap[next[0]][next[1]][1] = next[1];
				pathMap[next[0]][next[1]][2] = next[2];
				pathMap[next[0]][next[1]][3] = next[3];
				pathMap[next[0]][next[1]][4] = next[4];

				if (GameEngine.levelMap[0][next[0]][next[1]] != null && 
						!GameEngine.levelMap[0][next[0]][next[1]].isSolid()) {
					queue.add(next);
						}
			}
			//look down
			if (current[0] < pathMap.length-1 && pathMap[current[0]+1][current[1]][4] < 0) {
				int[] next = {current[0] + 1, current[1], current[0], current[1], current[4]+1};
				pathMap[next[0]][next[1]][0] = next[0];
				pathMap[next[0]][next[1]][1] = next[1];
				pathMap[next[0]][next[1]][2] = next[2];
				pathMap[next[0]][next[1]][3] = next[3];
				pathMap[next[0]][next[1]][4] = next[4];

				if (GameEngine.levelMap[0][next[0]][next[1]] != null && 
						!GameEngine.levelMap[0][next[0]][next[1]].isSolid()) {
					queue.add(next);
						}
			}
			//look right
			if (current[0] < pathMap[0].length-1 && pathMap[current[0]][current[1]+1][4] < 0) {
				int[] next = {current[0], current[1]+1, current[0], current[1], current[4]+1};
				pathMap[next[0]][next[1]][0] = next[0];
				pathMap[next[0]][next[1]][1] = next[1];
				pathMap[next[0]][next[1]][2] = next[2];
				pathMap[next[0]][next[1]][3] = next[3];
				pathMap[next[0]][next[1]][4] = next[4];

				if (GameEngine.levelMap[0][next[0]][next[1]] != null && 
						!GameEngine.levelMap[0][next[0]][next[1]].isSolid()) {
					queue.add(next);
						}
			}






		}
		//Secondary pass
		for (int i = 0; i < pathMap.length; i++) {
			for (int j = 0; j < pathMap[i].length; j++) {
				pathMap[i][j][4] = -1;
			}
		}

		current[0] = Player.player.getY();
		current[1] = Player.player.getX();
		current[2] = -1;
		current[3] = -1;
		current[4] = 0;
		pathMap[Player.player.getY()][Player.player.getX()][0] = current[0];
		pathMap[Player.player.getY()][Player.player.getX()][1] = current[1];
		pathMap[Player.player.getY()][Player.player.getX()][2] = current[2];
		pathMap[Player.player.getY()][Player.player.getX()][3] = current[3];
		pathMap[Player.player.getY()][Player.player.getX()][4] = current[4];

		queue.add(current);
		//Begin BFS
		while (queue.size() > 0) {
			current = queue.remove(0);


			if (current[4] >= maxDist) { continue; }

			//look up
			if (current[0] > 0 && pathMap[current[0]-1][current[1]][4] < 0) {
				int[] next = {current[0] - 1, current[1], current[0], current[1], current[4]+1};
				pathMap[next[0]][next[1]][0] = next[0];
				pathMap[next[0]][next[1]][1] = next[1];
				pathMap[next[0]][next[1]][2] = next[2];
				pathMap[next[0]][next[1]][3] = next[3];
				pathMap[next[0]][next[1]][4] = next[4];

				if (GameEngine.levelMap[0][next[0]][next[1]] != null && 
						!GameEngine.levelMap[0][next[0]][next[1]].isSolid()) {
					if (GameEngine.levelMap[2][next[0]][next[1]] instanceof EnemyObject) {
						EnemyObject en = ((EnemyObject)GameEngine.levelMap[2][next[0]][next[1]]);
						if (pathMap[next[0]][next[1]][4] < en.getCurrRange()) {
							en.setPath(extractPath(pathMap, next[0], next[1]));
						}
					}    
					else {
						queue.add(next);
					}
						}
			}
			//look left
			if (current[1] > 0 && pathMap[current[0]][current[1]-1][4] < 0) {
				int[] next = {current[0], current[1]-1, current[0], current[1], current[4]+1};
				pathMap[next[0]][next[1]][0] = next[0];
				pathMap[next[0]][next[1]][1] = next[1];
				pathMap[next[0]][next[1]][2] = next[2];
				pathMap[next[0]][next[1]][3] = next[3];
				pathMap[next[0]][next[1]][4] = next[4];

				if (GameEngine.levelMap[0][next[0]][next[1]] != null && 
						!GameEngine.levelMap[0][next[0]][next[1]].isSolid()) {
					if (GameEngine.levelMap[2][next[0]][next[1]] instanceof EnemyObject) {
						EnemyObject en = ((EnemyObject)GameEngine.levelMap[2][next[0]][next[1]]);
						if (pathMap[next[0]][next[1]][4] < en.getCurrRange()) {
							en.setPath(extractPath(pathMap, next[0], next[1]));
						}
					}    
					else {
						queue.add(next);
					}
						}
			}
			//look down
			if (current[0] < pathMap.length-1 && pathMap[current[0]+1][current[1]][4] < 0) {
				int[] next = {current[0] + 1, current[1], current[0], current[1], current[4]+1};
				pathMap[next[0]][next[1]][0] = next[0];
				pathMap[next[0]][next[1]][1] = next[1];
				pathMap[next[0]][next[1]][2] = next[2];
				pathMap[next[0]][next[1]][3] = next[3];
				pathMap[next[0]][next[1]][4] = next[4];

				if (GameEngine.levelMap[0][next[0]][next[1]] != null && 
						!GameEngine.levelMap[0][next[0]][next[1]].isSolid()) {
					if (GameEngine.levelMap[2][next[0]][next[1]] instanceof EnemyObject) {
						EnemyObject en = ((EnemyObject)GameEngine.levelMap[2][next[0]][next[1]]);
						if (pathMap[next[0]][next[1]][4] < en.getCurrRange()) {
							en.setPath(extractPath(pathMap, next[0], next[1]));
						}
					}    
					else {
						queue.add(next);
					}
						}
			}
			//look right
			if (current[0] < pathMap[0].length-1 && pathMap[current[0]][current[1]+1][4] < 0) {
				int[] next = {current[0], current[1]+1, current[0], current[1], current[4]+1};
				pathMap[next[0]][next[1]][0] = next[0];
				pathMap[next[0]][next[1]][1] = next[1];
				pathMap[next[0]][next[1]][2] = next[2];
				pathMap[next[0]][next[1]][3] = next[3];
				pathMap[next[0]][next[1]][4] = next[4];

				if (GameEngine.levelMap[0][next[0]][next[1]] != null && 
						!GameEngine.levelMap[0][next[0]][next[1]].isSolid()) {
					if (GameEngine.levelMap[2][next[0]][next[1]] instanceof EnemyObject) {
						EnemyObject en = ((EnemyObject)levelMap[2][next[0]][next[1]]);
						if (pathMap[next[0]][next[1]][4] < en.getCurrRange()) {
							en.setPath(extractPath(pathMap, next[0], next[1]));
						}
					}    
					else {
						queue.add(next);
					}
						}
			}






		}


		/*
		   for (int i = 0; i < pathMap.length; i++) {
		   for (int j = 0; j < pathMap[i].length; j++) {
//System.out.print("("+pathMap[i][j][3] + "," + pathMap[i][j][2]+")\t");
System.out.print(pathMap[i][j][4]+"\t");
		   }
		   System.out.println();
		   }
		   */
		//System.out.println(extractPath(pathMap, 12, 12));
		//System.out.println("done");
	}

	//Given a pathMap and a set of coordinates, this method extracts a series of locations that comprise a path towards the player spot
	//If the size of the returned arraylist is 0, then either there is no valid path or the given coordinates are equal to those of the player
	//The size of the array is equal to the pathing distance to the player
	public ArrayList<Pair<Integer,Integer>> extractPath(int[][][] pathMap, int y, int x) {
		ArrayList<Pair<Integer,Integer>> path = new ArrayList<Pair<Integer,Integer>>();
		Pair<Integer,Integer> current;
		int currx = x;
		int curry = y;
		while (curry >= 0 && curry < pathMap.length 
				&& currx >= 0 && currx < pathMap[curry].length 
				&& pathMap[curry][currx][4] > 0) {
			current = new Pair<Integer,Integer>(pathMap[curry][currx][3],pathMap[curry][currx][2]);
			path.add(current);
			currx = current.x;
			curry = current.y;
				}

		return path;
	}

	public static void updateInput(String input) {
		if(!input.equals("") && !input.equals(currentInput)) {
			currentInput = input;
		}

	}
}
