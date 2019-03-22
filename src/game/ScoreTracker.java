package game;
import main.*;

import render.*;
import sound.*;
import object.*;


import java.util.*;

public class ScoreTracker 
{
	public static final int MOVEEVENT = 0;
	
	private int upScore = 0;

	public ScoreTracker() {

	}

	public void notify(int[] info, int eventType)
	{
		//System.out.println("notified");
		if (eventType == MOVEEVENT) {
			processMoveEvent(info);
		}
		
	}

	public int getUpScore() {
		return upScore;
	}

	private void processMoveEvent(int[] info) {
		//check if input format is valid
		if (info[0] < 0)
		{
			//System.out.println("upping");
			upScore -= info[0];
		}
		
	}

}



