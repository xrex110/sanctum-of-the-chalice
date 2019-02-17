import com.jogamp.openal.*;
import com.jogamp.openal.util.*;
import com.jogamp.openal.AL;
import com.jogamp.openal.ALException;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;

import java.io.*;
import java.io.File;
import java.nio.ByteBuffer;

public class SoundEngine {

	public void play(String fName)
	{







	}

	public void loop(String fName)
	{
		

	 	System.out.println("Start Loop");
		static AL al = ALFactory.getAL();
		//buffer hold sound data
		static int[] buffer = new int[1];

		//source are points emitting sound
		static int[] source = new int[1];

		/*
		 * Pos and Velocity of the sound source
		 */
		static float[] sourcePos = { 0.0f, 0.0f, 0.0f};
		static float[] sourceVel = { 0.0f, 0.0f, 0.0f};
		
		/*
		 * Position, Velocity, and Orientation of the Listner
		 * About the Orientation
		 * 	1st 3 element = "at"
		 * 	2nd 3 element = "up"
		 */
		
		static float[] listnerPos = { 0.0f, 0.0f, 0.0f };
		static float[] listnerVel = { 0.0f, 0.0f, 0.0f };
		static float[] listnerOri = { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f };
		

	}


}
