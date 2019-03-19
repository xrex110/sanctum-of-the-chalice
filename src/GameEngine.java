import java.util.*;

class GameEngine {
	public final float FASTRATE = 31.25f;
	public final float SLOWRATE = 500f;
	private final float MILLITONANO = 1_000_000;
	private final int MAXHISTORY = 10;
	private float slowCount;
	private int fastIts;
	private int slowIts;
	private float gameStart;
	private boolean running;
	private String backgroundMusic = "../res/Twisting.ogg";
	private String enterSound = "../res/Mario.ogg";
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
	private GameObject[][][] levelMap;

	private MoveHistory moveHist;

	public GameEngine() {
		/*
		levelMap = new GameObject[30][30];
		entityMap = new GameObject[30][30];
		*/
		levelMap = new GameObject[3][30][30];

		//entityMap[12][12] = Player.player;
		Player.player.setX(12*32);
		Player.player.setY(12*32);
		levelMap[2][12][12]=Player.player;

		levelGen = new Generator();
		generateMap();

		int[] signPos = levelGen.getSignCoords();
		System.out.println("GE Row: " + signPos[0] + " GE Col: " + signPos[1]);
		String help = "Use the W A S D keys to move around the map!";
		
		levelEnd = new Sign(signPos[1] * 32, signPos[0] * 32, "Insert end stats here");
		/*
		entityMap[signPos[0]][signPos[1]] = levelEnd;
		entityMap[12][12] = new Sign(12*32, 12*32, help);
		*/
		levelMap[1][signPos[0]][signPos[1]]=levelEnd;
		levelMap[1][12][12] = new Sign(12*32, 12*32, help);

		moveHist = new MoveHistory(MAXHISTORY);

		renderEngine = new RenderLoop();
		renderEngine.setName("RenderEngine");
		
		soundEngine = new SoundEngine();
		tracker = new ScoreTracker();
		//player = new Player(12*32, 12*32);
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
					levelMap[0][i][j] = new Tile(j * 32, i * 32, "test_tile.png", 0, true);
				}
				else if(tileType == 1) {
					levelMap[0][i][j] = new Tile(j * 32, i * 32, "test_tile.png", 1, false);
				}
				else if(tileType == 0) {
					levelMap[0][i][j] = null;
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
		renderEngine.updateMap(levelMap);
		//renderEngine.updateEntityMap(entityMap);
		renderEngine.start();		//Starts the renderengine thread!
		
		soundEngine.play(enterSound, "enter");
		soundEngine.playLoop(backgroundMusic, "background");

		gameLoop();
	}

	public void gameLoop() {
		float timeStart = 0;
		while (running)
		{
			timeStart += System.nanoTime() / MILLITONANO;
			
			fastTick();
			if (slowCount >= SLOWRATE)
			{
				slowTick();
				slowCount -= SLOWRATE;
			}
			
			float timeElapsed = System.nanoTime() / MILLITONANO - timeStart;
			float timeToNext = FASTRATE - timeElapsed;
			slowCount += timeElapsed;
			if (timeToNext > 0)
			{
				sleepForMilli(timeToNext);
				slowCount += timeToNext;
			}
			else {
				System.out.print("lag");
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
		//Update player and stuff

		updatePlayer();
		
		if (!levelEnd.interact()); {
			levelEnd.setText(("Congratulations! Tutorial Complete\n" +
					"Level Stats:\n" +
					"\tNumber of Up Moves: " + tracker.getUpScore()));
		}
		
		//System.out.println(moveHist);
		pathAll(10);
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
		int xPos = Player.player.getX()/32;
		int yPos = Player.player.getY()/32;
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


		if(levelMap[0][yPos][xPos] != null) {
			if (!levelMap[0][yPos][xPos].isSolid())
			{
				int[] displace = {yPos - Player.player.getY()/32, xPos - Player.player.getX()/32};
				levelMap[2][Player.player.getY()/32][Player.player.getX()/32] = null;

				tracker.notify(displace, ScoreTracker.MOVEEVENT);
				Player.player.setX(xPos * 32);
				Player.player.setY(yPos * 32);
				//System.out.println(xPos + " " + yPos);

				levelMap[2][yPos][xPos] = Player.player;
			}
		}

		moveHist.push(Player.player.getX()/32 , Player.player.getY()/32);
		
		

	}

	public void pathAll(int maxDist) {
		int height = levelMap[0].length;
		int width = levelMap[0][0].length;
		int[][][] pathMap = new int[height][width][5];
		ArrayList<int[]> queue = new ArrayList<int[]>();
		//initialize all distances to -1
		for (int i = 0; i < pathMap.length; i++) {
			for (int j = 0; j < pathMap[i].length; j++) {
				pathMap[i][j][4] = -1;
			}
		}
		//{currentY, currentX, prevY, prevX, dist}
		int[] current = {Player.player.getY()/32,Player.player.getX()/32,-1,-1,0};
		pathMap[Player.player.getY()/32][Player.player.getX()/32][0] = current[0];
		pathMap[Player.player.getY()/32][Player.player.getX()/32][1] = current[1];
		pathMap[Player.player.getY()/32][Player.player.getX()/32][2] = current[2];
		pathMap[Player.player.getY()/32][Player.player.getX()/32][3] = current[3];
		pathMap[Player.player.getY()/32][Player.player.getX()/32][4] = current[4];

		queue.add(current);
		//Begin BFS
		while (queue.size() > 0) {
			current = queue.remove(0);
			//look up
			if (current[0] > 0 && pathMap[current[0]-1][current[1]][4] < 0) {
				int[] next = {current[0] - 1, current[1], current[0], current[1], current[4]+1};
				pathMap[next[0]][next[1]][0] = next[0];
				pathMap[next[0]][next[1]][1] = next[1];
				pathMap[next[0]][next[1]][2] = next[2];
				pathMap[next[0]][next[1]][3] = next[3];
				pathMap[next[0]][next[1]][4] = next[4];

				if (levelMap[0][next[0]][next[1]] != null && 
					!levelMap[0][next[0]][next[1]].isSolid()) {
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

				if (levelMap[0][next[0]][next[1]] != null && 
					!levelMap[0][next[0]][next[1]].isSolid()) {
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

				if (levelMap[0][next[0]][next[1]] != null && 
					!levelMap[0][next[0]][next[1]].isSolid()) {
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

				if (levelMap[0][next[0]][next[1]] != null && 
					!levelMap[0][next[0]][next[1]].isSolid()) {
					queue.add(next);
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
		System.out.println("done");
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
