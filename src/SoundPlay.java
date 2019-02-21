
import java.util.*;
import java.util.concurrent.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Properties;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javazoom.spi.PropertiesContainer;
//import junit.framework.TestCase;

public class SoundPlay {

	/*
	 *  !! required to get some flag to end the music.
	 *	about the backGroundFlag. 
	 *  it the flag changed as "false" then the music need to stop it.
	 */
	public static void main(String args[]){
		//SoundEngine object =new SoundEngine();
		//SoundEngine sTest = new SoundEngine();
		//boolean backGroundFlag =true;
		
		//String ar = "../res/FancyPants.wav";
		String arr = "../res/Mario.ogg";
		String arr2 = "../res/Twisting.ogg";
		//playing(arr2);
		//SoundTest backmusic = playLoop(arr);
		//sTest.testFile(arr2);
		SoundEngine sEngine = new SoundEngine();
		sEngine.play(arr2, "once");
		sEngine.play(arr, "upOnce");
		
		try{
			Thread.sleep(7000);
			sEngine.playLoop(arr, "looping");
			Thread.sleep(5000);

		}catch(Exception e){

		}
		sEngine.stopAllRequests();
		System.out.println("Exited");

	}// end main function.
/*
	public static SoundTest play(String fName){
		SoundTest sTest = new SoundTest();
		Thread soundInstance = new Thread() {
			public void run() {
				sTest.testFile(fName);
			}
		};
		soundInstance.start();
		return sTest;
	}// end playing function

	public static SoundTest playLoop(String fName) {
		SoundTest sTest = new SoundTest();
		Thread soundInstance = new Thread() { 
			public void run() {
				do {
					sTest.testFile(fName);
				} while (sTest.running);
			}

		};
		soundInstance.start();
		return sTest;
	}
*/
}

