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
            if(!menuView.equals((Menu)window.getWindowView()))
                menuView.focus((Menu)window.getWindowView());
        }
		if(in.isKeyPressed(KeyEvent.VK_W)) {
			//log("W was pressed on RE");
			//Player.player.moveUp();
            if(menuView.recursiveIsFocused())
                menuView.invoke("W");
			else
                GameEngine.updateInput("W");			
		}
		else if(in.isKeyPressed(KeyEvent.VK_S)) {
			//log("S was pressed on RE");
			//Player.player.moveDown();
            
            if(menuView.recursiveIsFocused())
                menuView.invoke("S");
			else
                GameEngine.updateInput("S");
		}
		else if(in.isKeyPressed(KeyEvent.VK_A)) {
			//log("A was pressed on RE");
			//Player.player.moveLeft();
            if(menuView.recursiveIsFocused())
                menuView.invoke("A");
			else
                GameEngine.updateInput("A");
		}
		else if(in.isKeyPressed(KeyEvent.VK_D)) {
			//log("D was pressed on RE");
			//Player.player.moveRight();
            if(menuView.recursiveIsFocused())
                menuView.invoke("D");
			else
                GameEngine.updateInput("D");
		}
        else if(in.isKeyPressed(KeyEvent.VK_ENTER)) {
            if(menuView.recursiveIsFocused())
                menuView.invoke("Enter");
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
		gm.setMap(map);
	}

	public void updateEntityMap(GameObject[][] emap) {
		gm.setEMap(emap);
	}

	public void log(String msg) {
		System.out.println(msg);
	}

}
