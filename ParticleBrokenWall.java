import java.awt.Image;

public class ParticleBrokenWall extends Particle {
  public ParticleBrokenWall(int x, int y, Image sprite) {
    super(x, y, sprite);

    this.speed = 2;
    this.width = 10;
    this.height = 10;
    at.rotate(Math.toRadians((int)(Math.random()*360)), this.x+5, this.y+5);
    this.lifeTime = 500 + Math.random() * 600; // more or less random
  }

  @Override
  public void update() {
    /* give it some linear displacement */
    this.x += speed;
    this.y += speed;

    super.update();
  }
}
