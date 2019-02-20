import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Color;
import java.awt.geom.AffineTransform;

public class GameView extends JPanel {
	
	private SpriteLoader loader;
	private FontLoader fl;
	private String sheetPath;
	Random rand;					/* util.Random object for benchmarking */
	int redraw = 1;

    int frameCount = 0;             /* Tracks number of frames since last check */
    double timeSinceFPSUpdate = 0;  /* Tracks last time of FPS calculation in MS */
    double updateDelta = 16;        /* Difference in MS between FPS calculations */
    double fps = 0;                 /* Number of frames per second */
    
	TextDevice fpsText;
	TextDevice testText;
    
    //TODO: Delete me later please and thank
    Sign sign = new Sign(-128, -128, "Hello general kenobi");

	public GameView() {
		//this.setIgnoreRepaint(true);
		loader = new SpriteLoader();
		sheetPath = "test_tile.png";
		loader.cacheSheet(sheetPath, 32, 32);	//Load in and cache stuff
		loader.cacheImage("wizard.png");
		fl = new FontLoader();
		fl.loadFont("dpcomic");
		rand = new Random();

		fpsText = new TextDevice("DPComic", 20, Color.WHITE, Color.BLACK);
		testText = new TextDevice("DPComic", 45, Color.BLUE, Color.RED);
	}
    
	@Override
	public void paint(Graphics g) {
        updateFPS();
		super.paint(g);		//Clears screen before every paint
        
		Graphics2D rend = (Graphics2D) g;
        positionCamera(rend);
		int xTiles = 25;	/* Number of tiles window can accomodate in x axis */
		int yTiles = 25;	/* Number of tiles window can accomodate in y axis */
        
		for(int i = 0; i < xTiles; i++) {
			for(int j = 0; j < yTiles; j++) {
				int randNum = rand.nextInt(12);
				BufferedImage img = loader.getSprite(sheetPath, randNum, 32, 32);
				rend.drawImage(img, null, i * 32, j * 32); 
			}
		}
        
		BufferedImage wizard = loader.getSprite("wizard.png", 0, 32, 32); 
		rend.drawImage(wizard, null, 320, 320);
        
        sign.draw(rend);

        Player.player.draw(rend);
        
        drawHud(rend);

		
	}

    public void drawHud(Graphics2D rend) {
        //If you remove these two lines things will start rendering in relation to the game world's 0,0
        AffineTransform at = new AffineTransform();
        rend.setTransform(at);
        
        testText.drawText(rend, "Normal Text", 50, 150);
		testText.drawOutlineText(rend, "Outlined", 50, 250);
		drawFPS(rend);
        drawPos(rend);
        
        if(sign.interact()) drawSign(rend);

    }
    public void drawSign(Graphics2D rend) {
        Color outline = Color.white;
        Color fill = new Color(0x002663);
        drawOutlinedRectangle(rend, outline, fill, 64, getHeight() - 256, getWidth() - 128, 192);
        fpsText.drawText(rend, sign.getText(), 92, getHeight() - 224);
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

	public void setInputHandler(InputHandler ih) {
		this.addKeyListener(ih);
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
        
        int transX = centerTileX - Player.player.getX();
        int transY = centerTileY - Player.player.getY();
        
        at.translate(transX, transY);

        rend.setTransform(at); 
    }

}
