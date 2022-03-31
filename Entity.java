import java.awt.Graphics2D;
import java.awt.Image;

// Entity class: mother of every game Entity
// Press F to pay respects
public abstract class Entity {

  GamePanel gp;
  public int x, y; // pos
  public Image sprite;

  abstract void update();
  abstract void draw(Graphics2D g2);
}
