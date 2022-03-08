import java.awt.image.BufferedImage;

// Entity class: mother of every game Entity
// Press F to pay respects
public class Entity {

  GamePanel gp;
  KeyHandler keyH;

  public int x, y; // pos
  public double speed;

  public BufferedImage sprite;
}
