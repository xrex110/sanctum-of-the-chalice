
import com.jogamp.openal.util.*;
import com.jogamp.openal.*;

import com.jogamp.openal.ALException;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;
import java.io.*;
import java.nio.ByteBuffer;

public class SoundEngine {
	public static AL al = ALFactory.getAL();
	public int[] buff = new int[1];
	public int[] src = new int[1];
	public float[] sPos = new float[3];
	public float[] sVel = new float[3];
	public float[] lPos = new float[3];
	public float[] lVel = new float[3];
	public float[] lOri = { 0, 0, -1f, 0, 1f, 0};

	public void play(String fName)
	{

		System.out.println("play");
		ALut.alutInit();
		al.alGetError();
		
		if (loadData(fName) == AL.AL_FALSE)
		{
			System.out.println("you are a failure");
			return;
		}

		setListener();

		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(
				new Thread(
					new Runnable() {
						public void run() {
							killAll();
						}
					}
					)
				);
		al.alSourcePlay(src[0]);
		
		
		
	}

	public int loadData(String fName)
	{
		int[] format = new int[1];
		int[] size = new int[1];
		ByteBuffer[] data = new ByteBuffer[1];
		int[] freq = new int[1];
		int[] loop = new int[1];
		
		al.alGenBuffers(1, buff, 0);
		if (al.alGetError() != AL.AL_NO_ERROR)
		{
			return AL.AL_FALSE;
		}
		ALut.alutLoadWAVFile(fName, format, data, size, freq, loop);
		al.alBufferData(buff[0], format[0], data[0], size[0], freq[0]);
		
		al.alGenSources(1, src, 0);
		if (al.alGetError() != AL.AL_NO_ERROR)
		{
			return AL.AL_FALSE;
		}
		al.alSourcei(src[0], AL.AL_BUFFER, buff[0]);
		al.alSourcef(src[0], AL.AL_PITCH, 1f);
		al.alSourcef(src[0], AL.AL_GAIN, 1f);
		al.alSourcefv(src[0], AL.AL_POSITION, sPos, 0);
		al.alSourcefv(src[0], AL.AL_VELOCITY, sVel, 0);
		al.alSourcei(src[0], AL.AL_LOOPING, loop[0]);

		if (al.alGetError() == AL.AL_NO_ERROR)
		{
			return AL.AL_TRUE;
		}
		return AL.AL_FALSE;

	}

	public void setListener()
	{
		al.alListenerfv(AL.AL_POSITION, lPos, 0);
		al.alListenerfv(AL.AL_VELOCITY, lVel, 0);
		al.alListenerfv(AL.AL_ORIENTATION, lOri, 0);
	}

	public void killAll()
	{
		al.alDeleteBuffers(1, buff, 0);
		al.alDeleteSources(1, src, 0);
		ALut.alutExit();
	}



	

	public void loop(String fName)
	{
		
  		play(fName);
		al.alSourcei(src[0],AL.AL_LOOPING,1);
		al.alSourcePlay(src[0]);

	}


}
