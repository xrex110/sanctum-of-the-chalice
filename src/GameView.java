import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Font;
public class GameView extends JPanel {
	
	private SpriteLoader loader;
	private String sheetPath;
	Random rand;
	int redraw = 1;

    int frameCount = 0;             /* Tracks number of frames since last check */
    double timeSinceFPSUpdate = 0;  /* Tracks last time of FPS calculation in MS */
    double updateDelta = 16;        /* Difference in MS between FPS calculations */
    double fps = 0;                 /* Number of frames per second */
    
	public GameView() {
		//Fixes a nasty bug that causes repaint() to be called when
		//the OS requests for it (moving the window offscreen or bringing it
		//to focux after having it be overlapped by another window) and thus
		//cause everything to be reloaded within the dirty area only. Very nasty
		
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
		//SpriteLoader loader = new SpriteLoader();
        

		Graphics2D rend = (Graphics2D) g;

		int xTiles = 25;
		int yTiles = 25;

		for(int i = 0; i < xTiles; i++) {
			for(int j = 0; j < yTiles; j++) {
				int randNum = rand.nextInt(12);
				BufferedImage img = loader.getSprite(sheetPath, randNum, 32, 32);
				rend.drawImage(img, null, i * 32, j * 32); 
			}
		}

		BufferedImage wizard = loader.getSprite("wizard.png", 0, 32, 32); 
		rend.drawImage(wizard, null, 320, 320);
		//System.out.printf("Frame %s took %.2f ms to draw\n",  redraw++, runTime);
        
        Font font = new Font("Serif", Font.PLAIN, 14);
        rend.setFont(font);
        rend.drawString("Frames Per Second: " + fps,20,20);
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
