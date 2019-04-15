package game;
import java.io.Serializable;
import static java.awt.event.KeyEvent.*;

public class Settings implements Serializable {
    public static enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    public Difficulty difficulty;
    public int volume;
    public boolean soundOn;
    public int interpRate;
    
    public int UP, DOWN, LEFT, RIGHT, INVENTORY, REVERT;

    public Settings() {
        difficulty = Difficulty.MEDIUM;
        volume = 50;
        soundOn = false;
        interpRate = 5;

        UP = VK_W;
        DOWN = VK_S;
        LEFT = VK_A;
        RIGHT = VK_D;
        INVENTORY = VK_E;
        REVERT = VK_Q;

    }

    @Override
    public String toString() {
        return "Difficulty: " + difficulty
                + "\nVolume: " + volume
                + "\nsoundOn: " + soundOn
                + "\ninterpRate: " + interpRate
                + "\nUP: " + UP
                + "\nDOWN: " + DOWN
                + "\nLEFT: " + LEFT
                + "\nRIGHT: " + RIGHT
                + "\nINVENTORY: " + INVENTORY
                + "\nREVERT: " + REVERT;
    }

}
