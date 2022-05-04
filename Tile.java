import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Image;

// Defines what is a wall on the map. This class is used for collisions
 
public class Tile {

  public GamePanel gp;
  public int x, y; 
  public int life;
  public Image spriteUoD, spriteLoR;
  public boolean collision;
  public boolean up = false, down = false, right = false, left = false; // collision state
  public boolean debug = false;

  public Tile(GamePanel gp, int x, int y, boolean up, boolean down, boolean right, boolean left) {
    this.gp = gp;
    this.x = x;
    this.y = y;
    this.life = 10;

    this.up = up;
    this.down = down;
    this.right = right;
    this.left = left;

    this.spriteUoD = gp.im.UoDfull;
    this.spriteLoR = gp.im.LoRfull;

    if(up || down || right || left) collision = true;
    else collision = false;
  }

  public void update() {
    // progressively destroying the wall
    if(life > 5 && life <= 8)  {
      this.spriteUoD = gp.im.UoDcrack1;
      this.spriteLoR = gp.im.LoRcrack1;
    } else if(life > 2 && life <= 5) {
      this.spriteUoD = gp.im.UoDcrack2;
      this.spriteLoR = gp.im.LoRcrack2;
    } else if(life > 0 && life <= 2) {
      this.spriteUoD = gp.im.UoDcrack2;
      this.spriteLoR = gp.im.LoRcrack2;
    } else if(life <= 0) this.destroy();
  }

  public void draw(Graphics2D g2) {
    int w = gp.tileSize-1; // width
    int xD = x+1;
    int yD = y+1;

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
        g2.drawImage(spriteUoD, xD+3*w/8, yD, w/4, w/2+1, null);
      }
      if(down) {
        g2.drawImage(spriteUoD, xD+3*w/8, yD+w/2, w/4, w/2+1, null);
      }
      if(right) {
        g2.drawImage(spriteLoR, xD+w/2, yD+3*w/8, w/2+1, w/4, null);
      }
      if(left) {
        g2.drawImage(spriteLoR, xD, yD+3*w/8, w/2+1, w/4, null);
      }
    }
  }

  public void destroy() {
    this.life = 0;
    this.up = false;
    this.down = false;
    this.right = false;
    this.left = false;
    this.collision = false;
    for(int i = 0; i < 5; i++) gp.particles.add(new ParticleBrokenWall(x+gp.tileSize/2, y+gp.tileSize/2, gp.im.dustWall));
  }
}
