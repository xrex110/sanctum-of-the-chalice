import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GameView extends JPanel {

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		SpriteLoader load = new SpriteLoader();
		Graphics2D rend = (Graphics2D) g;
		//rend.draw3DRect(320, 320, 64, 64, true);
		rend.drawImage(load.getImage("wizard.png"), null, 320, 320);
	}

}


