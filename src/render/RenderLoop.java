package render;
import main.*;

import game.*;
import sound.*;
import object.*;


import java.awt.event.KeyEvent;
import static main.Sanctum.settings; 

public class RenderLoop extends Thread {

	Window window;	/* Static window object */
	InputHandler in;	/* Static InputHandler object */
	public GameView gm;
    MenuView menuView;   /* Static MenuView object */
    public static final long SLEEP_TIME = 16;
	//private static int frame = 1;	/* Used for theoretical FPS calculation */
	//static long startTime = System.nanoTime();	/* Used for theoretical FPS calculation. Uncomment when needed*/

    public static final int tileSizeX = 32;
    public static final int tileSizeY = 32;

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
        Menu current = ((Menu)window.getWindowView());
		if(in.isKeyPressed(KeyEvent.VK_BACK_QUOTE)) {
			log("Window killed");
			window.killWindow();
			System.exit(1);
		}
        if(in.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            if(!menuView.equals(current)) {
                menuView.focus(current);
		        GameEngine.setPause();
	        }
        }
        if(in.isKeyPressed(KeyEvent.VK_ENTER)) {
            current.invoke("Enter");
        }
        else if(current instanceof KeybindMenu) {
            for(int i = 0; i < 255; ++i) {
                String tester = ""+ (char)i;
                if(in.isKeyPressed(i) && tester.matches("[A-Z0-9]")) {
                    current.invoke(tester);
                    break;
                }
            }
        }
		else if(in.isKeyPressed(settings.UP)) {
			//log("W was pressed on RE");
			//Player.player.moveUp();
            current.invoke("W");
			
		}
		else if(in.isKeyPressed(settings.DOWN)) {
			//log("S was pressed on RE");
			//Player.player.moveDown();
        
            current.invoke("S");
		}
		else if(in.isKeyPressed(settings.LEFT)) {
			//log("A was pressed on RE");
			//Player.player.moveLeft();
            current.invoke("A");
            
		}
		else if(in.isKeyPressed(settings.RIGHT)) {
			//log("D was pressed on RE");
			//Player.player.moveRight();
            current.invoke("D");
        }
        else if(in.isKeyPressed(settings.INVENTORY)) {
            current.invoke("INVENTORY");
        }
        else if(in.isKeyPressed(settings.REVERT)) {
            current.invoke("Q");
        }else if(in.isKeyPressed(KeyEvent.VK_F1)) {
            current.invoke("TOGGLE_HUD");
        }else if(in.isKeyPressed(KeyEvent.VK_F3)) {
            if(in.isKeyPressed(KeyEvent.VK_E))
                current.invoke("TOGGLE_OBJ");
            else if(in.isKeyPressed(KeyEvent.VK_F2))
                current.invoke("MAP_SCREENSHOT");
            else current.invoke("TOGGLE_DEBUG");
        }else if(in.isKeyPressed(KeyEvent.VK_F2)) {
            current.invoke("SCREENSHOT");
        }
		else current.invoke("");
		
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

    public static boolean invokedByRE() {
        if(Thread.currentThread().getName().startsWith("AWT"))
            return true;
        if(Thread.currentThread().getName().startsWith("Render"))
            return true;
        return false;
    }

}
