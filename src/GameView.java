import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class GameView extends Menu {
	
	private SpriteLoader loader;
	private FontLoader fl;
	private String sheetPath;
	Random rand;					/* util.Random object for benchmarking */
	int redraw = 1;

    int frameCount = 0;             /* Tracks number of frames since last check */
    double timeSinceFPSUpdate = 0;  /* Tracks last time of FPS calculation in MS */
    double updateDelta = 16;        /* Difference in MS between FPS calculations */
    double fps = 0;                 /* Number of frames per second */
    
	//BufferedImage wizard;
	private GameObject[][][] map;	
	//private GameObject[][] emap;

	TextDevice fpsText;
	TextDevice testText;
    PlayerStatusHUD playerStatusHud;
    
    //TODO: Delete me later please and thank
    //Sign sign = new Sign(-128, -128, "Hello general kenobi");
    
    private int playerXCopy = 0;
    private int playerYCopy = 0;
    private List<Pair<Integer, Integer>> cameraInterp; 
    
    private static int interpRate = 5;
    private Sign signSelected = null;
    
    private InventoryMenu inventoryMenu;
    
    //Handles the rendering of time until next tick
	public GameView() {
        super(0,0,null);
		//this.setIgnoreRepaint(true);
		loader = new SpriteLoader();
		sheetPath = "test_tile.png";
		loader.cacheSheet(sheetPath, 32, 32);	//Load in and cache stuff
		//loader.cacheImage("wizard.png");
		fl = new FontLoader();
		fl.loadFont("dpcomic");
		rand = new Random();
		//wizard  = loader.getSprite("wizard.png", 0, 32, 32); 

		fpsText = new TextDevice("DPComic", 20, Color.WHITE, Color.BLACK);
		testText = new TextDevice("DPComic", 45, Color.BLUE, Color.RED);
        playerStatusHud = new PlayerStatusHUD(800,800, fpsText);
		
		map = new GameObject[1][1][1];
		//emap = new GameObject[1][1];
        
        cameraInterp = new ArrayList<Pair<Integer, Integer>>(); 
        inventoryMenu = new InventoryMenu(800,800,this);
		this.setBackground(Color.BLACK);
	}
    
	@Override
	public void paint(Graphics g) {
        updateFPS();
        //TODO: Ask shu why we need super.paint(g) for GV specifically
		super.paint(g);		//Clears screen before every paint
        
		Graphics2D rend = (Graphics2D) g;
        positionCamera(rend);
		int xTiles = 25;	/* Number of tiles window can accomodate in x axis */
		int yTiles = 25;	/* Number of tiles window can accomodate in y axis */
        
        if(frameCount % (int)(GameEngine.SLOWRATE / RenderLoop.SLEEP_TIME) == 0)
            playerStatusHud.setKey("");

		/*for(int i = 0; i < xTiles; i++) {
			for(int j = 0; j < yTiles; j++) {
				int randNum = rand.nextInt(12);
				BufferedImage img = loader.getSprite(sheetPath, randNum, 32, 32);
				rend.drawImage(img, null, i * 32, j * 32); 
			}
		}*/
        
		if(map[0].length != 1) {
			for(int i = 0; i < map[0].length; i++) {
				for(int j = 0; j < map[0][i].length; j++) {
					if(map[0][i][j] != null) map[0][i][j].draw(rend);	
				}
			}
		}
        
		if(map[1].length != 1) {
            signSelected = null;
			for(int i = 0; i < map[1].length; i++) {
				for(int j = 0; j < map[1][i].length; j++) {
					if(map[1][i][j] instanceof Sign) {
						Sign sign = (Sign) map[1][i][j];
						//System.out.println("RENDEREING SIGN: " + sign.getText());
						sign.draw(rend);
						if(sign.interact(playerXCopy, playerYCopy)) signSelected = sign; 
					}
					else if(map[1][i][j] instanceof Chest) {
						Chest chest = (Chest) map[1][i][j];
						chest.draw(rend);
					}
				}
			}
 		}
		//rend.drawImage(wizard, null, 320, 320);
        
        //sign.draw(rend);
		if(map[2].length != 1) {
			for(int i = 0; i < map[2].length; i++) {
				for(int j = 0; j < map[2][i].length; j++) {
					if(map[2][i][j] != null && map[2][i][j] != Player.player) map[2][i][j].draw(rend);	
				}
			}
		}
        Player.player.draw(rend, playerXCopy, playerYCopy);
        drawHud(rend);
	}

	public void setMap(GameObject[][][] map) {
		this.map = map;	
	}
	/*
	public void setEMap(GameObject[][] emap) {
		this.emap = emap;
	}*/

    public void drawHud(Graphics2D rend) {
        //If you remove these two lines things will start rendering in relation to the game world's 0,0
		AffineTransform oldAt = rend.getTransform();
        AffineTransform at = new AffineTransform();
        rend.setTransform(at);
        
        //testText.drawText(rend, "Normal Text", 50, 150);
		//testText.drawOutlineText(rend, "Outlined", 50, 250);
        drawFog(rend);
		drawFPS(rend);
        drawPos(rend);
        drawMem(rend);
        
        playerStatusHud.draw(rend);

        if(signSelected != null) {
                drawSign(rend, signSelected);
                signSelected = null;
        }

		rend.setTransform(oldAt);

    }
    
    Gradient fogVertical = new Gradient(0,0,800,800,true,400,0,2,Color.black);
    Gradient fogHorizontal = new Gradient(0,0,800,800,false,400,0,2,Color.black);

    public void drawFog(Graphics2D rend) {
        float fogDensity = 1.5f;
        fogHorizontal.setDensity(fogDensity);
        fogHorizontal.drawInverted(rend);
        fogVertical.setDensity(fogDensity);
        fogVertical.drawInverted(rend);
    }

    public void drawSign(Graphics2D rend, Sign sign) {
		AffineTransform oldAt = rend.getTransform();
		AffineTransform at = new AffineTransform();
		rend.setTransform(at);
        Color outline = Color.white;
        Color fill = new Color(0x002663);
        drawOutlinedRectangle(rend, outline, fill, 64, getHeight() - 256, getWidth() - 128, 192);
        fpsText.drawOutlineText(rend, sign.getText(), 92, getHeight() - 224);
		rend.setTransform(oldAt);
    }

    public void drawOutlinedRectangle(Graphics2D rend, Color outline, Color fill, int x, int y, int width, int height) {
        rend.setColor(fill);
        rend.fillRect(x,y,width,height);
        rend.setColor(outline);
        rend.drawRect(x,y,width,height);

    }

	public void drawFPS(Graphics2D rend) {
		String fpsStr = "Fps: " + fps;
		fpsText.drawOutlineText(rend, fpsStr, 25, 25);
	}

    public void drawPos(Graphics2D rend) {
        String posStr = "Pos: (" + Player.player.getX() + ", " + Player.player.getY() + ")";
        fpsText.drawOutlineText(rend, posStr, 25, 50);
    }

    public void drawMem(Graphics2D rend) {
        int memoryTotal = (int)(Runtime.getRuntime().totalMemory() / 1024); 
        int memoryFree = (int)(Runtime.getRuntime().freeMemory() / 1024);
        int memoryUsed = memoryTotal - memoryFree;
        int memoryMax = (int)(Runtime.getRuntime().maxMemory() / 1024);
        String ramString = "Heap: " + memoryUsed + " / " + memoryMax + " KB";
        fpsText.drawOutlineText(rend, ramString, 25, 75);
    }
	
    private void updateFPS() {
        frameCount++;

        double currentTime = System.nanoTime() / 1e8;
        if(currentTime - timeSinceFPSUpdate >= updateDelta) {
            fps = frameCount * 10 / updateDelta;
            frameCount = 0;
            timeSinceFPSUpdate = System.nanoTime() / 1e8;
        }


    }

    private void positionCamera(Graphics2D rend) {
        AffineTransform at = new AffineTransform();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        
        //TODO: Make this not garbage later
        int PLAYER_WIDTH = 32;
        int PLAYER_HEIGHT = 32;
        
        int centerTileX = centerX - PLAYER_WIDTH / 2;
        int centerTileY = centerY - PLAYER_HEIGHT / 2;
        if(cameraInterp.size() > 0){
            //We must interpolate from the old point to new point
            Pair<Integer, Integer> coords = cameraInterp.remove(0);
            
            playerXCopy = coords.x;
            playerYCopy = coords.y;
        } else {
            //We don't need to interpolate
            int goalX = Player.player.getX();
            int goalY = Player.player.getY();
            int deltaX = goalX - playerXCopy;
            int deltaY = goalY - playerYCopy;
            //System.out.printf("GoalX: %d GoalY: %d X: %d Y: %d\n",goalX,goalY,playerXCopy, playerYCopy);
            
            for(int i = 0; i < interpRate; ++i){
                int interpX = playerXCopy + i*deltaX / interpRate;
                int interpY = playerYCopy + i*deltaY / interpRate;
                cameraInterp.add(new Pair<Integer,Integer>(interpX, interpY));
            }
            cameraInterp.add(new Pair<Integer,Integer>(Player.player.getX(), Player.player.getY()));

        }
        
        int transX = centerTileX - playerXCopy;
        int transY = centerTileY - playerYCopy;
        
        at.translate(transX, transY);

        rend.setTransform(at); 
    }

    public static void setInterpRate(int x) {
        interpRate = x;
    }
    public static int getInterpRate() {
        return interpRate;
    }
    public void invoke(String key) {
        //Please do nothing ty
        if(!isFocused) return; 
        if(key.matches("[WASDQ]")) {
            playerStatusHud.setKey(key);
            GameEngine.updateInput(key);
            return;
        }
        if(!sanitizeInputTime(key)) return;
        /* Debug options for now */ 
        switch(key) {
            case "O":
                playerStatusHud.reduceHP(25);
                break;
            case "I":
                inventoryMenu.focus(this);
                break;
            case "K":
                playerStatusHud.stamina -= 25;
                break;
            case "L":
                playerStatusHud.stamina += 25;
                break;

        }
        

    }
}
