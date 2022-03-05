import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;

// TANK BOUM BOUM
public class Tank extends Entity {

  GamePanel gp;
  KeyHandler keyH;

  public Tank(GamePanel gp, KeyHandler keyH) {

    this.gp = gp;
    this.keyH = keyH;

    this.x = 0;
    this.y = 0;
    this.speed = 1;

    try {
      sprite = ImageIO.read(getClass().getResourceAsStream("/assets/entities/tank/paintTank.png")); // load the sprite sa mère
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void update() {
    if(keyH.upPressed)
      y -= speed;
    if(keyH.leftPressed)
      x -= speed;
    if(keyH.downPressed)
      y += speed;
    if(keyH.rightPressed)
      x += speed;
  }

  public void draw(Graphics2D g2) {

    g2.drawImage(sprite, x, y, 2*gp.tileSize, 2*gp.tileSize, null);

  }

}
