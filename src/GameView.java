import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.HashMap;
public class GameView extends JPanel {
	
	private SpriteLoader loader;
	private String sheetPath;
	Random rand;
	int redraw = 1;

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
		long curTime = System.nanoTime();
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
		long nowTime = System.nanoTime();
		double runTime = (nowTime - curTime)/1000000;
		System.out.printf("Frame %s took %.2f ms to draw\n",  redraw++, runTime);
	}

	public void setInputHandler(InputHandler ih) {
		this.addKeyListener(ih);
	}

}
