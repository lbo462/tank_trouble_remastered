import java.awt.Graphics2D;
import java.awt.Image;

// Entity class: mother of every game Entity
// Press F to pay respects
public abstract class Entity {
  public GamePanel gp;
  public int x, y; // pos
  public int width, height; // size
  public Image sprite;
  public boolean debug;

  abstract void update();
  abstract void draw(Graphics2D g2);
}
