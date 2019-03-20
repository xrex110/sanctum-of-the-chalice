import java.awt.event.KeyEvent;

public class RenderLoop extends Thread {

	Window window;	/* Static window object */
	InputHandler in;	/* Static InputHandler object */
	GameView gm;
    MenuView menuView;   /* Static MenuView object */
    public static final long SLEEP_TIME = 16;
	//private static int frame = 1;	/* Used for theoretical FPS calculation */
	//static long startTime = System.nanoTime();	/* Used for theoretical FPS calculation. Uncomment when needed*/

	public RenderLoop() {
        
        int width = 800;
        int height = 800;
        
		in = new InputHandler();
		gm = new GameView();
        menuView = new MenuView(width, height);
        menuView.setInputHandler(in);
		window = new Window("Sanctum of the Chalice", menuView, width, height);
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
			window.getWindowView().repaint();
            //theoreticalFPS();	/*Uncomment when theoretical FPS is need (unbound refresh) */

			handleInput();

			try {
				Thread.sleep(SLEEP_TIME);	//Time for a single fast tick
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
        if(in.isKeyPressed(KeyEvent.VK_P)) {
            if(!menuView.equals((Menu)window.getWindowView())) {
                menuView.focus((Menu)window.getWindowView());
		GameEngine.setPause();
	    }
        }
		if(in.isKeyPressed(KeyEvent.VK_W)) {
			//log("W was pressed on RE");
			//Player.player.moveUp();
            ((Menu)window.getWindowView()).invoke("W");
			
		}
		else if(in.isKeyPressed(KeyEvent.VK_S)) {
			//log("S was pressed on RE");
			//Player.player.moveDown();
        
            ((Menu)window.getWindowView()).invoke("S");
		}
		else if(in.isKeyPressed(KeyEvent.VK_A)) {
			//log("A was pressed on RE");
			//Player.player.moveLeft();
            ((Menu)window.getWindowView()).invoke("A");
            
		}
		else if(in.isKeyPressed(KeyEvent.VK_D)) {
			//log("D was pressed on RE");
			//Player.player.moveRight();
            ((Menu)window.getWindowView()).invoke("D");
        }
        else if(in.isKeyPressed(KeyEvent.VK_ENTER)) {
            ((Menu)window.getWindowView()).invoke("Enter");
        }
        else if(in.isKeyPressed(KeyEvent.VK_O)) {
            ((Menu)window.getWindowView()).invoke("O");
        }
        else if(in.isKeyPressed(KeyEvent.VK_I)) {
            ((Menu)window.getWindowView()).invoke("I");
        }
        else if(in.isKeyPressed(KeyEvent.VK_K)) {
            ((Menu)window.getWindowView()).invoke("K");
        }
        else if(in.isKeyPressed(KeyEvent.VK_L)) {
            ((Menu)window.getWindowView()).invoke("L");
        }else if(in.isKeyPressed(KeyEvent.VK_Q)) {
            ((Menu)window.getWindowView()).invoke("Q");
        }

		else ((Menu)window.getWindowView()).invoke("");
		
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

	public void updateMap(GameObject[][][] map) {	
		gm.setMap(map);
	}
/*
	public void updateEntityMap(GameObject[][] emap) {
		gm.setEMap(emap);
	}
*/
	public void log(String msg) {
		System.out.println(msg);
	}

}
