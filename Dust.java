import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.AlphaComposite;

public class Dust extends Entity {

  AffineTransform at; // enable rotation
  double angle;
  double timeCreated;
  boolean dead;
  double timeToLive;

  public Dust(int x, int y) {

    this.x = x + (int)(Math.random()*11)-5;
    this.y = y + (int)(Math.random()*11)-5;
    angle = 0;
    dead = false;
    at = new AffineTransform();
    timeCreated = System.currentTimeMillis();
    timeToLive = 500 + Math.random() * 1000;

    // Loading the image of the tank
    try {
      this.sprite = ImageIO.read(getClass().getResourceAsStream("assets/entities/dust.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update() {
    double currentTime = System.currentTimeMillis();

    this.x += (int)(Math.random()*7)-3;
    this.y += (int)(Math.random()*7)-3;
    angle += 11;
    at.setToRotation(Math.toRadians(angle), this.x+5, this.y+5);

    if(currentTime - timeCreated > timeToLive) dead = true;
  }

  @Override
  public void draw(Graphics2D g2) {
    AffineTransform saveAt = g2.getTransform();
    g2.transform(at);

    AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
    g2.setComposite(alcom);

    g2.drawImage(sprite, x, y, 10, 10, null);

    alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
    g2.setComposite(alcom);

    g2.setTransform(saveAt);
  }
}
