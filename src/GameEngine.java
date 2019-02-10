import java.util.*;

class GameEngine {
	public final float FASTRATE = 31.25f;
	public final float SLOWRATE = 500f;
	private final float MILLITONANO = 1_000_000;
	private float slowCount;
	private int fastIts;
	private int slowIts;
	private float gameStart;
	private boolean running;

	public GameEngine()
	{
		
	}

	public void start()
	{
		fastIts = 0;
		slowIts = 0;
		slowCount = 0;
		running = true;
		gameStart = System.nanoTime() / MILLITONANO;
		gameLoop();
	}

	public void gameLoop()
	{
		float timeStart = 0;
		while (running)
		{
			timeStart += System.nanoTime() / MILLITONANO;
			
			fastTick();
			if (slowCount >= SLOWRATE)
			{
				slowTick();
				slowCount -= SLOWRATE;
			}
			
			float timeElapsed = System.nanoTime() / MILLITONANO - timeStart;
			float timeToNext = FASTRATE - timeElapsed;
			slowCount += FASTRATE;
			if (timeToNext > 0)
			{
				sleepForMilli(timeToNext);
			}
			else {
				System.out.print("lag");
			}
			timeStart = timeToNext-(long)timeToNext;
		}
		System.out.println("Total Time: " + (System.nanoTime() / MILLITONANO - gameStart)
				+ ", Slow ticks: " + slowIts + ", Fast ticks: " + fastIts);
	}
	

	public void fastTick()
	{
		fastIts += 1;
		fillerOperations(400000);
	}
	
	private void fillerOperations(int num)
	{
		for (int i = 0; i < num/2; i++)
		{
			if (i % fastIts == 0) {
			slowIts += num;
			slowIts -= num;}
		}
		
	}

	public void slowTick()
	{
		System.out.print(" 1");
		slowIts++;
		if (slowIts >= 30)
		{
			running = false;
			System.out.println();
		}
		
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
