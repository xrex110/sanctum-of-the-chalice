import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.HashMap;
public class GameView extends JPanel {
	
	public GameView() {
		//Fixes a nasty bug that causes repaint() to be called when
		//the OS requests for it (moving the window offscreen or bringing it
		//to focux after having it be overlapped by another window) and thus
		//cause everything to be reloaded within the dirty area only. Very nasty
		
		//this.setIgnoreRepaint(true);
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		SpriteLoader loader = new SpriteLoader();
		Graphics2D rend = (Graphics2D) g;
		//rend.draw3DRect(320, 320, 64, 64, true);

		int xTiles = 25;
		int yTiles = 25;

		String sheetPath = "test_tile.png";
		loader.cacheSheet(sheetPath, 32, 32);
		Random rand = new Random();

		for(int i = 0; i < xTiles; i++) {
			for(int j = 0; j < yTiles; j++) {
				int randNum = rand.nextInt(12);
				BufferedImage img = loader.getSprite(sheetPath, randNum, 32, 32);
				rend.drawImage(img, null, i * 32, j * 32);
			}
		}

		BufferedImage wizard = loader.cacheImage("wizard.png");
		rend.drawImage(wizard, null, 320, 320);
	}

	public void setInputHandler(InputHandler ih) {
		this.addKeyListener(ih);
	}

}
