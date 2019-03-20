import java.awt.Graphics2D;

public class Chest extends GameObject {
	SpriteLoader spLoader = new SpriteLoader();

	public Chest(int x, int y) {
		super(x, y, false);
	}
	public void draw(Graphics2D rend) {
		rend.drawImage(spLoader.getSprite("chest.png", 0, 32, 32), null, getX(), getY()); 
	}
}
