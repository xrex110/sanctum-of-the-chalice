import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {  

	private String titleText;
	private int xSize, ySize;

	public Window(String titleText, int x, int y) {
		this.titleText = titleText;
		this.xSize = x;
		this.ySize = y;
		
		this.setTitle(titleText);
		this.setSize(xSize, ySize);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public Window(String titleText) {
		this(titleText, 800, 800);
	}

	public void updateWindow() {
		this.setTitle(titleText);
		this.setSize(xSize, ySize);
	}

	public void showWindow() {
		this.setVisible(true);
	}

	public void setWindowSize(int x, int y) {
		xSize = x;
		ySize = y;
	}

	public void setWindowTitle(String title) {
		titleText = title;
	}

}
