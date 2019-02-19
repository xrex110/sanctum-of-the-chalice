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
			slowCount += timeElapsed;
			if (timeToNext > 0)
			{
				sleepForMilli(timeToNext);
				slowCount += timeToNext;
			}
			else {
				System.out.print("lag");
			}
			timeStart = timeToNext-(long)timeToNext;
		}
		//getTickRates();
	}
	
	
	public void fastTick()
	{

		fastIts += 1;
		//fillerOperations(100_000);
	}
	
	/*This method serves as a benchmark function to see statistics on the performance of the gameloop
	 */
	public void getTickRates()
	{

		float totTime = System.nanoTime() / MILLITONANO - gameStart;
		System.out.println("Total Time: " + totTime
				+ ", Slow ticks: " + slowIts + ", Fast ticks: " + fastIts);
		System.out.println("Avg ft/s: "+(fastIts/totTime*1000)+", Avg ms/ft: "+(totTime/fastIts) 
				+", Avg st/s: "+(slowIts/totTime*1000)+", Avg ms/st: "+(totTime/slowIts));


	}
	
	/*This method runs a number of arbitrary operations in order to test the response of the gameloop to
	 * loads that it cannot handle
	 */
	private void fillerOperations(int num)
	{
		double[] x = new double[num];
		for (int i = 0; i < num; i++)
		{
			x[i] = Math.random();
		}
		Arrays.sort(x);
		
	}

	public void slowTick()
	{
		//fillerOperations(100_000);

		System.out.print(" 1");
		slowIts++;
		if (slowIts >= 30)
		{
			end();
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
	
	public void end()
	{
		running = false;
	}

}
