import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Font;
import java.awt.Color;

public class GameView extends JPanel {
	
	private SpriteLoader loader;
	private String sheetPath;
	Random rand;					/* util.Random object for benchmarking */
	int redraw = 1;

    int frameCount = 0;             /* Tracks number of frames since last check */
    double timeSinceFPSUpdate = 0;  /* Tracks last time of FPS calculation in MS */
    double updateDelta = 16;        /* Difference in MS between FPS calculations */
    double fps = 0;                 /* Number of frames per second */
    
	public GameView() {
		//this.setIgnoreRepaint(true);
		loader = new SpriteLoader();
		sheetPath = "test_tile.png";
		loader.cacheSheet(sheetPath, 32, 32);	//Load in and cache stuff
		loader.cacheImage("wizard.png");
		rand = new Random();
	}
	@Override
	public void paint(Graphics g) {
        updateFPS();
		super.paint(g);		//Clears screen before every paint
        
		Graphics2D rend = (Graphics2D) g;

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
		drawFPS(rend);
        
	}
	
	public void drawFPS(Graphics2D rend) {
        Font font = new Font("Serif", Font.BOLD, 14);
        rend.setFont(font);
		//The next 5 lines allow us to draw a black outline for the FPS counter
		//Making it more readable
		rend.setColor(Color.BLACK);
        rend.drawString("FPS: " + fps,19,19);
        rend.drawString("FPS: " + fps,19,20);
        rend.drawString("FPS: " + fps,20,19);
        rend.drawString("FPS: " + fps,21,21);
		rend.setColor(Color.RED);
        rend.drawString("FPS: " + fps,20,20);
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

}
