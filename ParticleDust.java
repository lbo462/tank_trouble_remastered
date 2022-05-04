import java.awt.Image;

//Class used when a tank moves, as if there was dirt/dust when a player drives.

public class ParticleDust extends Particle {
 
  public ParticleDust(int x, int y, Image image) {

    super(x, y, image);

    /* random spawn around the value given */
    this.x = x + (int)(Math.random()*5)-2;
    this.y = y + (int)(Math.random()*5)-2;
    this.width = 10;
    this.height = 10;
    this.lifeTime = 500 + Math.random() * 600;
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
