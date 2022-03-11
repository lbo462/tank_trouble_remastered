import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.BasicStroke;

public class Tile {

  GamePanel gp;
  public boolean collision; // Can we get though and can we eat the wall ?
  public String tileNum;

  public boolean up = false, down = false, right = false, left = false;

  public Tile(GamePanel gp, String tileNum, boolean up, boolean down, boolean right, boolean left) {
    this.gp = gp;
    this.tileNum = tileNum;

    this.up = up;
    this.down = down;
    this.right = right;
    this.left = left;

    collision = true;
  }

  public Tile(GamePanel gp, String tileNum) {
    this.gp = gp;
    this.tileNum = tileNum;
  }



  public void draw(Graphics2D g2, int x, int y, int width, int height) {
    // draw cell bounds
    g2.setStroke(new BasicStroke(1.0f));
    g2.drawRect(x-width/2, y-height/2, width, height);
    if(!tileNum.equals("0")) {
      g2.setStroke(new BasicStroke(10.0f));
      if(up)
        g2.drawLine(x, y, x, y-height/2);
      if(down)
        g2.drawLine(x, y, x, y+height/2);
      if(right)
        g2.drawLine(x, y, x+width/2, y);
      if(left)
        g2.drawLine(x, y, x-width/2, y);
    }
  }

  public String toString() {
    return "Tile " + tileNum + " / collision " + collision;
  }

}
