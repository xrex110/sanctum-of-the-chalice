import java.awt.event.KeyEvent;

public class Sanctum {

	static Window window;
	static InputHandler in;
	static GameView gm;
	
	//private static int frame = 1;	/* Used for theoretical FPS calculation */
	//static long startTime = System.nanoTime();	/* Used for theoretical FPS calculation. Uncomment when needed*/

	public static void main(String[] args) {
		System.out.println("Hello, world!@@"); // hey T was here

		in = new InputHandler();
		gm = new GameView();
		gm.setInputHandler(in);
		window = new Window("Sanctum of the Chalice - Rendering Engine Test", gm, 800, 800);
		window.showWindow();
		window.setInputHandler(in);

		startTestLoop();
	}

	public static void startTestLoop() {
		log("==========INPUT TESTING==========");
		boolean isRunning = true;
		while(isRunning) {
			gm.repaint();

			//theoreticalFPS();	/*Uncomment when theoretical FPS is need (unbound refresh) */

			handleInput();

			try {
				Thread.sleep(32);	//Time for a single fast tick on avg
			}
			catch(InterruptedException e) {
				System.out.println("Timer failed? What.");
			}
		}

	}
	public static void handleInput() {
		in.poll();

		if(in.isKeyPressed(KeyEvent.VK_ESCAPE)) {
			log("Window killed");
			window.killWindow();
			System.exit(1);
		}
		if(in.isKeyPressed(KeyEvent.VK_W)) {
			log("W key is PRESSED");
		}
		if(in.isKeyPressed(KeyEvent.VK_S)) {
			log("S key is PRESSED");
		}
	}

	/*public static void theoreticalFPS() {
		long nowTime = System.nanoTime();
		if(((nowTime - startTime) / 1e9) >= 1) {
			System.out.println("FPS: " + frame);	//Theoretical FPS
			frame = 1;
			startTime = nowTime;
		}
		else frame++;
	}*/

	public static void log(String msg) {
		System.out.println(msg);
	}

}
