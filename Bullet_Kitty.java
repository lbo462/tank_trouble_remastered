import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Bullet_Kitty extends Bullet {

  double timeBeforeExplosion = 1000;
  boolean exploded = false;
  int radius; // radius of explosion
  Image explodedSprite;

  public Bullet_Kitty(int x, int y, double direction, GamePanel gp) {
    super(x, y, direction, gp.im.bulletKitty, gp);

    explodedSprite = gp.im.explodedKitty;
    radius = 50;
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
    gp.s.splash.stop();
    gp.s.splash.play();
  }

  @Override
  public void didItTouch() {
    if(exploded) {
      for(Tank t: gp.players) {
        // check if a player is inside a circle of radius r
        for(int r = 0; r < radius; r += 5) {
          for(int a = 0; a < 360; a += 2) {
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
      g2.drawImage(explodedSprite, x-radius, y-radius, gp);
  }
}
