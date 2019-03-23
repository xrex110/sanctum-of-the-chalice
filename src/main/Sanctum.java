package main;
import game.*;

public class Sanctum {

    public static GameEngine ge = new GameEngine();
    public static Settings settings;
 
    public static void main(String[] args) {
        System.out.println("Starting game");
        //Automatically executed when JVM shuts down
        //Warning: JVM does not always execute the hook
        Runtime.getRuntime().addShutdownHook(
            new Thread() { 
                public void run() { 
                    System.out.println("Shutdown Hook is running !");
                    SaveHandler.saveSettings(settings);
                    ge.end();
                } 
            }
        ); 
        

        ge.startLoop();
    }
}
