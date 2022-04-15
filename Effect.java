import java.awt.Graphics2D;
import java.awt.Image;

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

  void update() {
    double currentTime = System.currentTimeMillis();
    if(currentTime - bornAt > duration) this.dead = true; // is duration is over, kill effect
  }

  void draw(int x, int y, int size, Graphics2D g2) {
    // nothing to draw by default ...
  }

  public String toString() {
    return this.getClass().getSimpleName();
  }
}
