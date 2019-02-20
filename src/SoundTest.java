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


public class SoundTest{

	
	private String name = null;

	public void testFile(String fileName){
		try{
			System.out.println("Start: "+ fileName+" !!!");
			File file = new File(fileName);
			AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
			System.out.println("The Audio Type is : " + aff.getType()); 

			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			AudioInputStream din = null;
			if( in != null){
				AudioFormat baseFormat = in.getFormat();
				System.out.println("SourceFormat : "+ baseFormat.toString());
				AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
															 baseFormat.getSampleRate(),
															 16,
															 baseFormat.getChannels(),
															 baseFormat.getChannels() *2,
															 baseFormat.getSampleRate(),
															 false);
				System.out.println("Target Format: " + decodedFormat.toString());
				din = AudioSystem.getAudioInputStream(decodedFormat, in);
				/*
				if(din instanceof PropertiesContainer){
					assertTrue("PropertiesContainer: OK", true);

				}else{
					assertTrue("wrong propertiesContainer instnace", false);
				}
				*/
				rawplay(decodedFormat, din);
				in.close();
				System.out.println("STOP: " + fileName + " !!");
				//assertTrue("testPlay: OK", true);
				return;
			}
	     }catch (Exception e)
		 {
			 //assertTrue("testPlay : "+e.getMessage(),false);
		 }
	

	}
	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
	{
	  SourceDataLine res = null;
	  DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	  res = (SourceDataLine) AudioSystem.getLine(info);
	  res.open(audioFormat);
	  return res;
	}
	private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException
	{
		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);		
		if (line != null)
		{
		  // Start
		  line.start();
		  int nBytesRead = 0, nBytesWritten = 0;
		  while (nBytesRead != -1)
		  {
			nBytesRead = din.read(data, 0, data.length);
			if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
		  }
		  // Stop
		  line.drain();
		  line.stop();
		  line.close();
		  din.close();
		}		
	}
}