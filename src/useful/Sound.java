package useful;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	private File audio;
	private Clip clip;
	private boolean isLoop ;
	private boolean isStoped = false;
	
	public Sound (String audioAddress , boolean isLoop)
	{
		this.isLoop = isLoop;
		
		audio = new File(audioAddress);

		 try {
		 clip = AudioSystem.getClip();
		 
		 AudioInputStream inputStream = AudioSystem.getAudioInputStream(audio);             //Main.class.getResourceAsStream("/path/to/sounds/"+ url
		 clip.open(inputStream);
		 } catch (Exception e) {System.err.println(e.getMessage());}

	}
	
	public synchronized void stopSound()
	{
		clip.stop();
		
	}
	
	public synchronized void playSound() 
	{
		 new Thread(new Runnable() {
		 
		 public void run() {
			 
			 try {
			 if (isLoop)
			 {clip.loop(1000);}

			 clip.start();
			 } catch (Exception e) {System.err.println(e.getMessage());}
			 
		 }
		 }).start();
	}
	
	public boolean isActive()
	{
		return clip.isActive();
	}
}
