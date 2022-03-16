import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

public class Bullet_Kitty extends Bullet_Super {

  double timeBeforeExplosion = 2000;
  boolean exploded = false;
  int radius; // radius of explosion
  BufferedImage spriteExploded;


  public Bullet_Kitty(int x, int y, double direction, String image, String imageExploded, GamePanel gp) {
    super(x, y, direction, image, gp);

    try {
      spriteExploded = ImageIO.read(getClass().getResourceAsStream("assets/entities/bullet/"+imageExploded)); // load the sprite
    } catch (IOException e) {
      e.printStackTrace();
    }

    radius = spriteExploded.getHeight()/2;
    lifeTime = 10000;
  }

  @Override
  public void update() {
    if(!exploded) {
      this.updateNextPosition();
      this.collision();
      this.updatePosition();
    }
    double currentTime = System.currentTimeMillis();
    if(timeBeforeExplosion<currentTime-bornAt && !exploded)
      explode();
    this.didItTouch();
    this.deadOrAlive();

  }

  public void explode() {
    exploded = true;
  }

  @Override
  public void didItTouch() {
    if(exploded) {
      for(Tank t: gp.players) {
        // check if a player is inside a circle of radius r
        for(int r = 0; r < radius; r++) {
          for(int a = 0; a < 360; a++) {
            int xCord = (int)(x + r * Math.cos(Math.toRadians(a)));
            int yCord = (int)(y + r * Math.sin(Math.toRadians(a)));

            if(xCord == t.getX() && yCord == t.getY()) {
              t.slow(true);
              break;
            }


          }
        }
      }
    }
  }

  @Override
  public void draw(Graphics2D g2) {
    if(!exploded)
      g2.drawImage(sprite, (int)(x-width), (int)(y-height), null);
    else
      g2.drawImage(spriteExploded, x-radius, y-radius, null);
  }
}
