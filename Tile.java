import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

public class Tile {

  GamePanel gp;
  int x, y;
  boolean collision;
  boolean up = false, down = false, right = false, left = false; // collision state
  boolean debug = false;

  public Tile(GamePanel gp, int x, int y, boolean up, boolean down, boolean right, boolean left) {
    this.gp = gp;
    this.x = x;
    this.y = y;

    this.up = up;
    this.down = down;
    this.right = right;
    this.left = left;

    if(up || down || right || left) collision = true;
    else collision = false;
  }

<<<<<<< HEAD
  public void draw(Graphics2D g2) {
    int w = gp.tileSize-1; // width
    int xD = x+1;
    int yD = y+1;
=======
  // "not-wall" tile
  public Tile(GamePanel gp, String tileNum) {
    this.gp = gp;
    this.tileNum = tileNum;
  }
>>>>>>> 1cf126e34ef9e82450302d951df47ee0543610ef

    if(debug) {
      // draw cell bounds
      g2.setColor(Color.RED);
      g2.setStroke(new BasicStroke(1.0f));
      g2.drawRect(x, y, w+1, w+1);
    }




    g2.setColor(Color.BLACK);
    if(collision) {
      g2.fillRect(xD+3*w/8, yD+3*w/8, w/4, w/4); // make nice transition between rectangles
      if(up) {
        g2.fillRect(xD+3*w/8, yD, w/4, w/2+1);
      }
      if(down) {
        g2.fillRect(xD+3*w/8, yD+w/2, w/4, w/2+1);
      }
      if(right) {
        g2.fillRect(xD+w/2, yD+3*w/8, w/2+1, w/4);
      }
      if(left) {
        g2.fillRect(xD, yD+3*w/8, w/2+1, w/4);
      }
    }
  }
}
