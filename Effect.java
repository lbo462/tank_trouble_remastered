import java.awt.Graphics2D;
import java.awt.Image;
 
//Class that corresponds to the effect of the power ups that randomnly spawn on the game map 

public class Effect {

  public Tank t; // player having the effect
  public Image sprite;
  public double duration;
  public double bornAt;
  public boolean dead;
  public ImageManager im;

  public Effect(Tank t, double duration) {
    this.t = t;
    this.duration = duration;
    this.im = t.gp.im;
    this.dead = false;
    this.bornAt = System.currentTimeMillis();
  }

  public void setSprite(Image sprite) {
    this.sprite = sprite;
  }

  public void update() {
    double currentTime = System.currentTimeMillis();
    if(currentTime - bornAt > duration) this.dead = true; // if duration is over, kill effect
  }
  
  public void draw(int x, int y, int size, Graphics2D g2) {
  }

  public String toString() {
    return this.getClass().getSimpleName();
  }
}
