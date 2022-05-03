import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.AlphaComposite;
import java.awt.Image;

public class Particle_Dust extends Particle {

  public Particle_Dust(int x, int y, Image image) {

    super(x, y, image);

    /* random spawn around the value given */
    this.x = x + (int)(Math.random()*5)-2;
    this.y = y + (int)(Math.random()*5)-2;
    this.width = 10;
    this.height = 10;
    this.lifeTime = 500 + Math.random() * 600; // more or less random
  }

  @Override
  public void update() {
    /* give it some random displacement */
    this.x += (int)(Math.random()*5)-2;
    this.y += (int)(Math.random()*5)-2;
    /* the dust is constently rotating on itself */
    at.rotate(Math.toRadians(angle), this.x+this.width/2, this.y+this.height/2);

    super.update();
  }
}