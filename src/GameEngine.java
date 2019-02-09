import java.util.*;

class GameEngine {
	public final float FASTRATE = 31.25f;
	public final float SLOWRATE = 500f;
	private final float MILLITONANO = 1_000_000;
	private float cycleCount;
	private float gameStart;

	public GameEngine()
	{
		
	}

	public void start()
	{
		cycleCount = 0;
		gameStart = System.nanoTime() / MILLITONANO;
		gameLoop();
	}

	public void gameLoop()
	{
		while (cycleCount < 32 * 10)
		{
			float timeStart = System.nanoTime() / MILLITONANO;
			cycleCount += 1;
			
			fastTick();
			
			
			
			float timeToNext = FASTRATE - (System.nanoTime() / MILLITONANO - timeStart);
			if (timeToNext > 0)
			{
				sleepForMilli(timeToNext);
			}
		}
		System.out.println(System.nanoTime() / MILLITONANO - gameStart);
	}

	public void fastTick()
	{
		
	}
	
	private void sleepForMilli(float t)
	{
		try {
			Thread.sleep((long)t);
		} catch (Exception e)
		{
			
		}
	}
	
}
