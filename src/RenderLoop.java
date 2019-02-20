import java.awt.event.KeyEvent;

public class RenderLoop extends Thread {

	Window window;	/* Static window object */
	InputHandler in;	/* Static InputHandler object */
	GameView gm;		/* Static GameView object */
	
	//private static int frame = 1;	/* Used for theoretical FPS calculation */
	//static long startTime = System.nanoTime();	/* Used for theoretical FPS calculation. Uncomment when needed*/

	public RenderLoop() {
		in = new InputHandler();
		gm = new GameView();
		gm.setInputHandler(in);
		window = new Window("Sanctum of the Chalice", gm, 800, 800);
	}

	public void run() {
		window.showWindow();
		window.setInputHandler(in);

		startLoop();
	}

	public void startLoop() {
		//log("==========INPUT TESTING==========");
		boolean isRunning = true;
		while(isRunning) {
			gm.repaint();

			//theoreticalFPS();	/*Uncomment when theoretical FPS is need (unbound refresh) */

			handleInput();

			try {
				Thread.sleep(16);	//Time for a single fast tick
			}
			catch(InterruptedException e) {
				System.out.println("Timer failed? What.");
			}
		}

	}
	public void handleInput() {
		in.poll();

		if(in.isKeyPressed(KeyEvent.VK_ESCAPE)) {
			log("Window killed");
			window.killWindow();
			System.exit(1);
		}
		if(in.isKeyPressed(KeyEvent.VK_W)) {
			//log("W was pressed on RE");
			//Player.player.moveUp();
			GameEngine.updateInput("W");			
		}
		else if(in.isKeyPressed(KeyEvent.VK_S)) {
			//log("S was pressed on RE");
			//Player.player.moveDown();
			GameEngine.updateInput("S");
		}
		else if(in.isKeyPressed(KeyEvent.VK_A)) {
			//log("A was pressed on RE");
			//Player.player.moveLeft();
			GameEngine.updateInput("A");
		}
		else if(in.isKeyPressed(KeyEvent.VK_D)) {
			//log("D was pressed on RE");
			//Player.player.moveRight();
			GameEngine.updateInput("D");
		}
		else GameEngine.updateInput("");
		
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

	public void updateMap(GameObject[][] map) {	
		log("SIZE: " + map.length);
		//log("
		gm.setMap(map);
	}

	public void log(String msg) {
		System.out.println(msg);
	}

}
