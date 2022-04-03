import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Image;

public class Dust extends MovingEntity {

  public Dust(int x, int y, Image image) {

    /* random spawn around the value given */
    this.x = x + (int)(Math.random()*5)-2;
    this.y = y + (int)(Math.random()*5)-2;
    this.sprite = image;
    this.lifeTime = 500 + Math.random() * 600; // more or less random

    this.dead = false;
    this.at = new AffineTransform();
    this.bornAt = System.currentTimeMillis();
  }

  @Override
  public void update() {
    double currentTime = System.currentTimeMillis();

    /* give it some random displacement */
    this.x += (int)(Math.random()*5)-2;
    this.y += (int)(Math.random()*5)-2;
    /* the dust is constently rotating on itsel*/
    at.rotate(Math.toRadians(angle), this.x+5, this.y+5);

    if(currentTime - bornAt > lifeTime) this.dead = true;
  }

  @Override
  public void draw(Graphics2D g2) {
    AffineTransform saveAt = g2.getTransform();
    g2.transform(at);

    // give it an alpha value
    AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
    g2.setComposite(alcom);

    g2.drawImage(sprite, x, y, 10, 10, null); // draw sprite

    alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
    g2.setComposite(alcom);

    g2.setTransform(saveAt);
  }

  @Override
  void collision() {} // no collision
}
