import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Bullet extends Entity {

  double bornAt;
  double lifeTime = 5000; // time to live in ms
  public boolean dead = false;
  double direction;
  double angle; // for animation

  AffineTransform at = new AffineTransform();

  public Bullet(int x, int y, double direction,GamePanel gp) {

    this.x = x;
    this.y = y;
    this.gp = gp;
    this.direction = direction;
    this.speed = 10;
    bornAt = System.currentTimeMillis();

    try {
      sprite = ImageIO.read(getClass().getResourceAsStream("assets/entities/bullet.png")); // load the sprite sa mère
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void update() {

    x += speed * Math.sin(Math.toRadians(direction));
    y += speed * Math.cos(Math.toRadians(direction));

    // check it is time to die
    double currentTime = System.currentTimeMillis();
    if(currentTime - bornAt > lifeTime) dead = true;
  }

  public void draw(Graphics2D g2) {
    g2.drawImage(sprite, x, y, null);
  }
}
