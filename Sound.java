import java.io.File;
import javax.sound.sampled.*;


public class Sound {

  // main theme
  public Clip intro; // main theme intro
  public Clip mainLoop; // main loop
  public Clip splash; // splash sound
  public Clip explosionSound; // death sound
  public Clip pew; // pew pew sound
  public Clip grosPew; // gros pew pew
  public Clip end; // game over sound

  public Sound() {

    String url;
    AudioInputStream is;

    // load sounds
    try{
      url = "assets/sounds/MusicIntro.wav";
      is = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
      intro = AudioSystem.getClip();
      intro.open(is);

      url = "assets/sounds/MusicLoop.wav";
      is = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
      mainLoop = AudioSystem.getClip();
      mainLoop.open(is);

      url = "assets/sounds/gameover.wav";
      is = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
      end = AudioSystem.getClip();
      end.open(is);

      url = "assets/sounds/pew.wav";
      is = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
      pew = AudioSystem.getClip();
      pew.open(is);

      url = "assets/sounds/gros_piou.wav";
      is = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
      grosPew = AudioSystem.getClip();
      grosPew.open(is);

      url = "assets/sounds/ouh_death_sound.wav";
      is = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
      explosionSound = AudioSystem.getClip();
      explosionSound.open(is);

      url = "assets/sounds/splash.wav";
      is = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
      splash = AudioSystem.getClip();
      splash.open(is);

    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
