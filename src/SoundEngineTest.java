/*
 *	This code would be the test of the play ogg files
 *	with Javax audio library.
 *	JoVorbis.
 */


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import java.util.Properties;
import javax.sound.sampled.AudioFileformat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class SoundEngineTest{

	public void testPlayFile()
	{
		
		try{
			
	
			String filename = "/c/Users/lazyg/sotc/sanctum-of-the-chalice/src/SilentHill.ogg";
			File file = new File(filename);
			System.out.println("Start: "+filename);
		
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
	
			AudioInputStream din = null;
		
			if(in != null){
		
				AudioFormat baseFormat = in.getFormat();
				AudioFormat decodedFormat = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED,
									     baseFormat.getSampleRate(), 16,
									     baseFormat.getChannels(),
									     baseFormat.getChannels()*2,
									     baseFormat.getSampleRate(),
									     false);
				din = AudioSystem.getAudioInputStream(decodedFormat, in);
				rawplay(decodedFormat,din);
				in.close();
				assertTrue("testPlay: done",true);
			}
		}catch(Exception e){
			assertTrue("TestPlay: "+e.getMessage(),false);
		}
	}
	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException{
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

	private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException
	{
		byte[] data = new byte[4096];

		SouceDataLine line = getLine(targetFormat);
		if(line != null){
			//start
			line.start();
			int nBytesRead = 0;
			int nBytesWritten =0;
			while(nBytesRead != -1){
				nBytesRead = din.read(data,0,data.length);
				if(nBytesRead != -1){
					nBytesWritten = line.write(data, 0, nBytesRead);
				}
			}
			//stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}


}
