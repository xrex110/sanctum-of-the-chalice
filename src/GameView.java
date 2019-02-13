import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.Color;
import java.awt.Shape;

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
    
	public GameView() {
		//this.setIgnoreRepaint(true);
		loader = new SpriteLoader();
		sheetPath = "test_tile.png";
		loader.cacheSheet(sheetPath, 32, 32);	//Load in and cache stuff
		loader.cacheImage("wizard.png");
		fl = new FontLoader();
		//Extension for fonts are fixed to TTFs
		fl.loadFont("dpcomic");	/* loadFont accepts just the name of the font file, not the ext */
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
        
		drawText(rend, "Normal Text", "DPComic", 40, 50, 150, Color.BLUE);
		drawOutlineText(rend, "Outline Text", "DPComic", 40, 50, 250, Color.DARK_GRAY, Color.BLACK);
		drawFPS(rend);
	}
	
	public void drawFPS(Graphics2D rend) {
		String fpsStr = "Fps: " + fps;
		drawOutlineText(rend, fpsStr, "DPComic", 20, 25, 25, Color.WHITE, Color.BLACK); 
	}

	public void drawText(Graphics2D rend, String text, String fontName, int size, int x, int y, Color color) {
		Color oldColor = rend.getColor();	/* Stores the current color of g2d obj */
		Font oldFont = rend.getFont();		/* Stores the current font of g2d obj */

		Font font = new Font(fontName, Font.BOLD, size);
		rend.setFont(font);
		rend.setColor(color);
		rend.drawString(text, x, y);

		rend.setColor(oldColor);	/* Restore color to original one from before calling */	
		rend.setFont(oldFont);		/* Restore color to original one from before calling */
	}

	public void drawOutlineText(Graphics2D rend, String text, String fontName, int size, int x, int y, Color textColor, Color outlineColor) {
		//To preserve the original color and fonts
		Color oldColor = rend.getColor();
		Font oldFont = rend.getFont();

		drawText(rend, text, fontName, size, x, y, textColor);	/* To draw the inner text */

		Font font = new Font(fontName, Font.BOLD, size);
		rend.setFont(font);
		FontRenderContext frc = rend.getFontRenderContext();	/* No clue tbh */
		GlyphVector gc = font.createGlyphVector(frc, text);		/* Gets the glyphs */
		rend.setColor(outlineColor);

		//Graphics2D.translate(x,y) translates the origin of the G2D device to x, y of the JFrame
		rend.translate(x, y);		/* Needed because g2d.draw doesnt accept coords to draw at */

		Shape textOutline = gc.getOutline();  /* gc.getOutline return a Shape type */
		rend.draw(textOutline);

		//We need to return the origin of the G2D device to the origin of the JFrame
		//Otherwise all further Shape draws will be relative to x, y
		rend.translate(-x, -y);

		//Restore the old color and font settings for the Graphics2D obj
		rend.setColor(oldColor);
		rend.setFont(oldFont);
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
