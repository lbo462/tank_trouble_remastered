import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class Tile {

  public BufferedImage image;
  public boolean collision = false; // Can we get though and can we eat the wall ?

  public void draw(Graphics2D g2, int x, int y, int width, int height) {
    g2.drawImage(image, x, y, width, height, null); // null is the observer. Useless for BufferedImage, don't worry about it
  }

}
