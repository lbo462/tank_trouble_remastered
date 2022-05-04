import java.awt.Graphics2D;
import java.awt.Image;

//This class is used when TankKitty uses its a special ability i.e. shoots special bullets

public class BulletKitty extends Bullet {

  public double timeBeforeExplosion;
  public boolean exploded;
  public int radius; // radius of explosion
  public Image explodedSprite;

  public BulletKitty(int x, int y, double direction, GamePanel gp) {
    super(x, y, direction, gp.im.bulletKitty, gp);
    this.explodedSprite = gp.im.explodedKitty;

    this.radius = 70; // radius of the cake after explosion
    this.lifeTime = 10000; // total time to live, from birth to cake vanishing
    this.timeBeforeExplosion = 1000;
    this.exploded = false;
  }

  @Override
  public void update() {
    double currentTime = System.currentTimeMillis();
    if(timeBeforeExplosion<currentTime-bornAt && !exploded)
      explode();
    super.update();
  }

  public void explode() {
    this.exploded = true;
    this.speed = 0; // stop movement
    // make a splash sound
    gp.s.splash.setFramePosition(0);
    gp.s.splash.start();
  }

  @Override
  public void collisionWithTanks() {
    if(exploded) { // only check if the bullet exploded. Does nothing otherwise
      for(Tank t: gp.players) {
        // check if a player is inside a circle of radius r
        for(int r = 0; r < radius+t.width/2; r += 5) {
          for(int a = 0; a < 360; a += 2) {
            // corresponding position in cartesian coordinates
            int xCord = (int)(getX() + r * Math.cos(Math.toRadians(a)));
            int yCord = (int)(getY() + r * Math.sin(Math.toRadians(a)));

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
    if(!exploded) super.draw(g2);
    else g2.drawImage(explodedSprite, getX()-radius, getY()-radius, 2*radius, 2*radius, gp); // draw the cake
  }
}
