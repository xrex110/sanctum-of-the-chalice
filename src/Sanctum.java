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
		window = new Window("Sanctum of the Chalice - Rendering Engine test", gm, 800, 800);
		window.showWindow();
		window.setInputHandler(in);

		startTestLoop();
	}

	public static void startTestLoop() {
		log("==========INPUT TESTING==========");
		int count = 0;
		boolean isRunning = true;
		while(isRunning) {
			gm.repaint();

			//theoreticalFPS();	/*Uncomment when theoretical FPS is need (unbound refresh) */

			in.poll();
			count++;
			/*
			 *	TODO: Bug with the HELD state being calculated too soon
			 *	Might be tickrate related
			 */ 

			if(in.isKeyDown(KeyEvent.VK_ESCAPE)) {
				break;
			}
			if(in.isKeyDown(KeyEvent.VK_W)) {
				log("W key was triggered");
			}
			if(in.isKeyHeld(KeyEvent.VK_W)) {
				log("W key is HELD down");
			}
			else if(in.isKeyPressed(KeyEvent.VK_W)) {
				log("W key is just PRESSED, not held");
			}

			if(in.isKeyDown(KeyEvent.VK_S)) {
				log("S key was triggered");
			}
			if(in.isKeyHeld(KeyEvent.VK_S)) {
				log("S key is HELD down");
			}
			else if(in.isKeyPressed(KeyEvent.VK_S)) {
				log("S key is just PRESSED, not held");
			}

			try {
				Thread.sleep(32);	//Time for a single fast tick on avg
			}
			catch(InterruptedException e) {
				System.out.println("Timer failed? What.");
			}
		}

		System.out.println("Game HECKIN over ree");
		window.killWindow();		//Heckin' KILLS windows	
	}

	public static void theoreticalFPS() {
		long nowTime = System.nanoTime();
		if(((nowTime - startTime) / 1e9) >= 1) {
			System.out.println("FPS: " + frame);	//Theoretical FPS
			frame = 1;
			startTime = nowTime;
		}
		else frame++;
	}

	public static void log(String msg) {
		System.out.println(msg);
	}

}
