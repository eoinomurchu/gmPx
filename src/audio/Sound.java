package audio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound
{
	/** Stores sounds in memory */
	private static HashMap<String, Clip> bufferedSound = new HashMap<String, Clip>();

	private Sound()
	{
		//no public constructor, static class
	}
	

	/**
	 * Plays a sound file from a buffer in memory. If the sound file doesn't
	 * exist in memory it will be loaded and buffered in memory.
	 */
	public static void playBuffered(String file)
	{
		Clip clip = bufferedSound.get(file);
		
		if (clip == null)
		{
			try
			{
				AudioInputStream ain = AudioSystem
						.getAudioInputStream(new File(file));

				AudioFormat format = ain.getFormat();
				System.out.println(format);
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				
				clip = (Clip) AudioSystem.getLine(info);
				clip.open(ain);

				bufferedSound.put("awp1.wav", clip);

			}
			catch (UnsupportedAudioFileException e)
			{
				System.out.println("unsupported file type: \"" + file + "\"\n"
						+ e.getMessage() + "\ncontinuing without playing sound file");
				// e.printStackTrace();
				return;
			}
			catch (IOException e)
			{
				System.out.println("IO Exception when loading \"" + file + "\"\n"
						+ e.getMessage() + "\ncontinuing without playing sound file");
//				e.printStackTrace();
				return;
			}
			catch (LineUnavailableException e)
			{
				System.out.println("Line unavailable: " + e.getMessage() + 
						"\ncontinuing without playing \"" + file + "\"");
//				e.printStackTrace();
				return;
			}
		}

		clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}
	
	/**
	 * Plays a sound file, streaming it from disk.
	 */
	public static void playStreamed(String file)
	{
		//not implemented yet
		//TODO
		assert false;
	}
	


	public static void main(String[] args)
			throws UnsupportedAudioFileException, IOException,
			LineUnavailableException, InterruptedException
	{

		// s.playBuffered("awp1.wav");

		// s.playBuffered("sine4416ml.wav");
		
//		s.playBuffered("scout_fire-1.wav");
//		s.playBuffered("explode4.wav");
//		s.playBuffered("explode3.wav");
//		s.playBuffered("explode5.wav");
//		s.playBuffered("c4_explode1.wav");
//		s.playBuffered("thunder4.wav");
//		 s.playBuffered("Opera.wav");
//		 s.playBuffered("10 Butterflies and Hurricanes.wav");
//		 s.playBuffered("awp1.wav");
//		 s.playBuffered("awp1.wav");

		Thread.sleep(500);

		// s.playBuffered("awp1.wav");
		System.out.println( (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 );
		while (true)
		{
			Thread.sleep(1000);
		}
	}

	
}
