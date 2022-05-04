import java.awt.Image;

//Class used when a wall breaks. When a wall break it separates into multiple pieces in order to add a little bit of explosion effect.

public class ParticleBrokenWall extends Particle {
  public ParticleBrokenWall(int x, int y, Image sprite) {
    super(x, y, sprite);

    this.speed = 2;
    this.width = 10;
    this.height = 10;
    at.rotate(Math.toRadians((int)(Math.random()*360)), this.x+5, this.y+5);
  }

  @Override
  public void update() {
    /* give it some linear displacement */
    this.x += speed;
    this.y += speed;

    super.update();
  }
}
