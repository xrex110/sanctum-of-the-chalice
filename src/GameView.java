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
		SpriteLoader load = new SpriteLoader();
		Graphics2D rend = (Graphics2D) g;
		//rend.draw3DRect(320, 320, 64, 64, true);
		rend.drawImage(load.getImage("wizard.png"), null, 320, 320);

        //Test code to draw a 10x10 of random tiles
        String sheetPath = "../res/test.png";
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
        }
        
	}

}
