import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

//Particles in tank trouble are either parts of broken wall, dust or fire. This is the mother class of those particles.

public class Particle extends MovingEntity {

  public Particle(int x, int y, Image sprite) {
    this.x = x;
    this.y = y;
    this.sprite = sprite;

    this.dead = false;
    this.at = new AffineTransform();
    this.bornAt = System.currentTimeMillis();
    this.lifeTime = 500 + Math.random() * 600; 
  }

  @Override
  public void update() {
    double currentTime = System.currentTimeMillis();
    if(currentTime - bornAt > lifeTime) this.dead = true;
  }

  @Override
  public void draw(Graphics2D g2) {
    AffineTransform saveAt = g2.getTransform();
    g2.transform(at);

    g2.drawImage(sprite, x, y, width, height, null); // draw sprite

    g2.setTransform(saveAt);
  }

  @Override
  public void collision() {}
}
