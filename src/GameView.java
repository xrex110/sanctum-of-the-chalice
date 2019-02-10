import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.HashMap;
public class GameView extends JPanel {

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

    //Test code to draw a 10x10 of random tiles
    /*String sheetPath = "test.png";
    int cropWidth = 32;
    int cropHeight = 32;
    load.loadSheet(sheetPath, cropWidth, cropHeight);

    Random random = new Random();
    HashMap<String, BufferedImage> cache = load.getCache();
    for(int i = 0; i < 15*cropWidth; i += cropWidth){
      for(int j = 0; j < 15*cropHeight; j += cropHeight){
        int randNum = random.nextInt(cache.size());
        BufferedImage img = cache.get(sheetPath+randNum);
        rend.drawImage(img, null, i, j);
      }
    }*/

  }

}
