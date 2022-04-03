import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Sound {

  public AudioClip music; // main theme
  public AudioClip splash; // splash sound
  public AudioClip explosionSound; // death sound
  public AudioClip pew; // pew pew sound
  public AudioClip grosPew; // gros pew pew
  public AudioClip end; // game over sound

  public Sound() {

    URL url;

    // load sounds
    url = getClass().getResource("assets/sounds/mario.wav");
    music = Applet.newAudioClip(url);

    url = getClass().getResource("assets/sounds/gameover.wav");
    end = Applet.newAudioClip(url);

    url = getClass().getResource("assets/sounds/pew.wav");
    pew = Applet.newAudioClip(url);

    url = getClass().getResource("assets/sounds/gros_piou.wav");
    grosPew = Applet.newAudioClip(url);

    url = getClass().getResource("assets/sounds/ouh_death_sound.wav");
    explosionSound = Applet.newAudioClip(url);

    url = getClass().getResource("assets/sounds/splash.wav");
    splash = Applet.newAudioClip(url);
  }
}
