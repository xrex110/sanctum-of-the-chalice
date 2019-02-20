
import java.util.*;

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
		SoundTest sTest = new SoundTest();
		boolean backGroundFlag =true;
		
		//String ar = "../res/FancyPants.wav";
		String arr = "../res/Mario.ogg";
		String arr2 = "../res/Twisting.ogg";
		playing(arr);
		playing(arr2);
		
		try{
			Thread.sleep(2000);
			

		}catch(Exception e){

		}


	}// end main function.

	public static void playing(String fName){
		SoundTest sTest = new SoundTest();
		boolean backGroundFlag = true;
		Thread soundInstance = new Thread() {
			public void run() {
				for(int i =0; backGroundFlag == true; i++){
					sTest.testFile(fName);
					//sTest.testFile(arr2);
				}
			}
		};
		soundInstance.start();

	}// end playing function

}
