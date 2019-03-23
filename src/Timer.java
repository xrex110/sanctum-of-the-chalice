package game;
import java.io.Serializable;
public class Timer implements Serializable{
    
    private transient boolean active = false;
    private transient long lastNanoTime = 0;
    
    private long nanoseconds = 0;
    
    public Timer() {
        
    }

    public void start() {
        active = true;
        lastNanoTime = System.nanoTime();
    }

    public void stop() {
        active = false;
        nanoSeconds +=  System.nanoTime() - lastNanoTime;
    }

    public void reset() {
        nanoseconds = 0;
        lastNanoTime = System.nanoTime();
    }

    public void stop(long forcedTime) {
        active = false;
        nanoSeconds += forcedTime - lastNanoTime;
    }

    public long getNano() {
        if(active) {
            return System.nanoTime() - lastNanoTime + nanoseconds;
        }
        return nanoseconds;
    }

    public int getMillis() {
        return (int)(getNano() / 1e6);
    }

}
