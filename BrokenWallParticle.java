import java.awt.Image;

public class BrokenWallParticle extends Dust {
  public BrokenWallParticle(int x, int y, Image sprite) {
    super(x, y, sprite);

    this.speed = 2;
    at.rotate(Math.toRadians((int)(Math.random()*360)), this.x+5, this.y+5);
  }

  @Override
  public void update() {
    double currentTime = System.currentTimeMillis();

    /* give it some linear displacement */
    this.x += speed;
    this.y += speed;

    if(currentTime - bornAt > lifeTime) this.dead = true;
  }
}
