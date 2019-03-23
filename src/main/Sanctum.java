package main;
import game.*;

public class Sanctum {

	public static GameEngine ge = new GameEngine();;

	public static void main(String[] args) {
		System.out.println("Starting game");
		ge.startLoop();
	}
}
