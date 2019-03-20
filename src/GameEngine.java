import java.util.*;

class GameEngine {
	public final float FASTRATE = 31.25f;
	public static final float SLOWRATE = 500f;
	private final float MILLITONANO = 1_000_000;
	private final int MAXHISTORY = 30;
	public float currSlowRate;

	enum MODE {
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
		GameEngine.unPause();
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
		//Update player and stuff
		if (currentInput.equals("Q") && moveHist.history.size() > 0) {
			setState(MODE.REVERSION);
		}
		else {
			setState(MODE.GAME);
			currSlowRate = SLOWRATE;
		}
		
		if (gameMode == MODE.GAME) {
			updatePlayer();
			
			if (!levelEnd.interact()); {
				levelEnd.setText(("Congratulations! Tutorial Complete\n" +
						"Level Stats:\n" +
						"\tNumber of Up Moves: " + tracker.getUpScore()));
			}
			
			System.out.println("Slow tick: "+slowIts+"\n"+moveHist);
		}
		else if (gameMode == MODE.REVERSION) { 
			revert();
		}
		prevMode = gameMode;
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

	public void revert() {
		currSlowRate = SLOWRATE / 2;
		Pair<Integer, Integer> prevPos = moveHist.pop();
		if (prevPos != null) {
			//do stuff
			levelMap[2][Player.player.getY()/32][Player.player.getX()/32] = null;
			Player.player.setX(prevPos.x * 32);
			Player.player.setY(prevPos.y * 32);
			levelMap[2][prevPos.y][prevPos.x] = Player.player;

			
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
			return true;
		}
		return false;
	}

	public static void unPause() {
		gameMode = prevMode;
	}

	public static void updateInput(String input) {
		if(!input.equals("") && !input.equals(currentInput)) {
			currentInput = input;
		}
		
	}
}
