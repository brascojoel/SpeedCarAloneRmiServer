
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author brasc
 */
public class SoundGame {
    public  static final SoundGame started =  new SoundGame("./SoundGame/cargeardown.wav");
    public  static final SoundGame brake =  new SoundGame("./SoundGame/CarCrash.wav");
    public  static final SoundGame left =  new SoundGame("./SoundGame/TireSKID.wav");
    public  static final SoundGame right =  new SoundGame("./SoundGame/TireSKID.wav");
    public  static final SoundGame accelerate=  new SoundGame("./SoundGame/gt40takingOff.wav");
   
   // Nested class for specifying volume
   public static enum Volume {
      MUTE, LOW, MEDIUM, HIGH
   }
   
   public static Volume volume = Volume.LOW;
   
   // Each sound effect has its own clip, loaded with its own sound file.
   private Clip clip;
   
   // Constructor to construct each element of the enum with its own sound file.
    public SoundGame(String soundFileName) {
      try {
         // Use URL (instead of File) to read from disk and JAR.
         URL url = this.getClass().getClassLoader().getResource(soundFileName);
         // Set up an audio input stream piped from the sound file.
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
         // Get a clip resource.
         clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioInputStream);
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   }
   
   /**
    * Jouer la music
    */
   public synchronized void play() {
      if (volume != Volume.MUTE) {
         if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
          clip.setFramePosition(0); // rewind to the beginning
          System.out.println(" long : "+clip.getFrameLength());
       //  clip.setLoopPoints(4000,6000);
         clip.start();     // Start playing
      }
   }
   
    public void stop() {
     clip.stop();
     clip.close();
   }
  
    
    
}