import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.Color;
import java.awt.Shape;

public class TextDevice {
	
	private Font font;
	private Color innerColor;
	private Color outlineColor;

	private int fontSize;

	public TextDevice(String fontName, int size, Color ic, Color oc) {
		fontSize = size;
		setFont(fontName);	
		innerColor = ic;
		outlineColor = oc;
	}

	public void drawText(Graphics2D rend, String text, int x, int y) {
		Color oldColor = rend.getColor();	/* Stores the current color of g2d obj */
		Font oldFont = rend.getFont();		/* Stores the current font of g2d obj */

		rend.setFont(font);
		rend.setColor(innerColor);
		rend.drawString(text, x, y);

		rend.setColor(oldColor);	/* Restore color to original one from before calling */	
		rend.setFont(oldFont);		/* Restore color to original one from before calling */
	}

	public void drawOutlineText(Graphics2D rend, String text, int x, int y) {
		//To preserve the original color and fonts
		Color oldColor = rend.getColor();
		Font oldFont = rend.getFont();

		drawText(rend, text, x, y);	/* To draw the inner text */

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

	public void setFont(String fontName) {
		font = new Font(fontName, Font.BOLD, fontSize);
	}

	public void setSize(int size) {
		fontSize = size;
	}
    
    public int getPixelWidth(Graphics2D rend, String text) {
        Font old = rend.getFont();
        rend.setFont(font);
        FontMetrics fm = rend.getFontMetrics();
        int res = fm.stringWidth(text);
        rend.setFont(old);
        return res;
    }

    public int getPixelHeight(Graphics2D rend) {
        Font old = rend.getFont();
        rend.setFont(font);
        FontMetrics fm = rend.getFontMetrics();
        int res = fm.getHeight();
        rend.setFont(old);
        return res;
    }

  public void setColors(Color ic, Color oc) {
		innerColor = ic;
		outlineColor = oc;
	}

}
