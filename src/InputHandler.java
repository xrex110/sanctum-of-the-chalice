/**
 *	This class implementation takes a lot of inspiration from Tim Wright's 
 *	Java Games: Keyboard and Mouse article on gamedev.net 
 *	Link here eventually
 */

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class InputHandler implements KeyListener {
	
	//Holds 3 possible states for keys. We can include more later
	//If more are included, cases for those must be accounted for in the
	//below functions
	private enum State {
		PRESSED,
		RELEASED
	}

	//We only want to monitor the first 256 keycodes (as set by KeyEvent)
	private static final int KEY_COUNT = 256;

	private boolean[] pressState;
	private State[] keyState;

	public InputHandler() {
		//There is a 1:1 mapping between keyState and pressState
		//With their indices specifying which button has been pressed
		keyState = new State[KEY_COUNT];
		pressState = new boolean[KEY_COUNT];

		//Initialize every key as released to begin with
		for(State key : keyState) key = State.RELEASED;	
	}

	//Must be synchronized to run on the same thread as RenderEngine
	public synchronized void poll() {
		//We're going to iterate over every keycode and see if its
		//its in a state that we're interested in (i.e. anything except RELEASED for now)
		
		for(int i = 0; i < KEY_COUNT; i++) {
			//If the ith key is pressed right now
			if(pressState[i]) {	
				//If the key is pressed right now, but wasn't the last time we polled, we register
				//it as a single *tap* action
				if(keyState[i] == State.RELEASED) keyState[i] = keyState[i] = State.PRESSED;
			}
			else {
				keyState[i] = State.RELEASED;
			}
		}
	}

	public boolean isKeyReleased(int code) {
		return keyState[code] == State.RELEASED;
	}

	public boolean isKeyPressed(int code) {
		return keyState[code] == State.PRESSED;
	}

	//This function is triggered by some external KeyEvent class
	//that monitors the keyboard
	public synchronized void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		//Indicates that some action has occured on key keyCode
		if(keyCode >= 0 && keyCode < KEY_COUNT) {
			//System.out.println("Key " + keyCode + " was pressed!");
			pressState[keyCode] = true;
		}
	}

	public synchronized void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		//Indicates that some action has occured on key keyCode
		if(keyCode >= 0 && keyCode < KEY_COUNT) pressState[keyCode] = false;
	}

	public void keyTyped(KeyEvent e) {
		//Idk what this is for, but it must be implemented so *shrug*
	}

}
