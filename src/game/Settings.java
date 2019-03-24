package game;
import java.io.Serializable;
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

    public Settings() {
        difficulty = Difficulty.MEDIUM;
        volume = 50;
        soundOn = false;
        interpRate = 5;
    }

    @Override
    public String toString() {
        return "Difficulty: " + difficulty
                + "\nVolume: " + volume
                + "\nsoundOn: " + soundOn
                + "\ninterpRate: " + interpRate;
    }

}
