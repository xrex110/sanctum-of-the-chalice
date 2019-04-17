package game;
import main.*;

import render.*;
import sound.*;
import object.*;
import procedural.*;


import java.util.*;

public class GameEngine {
	public final float FASTRATE = 31.25f;
	public static final float SLOWRATE = 250f;
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
	////////////////Sound Lists////////////////////////////
	//background music
	private String backgroundMusic = "../res/Twisting.ogg";
	//Enemy attack flare sound
	private String enemyAtkSound= /*"../res/EnemySound.ogg"*/ "../res/classic_hurt.ogg";
	// footstep sound.
	private String footStep = "../res/footStep2.ogg";
	// fight sound.
	private String atkSound2 = "../res/attackSound2.ogg";
	////////////////////////////////////////////////////////
	private static String currentInput;
	private static int inventIndex;
	private RenderLoop renderEngine;
	private SoundEngine soundEngine;
	public ScoreTracker tracker;

	private Generator levelGen;
	public static GameObject[][][] levelMap;
	public static UsableItem[] inventory;
	public static Equipable[] equips;

	private ArrayList<EnemyObject> enemyUpdateList;
	public MoveHistory moveHist;

	private EnemyObject theEnemy;
	public static int mapWidth;
	public static int mapHeight;

	public static Timer playtime = new Timer();

	private ArrayList<UsableItem> itemList = new ArrayList<UsableItem>();
	//event number variable
	public int event_num;
	public GameEngine() {

		enemyUpdateList = new ArrayList<EnemyObject>();

		LevelMap testLevel = new LevelMap();
		testLevel.numRooms = 25;
		testLevel.linear = true;
		testLevel.minRoomSize = 7;
		testLevel.maxRoomSize = 11;
		testLevel.minSpawnSize = 6;
		testLevel.maxSpawnSize = 12;
		testLevel.tileSpriteSheet = "tiles.png";
		testLevel.wallTileCode = 0;
		testLevel.floorTileCode = 2;
		testLevel.chestSpawnChance = 55.50;
		testLevel.chestSpawnPenalty = 30;


		LevelData curLevel = null;
		while(curLevel == null) {
			levelGen = new Generator(testLevel);
			curLevel = levelGen.generateDungeon();
			System.out.println("Generating Dungeon...");
		}

		GameEngine.levelMap = curLevel.levelMap;
		this.mapWidth = curLevel.mapWidth;
		this.mapHeight = curLevel.mapHeight;
		//Event Handler
		event_num = levelGen.getEventNum();

		//Weird sink code, because Player.player exists and is static
		Player.player = new Player(curLevel.playerSpawnPosition.col, curLevel.playerSpawnPosition.row,event_num);

		JsonReader jsonread = new JsonReader();
		ArrayList<UsableItem> itemsReadIn = null;
		try { 
			itemsReadIn = jsonread.readItems();
		}
		catch (java.io.IOException e) {
			System.out.println("JSON FAIL");
		}

		inventIndex = -1;
		inventory = new UsableItem[28];
		equips = new Equipable[4];
		//Debug Items; These are examples of two potion items that have been instantiated and then cloned to the map
		//Each uses a different method of setting stat values. The one used by betterPotion is probably preferable
		//TODO remove these debug items later
		inventory[3] = new UsableItem(-1, -1, "Mundane Potion", "basicPotion.png", 2);
		inventory[3].modifier.setHP(20);
		Stat heal = new Stat(1,0,0,0,0,10,10,false);
		UsableItem betterPotion = new UsableItem(-1,-1,"Growth Potion", "growthPotion.png", 1, heal);
		
		inventory[3].cloneTo(Player.player.getX(), Player.player.getY() +1);

		betterPotion.cloneTo(Player.player.getX(), Player.player.getY() +1);
		itemList.add(betterPotion);
		heal = new Stat(1,0,0,0,-2,0,0,false);
		Equipable arm = new Equipable(-1,-1,"Underwear of Vulnerability", "underwear.png", 1, heal, Equipable.EquipType.ARMOR);
		itemList.add(arm);
		heal = new Stat(1,0,0,0,1,0,0,false);
		arm = new Equipable(-1,-1,"Leather Gambisson", "leatherArmor.png", 1, heal, Equipable.EquipType.ARMOR);
		itemList.add(arm);

		for(UsableItem item : itemsReadIn) {
			itemList.add(item);
		}

		moveHist = new MoveHistory(MAXHISTORY);
		//levelEnd = new Sign(signPositions[1].col, signPositions[1].row, "Insert end stats here");

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


	public void startLoop() {
		fastIts = 0;
		slowIts = 0;
		slowCount = 0;
		running = true;
		gameStart = System.nanoTime() / MILLITONANO;
		renderEngine.updateMap(GameEngine.levelMap);
		renderEngine.updateInventory(inventory, equips);
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
			//System.out.print(gameMode+" "+prevMode);

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
					//System.out.print("lag");
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
		if (inventIndex >= 0) {
			if (inventory[inventIndex] != null) {
				inventory[inventIndex].use();
				if (inventory[inventIndex].durability <= 0) {
					inventory[inventIndex] = null;
				}
			}
			inventIndex = -1;
		}
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
		//signSelected = null;
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


			/*if (!levelEnd.interact()); {
				levelEnd.setText(("Congratulations! Tutorial Complete\n" +
							"Level Stats:\n" +
							"\tNumber of Up Moves: " + tracker.getUpScore()));
			}*/

			//System.out.println("Slow tick: "+slowIts+"\n"+moveHist);
			pathAll(10);
			sleepForMilli(currSlowRate/2);
			while (enemyUpdateList.size() > 0) {
				//System.out.println("enemy");
				EnemyObject en = enemyUpdateList.remove(0);
				if (en.stat.checkAlive()) {
					Pair<Integer,Integer> nextLoc = en.nextLoc();
					if (nextLoc != null 
							&& !GameEngine.levelMap[0][nextLoc.y][nextLoc.x].isSolid() 
							&& GameEngine.levelMap[2][nextLoc.y][nextLoc.x] == null) {
						GameEngine.levelMap[2][en.getY()][en.getX()] = null;
						GameEngine.levelMap[2][nextLoc.y][nextLoc.x] = en;
						en.moveTo(nextLoc.x, nextLoc.y);
				
						//en.setX(nextLoc.x);
						//en.setY(nextLoc.y);
					}else if(nextLoc!= null 
						  &&levelMap[2][nextLoc.y][nextLoc.x] ==Player.player){
						soundEngine.play(enemyAtkSound, "enemyAtk");
						int[] kb = {nextLoc.y+(nextLoc.y-en.getY()),
							nextLoc.x+(nextLoc.x-en.getX())};
						if (kb[0] >= 0 && kb[0] < levelMap[0].length 
							&& kb[1] >= 0 && kb[1] < levelMap[0][0].length
							&& levelMap[0][kb[0]][kb[1]] != null
							&& !levelMap[0][kb[0]][kb[1]].isSolid()
							&& levelMap[2][kb[0]][kb[1]] == null) {
							levelMap[2][kb[0]][kb[1]] = Player.player;
							Player.player.moveTo(kb[1],kb[0]);
							levelMap[2][nextLoc.y][nextLoc.x] = null;
						}
						CombatSys.combatEnemy(en,Player.player);
						renderEngine.hurtEffect();
					}
					TriggerList trig = (TriggerList)GameEngine.levelMap[1][en.getY()][en.getX()];
					for (int i = 0; i < trig.triggers.size(); i++) {
						trig.triggers.get(i).interact(en);
					}	
				}
				else {
					//betterPotion.cloneTo(Player.player.getX(), Player.player.getY() +1);
					en.death();
					GameEngine.levelMap[2][en.getY()][en.getX()] = null;
					Random r = new Random();
					int itemNum = r.nextInt(itemList.size());
					(itemList.get(itemNum)).cloneTo(en.getX(),en.getY());
					
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
			System.out.println("\n\nEvent number : " + event_num+" \n");
			
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
			else if(levelMap[2][yPos][xPos] != null){
				if(levelMap[2][yPos][xPos] instanceof EnemyObject){
					//Combat system with collision detection.
					//when user attack, then play the sound effect.
					soundEngine.play(atkSound2, "attack2");
					//soundEngine.play(footStep, "footStep2");
				
					// int checkKill = false;
					EnemyObject en = (EnemyObject)levelMap[2][yPos][xPos];
					en.cooldown++;
					int[] kb = {Player.player.getY()+2*(yPos-Player.player.getY())
						, Player.player.getX()+2*(xPos-Player.player.getX())};
					if (kb[0] >= 0 && kb[0] < levelMap[0].length 
							&& kb[1] >= 0 && kb[1] < levelMap[0][0].length
							&& levelMap[0][kb[0]][kb[1]] != null
							&& !levelMap[0][kb[0]][kb[1]].isSolid()
							&& levelMap[2][kb[0]][kb[1]] == null) {
						levelMap[2][yPos][xPos] = null;
						en.moveTo(kb[1],kb[0]);
						levelMap[2][kb[0]][kb[1]] = en;
					}
					
					CombatSys.combatPlayer(Player.player, en);
				}

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
	//	synchronized(gameMode) {
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
		if (gameMode == MODE.PAUSE) {
			gameMode = MODE.GAME;
			prevMode = MODE.GAME;
		}

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
		}
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

	public static boolean addToInventory(UsableItem it) {
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] == null) {
				inventory[i] = it;
				return true;
			}
		}
		return false;
	}

	public static void updateInput(String input) {
		if(!input.equals("") && !input.equals(currentInput)) {
			currentInput = input;
		}

	}
	public static void updateInventIndex(int i) {
		inventIndex = i;
	}
}
