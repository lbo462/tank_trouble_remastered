import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;

// TANK BOUM BOUM
public class Tank extends Entity {

  GamePanel gp;
  KeyHandler keyH;

  AffineTransform at = new AffineTransform();
  double angle;

  public Tank(GamePanel gp, KeyHandler keyH) {

    this.gp = gp;
    this.keyH = keyH;

    this.x = 100;
    this.y = 100;
    this.speed = 5;
    this.angle = 0; // angle difference from the "up-orientation" to now

    try {
      sprite = ImageIO.read(getClass().getResourceAsStream("/assets/entities/tank/painTank.png")); // load the sprite sa mère
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void update() {
    if(keyH.upPressed || keyH.leftPressed || keyH.downPressed || keyH.rightPressed) {
      double prevAngle = angle; // angle before the transformation

      // keys to rotate
      if(keyH.leftPressed)
        angle -= speed;
      if(keyH.rightPressed)
        angle += speed;
      angle = angle % 360; // might be of poor use be I like it that way

      double angleToRotate = prevAngle - angle; // angle difference to adjust between then and now
      // do the rotation at the right spot
      at.rotate(Math.toRadians(angleToRotate), x+gp.tileSize/2, y+gp.tileSize/2);

      // move forward and backward
      if(keyH.upPressed)
        y -= speed;
      if(keyH.downPressed)
        y += speed;

    }

  }

  public void draw(Graphics2D g2) {

    g2.setTransform(at);
    g2.drawImage(sprite, x, y, gp.tileSize, gp.tileSize, null);

  }
}
