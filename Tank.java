import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

// TANK BOUM BOUM
public class Tank extends Entity {

  int number;
  double lastShot;

  AffineTransform at = new AffineTransform();
  double angle; // actual angle, whatevers that means. I don't even know how this works but it works
  boolean dead = false;

  ArrayList<Bullet> bullets = new ArrayList<Bullet>();

  public Tank(int number, int x, int y, GamePanel gp, KeyHandler keyH) {

    this.gp = gp;
    this.keyH = keyH;

    this.x = x;
    this.y = y;
    this.number = number;
    this.speed = 3;
    this.angle = 0; // keep

    try {
      sprite = ImageIO.read(getClass().getResourceAsStream("assets/entities/tank/painTank.png")); // load the sprite sa mère
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void update() {

    boolean upPressed = false,
      downPressed = false,
      leftPressed = false,
      rightPressed = false,
      shotPressed = false;

    if(number == 1) {
      upPressed = keyH.zPressed;
      downPressed = keyH.sPressed;
      leftPressed = keyH.qPressed;
      rightPressed = keyH.dPressed;
      shotPressed = keyH.spacePressed;
    } else if(number == 2) {
      upPressed = keyH.upPressed;
      downPressed = keyH.downPressed;
      leftPressed = keyH.leftPressed;
      rightPressed = keyH.rightPressed;
      shotPressed = keyH.enterPressed;
    }

    if(upPressed || leftPressed || downPressed || rightPressed) {

      // ROTATION
      if(leftPressed || rightPressed) {
        double prevAngle = angle; // angle before the transformation
        // keyboard inputs
        if(leftPressed)
          this.angle -= speed;
        if(rightPressed)
          this.angle += speed;
        double angleToRotate = prevAngle - this.angle; // angle difference to adjust between then and now
        at.rotate(Math.toRadians(angleToRotate), x+(int)(gp.tileSize/2), y+(int)(gp.tileSize/2)); // do the rotation at the right spot
      }
      // TRANSLATION
      if(upPressed || downPressed) {

        int nextY = y; // keep new y in a var before updating the real y: care for collisions
        // keyboard inputs
        if(upPressed)
          nextY += speed*2;
        if(downPressed)
          nextY -= speed*2;

        // check collisions
        boolean collision = false;
        // got this sh*t out of the docs to find the positions in the reference frame
        double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
        double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();
        for(int i = nextY - (int)(gp.tileSize)/2; i <= nextY + (int)(gp.tileSize)/2; i++) {
          for(int j = x - (int)(gp.tileSize)/2; j <= x + (int)(gp.tileSize)/2; j++) {
            int xc = j + gp.tileSize/2, yc = i + gp.tileSize/2; // position of the center of the tank
            int nextX0 = (int)(m00 * xc + m01 * yc + m02);
            // The next line is hell. I hate this line. I wish it was never born and hope it'll die soon
            int nextY0 = (int)(m10 * yc + m11 * xc + m12); // you motherf*cker I hate u and you were adopted
            int xGrid, yGrid; // pos inside the map grid
            xGrid = (int)(nextX0 / gp.tileSize);
            yGrid = (int)(nextY0 / gp.tileSize);
            // collision with window bounds
            if(nextY0 < gp.tileSize/2 || nextY0 >= gp.height - gp.tileSize/2 || nextX0 < gp.tileSize/2 || nextX0 >= gp.width - gp.tileSize/2)
              collision = true;
            else if(gp.currentMap.tiles[yGrid][xGrid].collision) { // collision with tile
              Tile currentTile = gp.currentMap.tiles[yGrid][xGrid];
              int xTile = (int)(xGrid * gp.tileSize);
              int yTile = (int)(yGrid * gp.tileSize);
              int deltaX = nextX0 - xTile;
              int deltaY = nextY0 - yTile;
              if(currentTile.up && Math.abs(deltaX) < gp.tileSize/4 && deltaY < 0)
                collision = true;
              else if(currentTile.down && Math.abs(deltaX) < gp.tileSize/4 && deltaY > 0)
                collision = true;
              else if(currentTile.right && Math.abs(deltaY) < gp.tileSize/4 && deltaX > 0)
                collision = true;
              else if(currentTile.left && Math.abs(deltaY) < gp.tileSize/4 && deltaX < 0)
                collision = true;
            }
          }
        }

        if(!collision) { // update only if there's no collision
          if(upPressed)
            y += speed;
          if(downPressed)
            y -= speed;
        }
      }
    }

    if(shotPressed && System.currentTimeMillis() - lastShot > 100) {
      bullets.add(new Bullet(getX(), getY(), this.angle, gp));

      lastShot = System.currentTimeMillis();
    }

    for(int i = 0; i < bullets.size(); i++) {
      bullets.get(i).update();
      if(bullets.get(i).dead) bullets.remove(i);
    }
  }

  public void draw(Graphics2D g2) {

    AffineTransform saveAt = g2.getTransform();
    g2.setTransform(at); // askip this function is not supposed to be used this way but it works
    g2.drawImage(sprite, x, y, gp.tileSize, gp.tileSize, null);
    g2.setTransform(saveAt);

    for(int i = 0; i < bullets.size(); i++) bullets.get(i).draw(g2);

  }

  // get X and Y in the reference frame, reverting the rotating matrix at(Affine Transform)
  public int getX() {
    double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
    return (int)(m00 * (x+gp.tileSize/2) + m01 * (y+gp.tileSize/2) + m02);
  }

  public int getY() {
    double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();
    return (int)(m10 * (y+gp.tileSize/2) + m11 * (x+gp.tileSize/2) + m12);
  }
}
