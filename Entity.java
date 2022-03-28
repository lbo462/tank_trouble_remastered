import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

// Entity class: mother of every game Entity
// Press F to pay respects
public abstract class Entity {

  GamePanel gp;
  public int x, y; // pos
  public BufferedImage sprite;

  abstract void update();
  abstract void draw(Graphics2D g2);
}
