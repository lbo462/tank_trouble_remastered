import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;

// TANK BOUM BOUM
public class Tank extends Entity {

  GamePanel gp;
  KeyHandler keyH;

  AffineTransform at = new AffineTransform();
  double angle; // actual angle, whatevers that means. I don't even know how this works but it works

  public Tank(GamePanel gp, KeyHandler keyH) {

    this.gp = gp;
    this.keyH = keyH;

    this.x = 100;
    this.y = 100;
    this.speed = 2;
    this.angle = 0; // initial angle = 0

    try {
      sprite = ImageIO.read(getClass().getResourceAsStream("/assets/entities/tank/painTank.png")); // load the sprite sa mère
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void update() {
    if(keyH.upPressed || keyH.leftPressed || keyH.downPressed || keyH.rightPressed) {

      // ROTATION
      if(keyH.leftPressed || keyH.rightPressed) {
        double prevAngle = angle; // angle before the transformation
        // keyboard inputs
        if(keyH.leftPressed)
          angle -= speed;
        if(keyH.rightPressed)
          angle += speed;
        double angleToRotate = prevAngle - angle; // angle difference to adjust between then and now
        at.rotate(Math.toRadians(angleToRotate), x+gp.tileSize/2, y+gp.tileSize/2); // do the rotation at the right spot
      }
      // TRANSLATION
      if(keyH.upPressed || keyH.downPressed) {

        int nextY = y; // keep new y in a var before updating the real y: care for collisions
        // keyboard inputs
        if(keyH.upPressed)
          nextY += speed;
        if(keyH.downPressed)
          nextY -= speed;

        // check collisions
        boolean collision = false;
        // got this sh*t out of the docs to find the positions in the reference frame
        double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
        double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();
        for(int i = nextY - gp.tileSize/2; i <= nextY + gp.tileSize/2; i++) {
          for(int j = x - gp.tileSize/2; j <= x + gp.tileSize/2; j++) {
            int xc = j + gp.tileSize/2, yc = i + gp.tileSize/2; // position of the center of the tank
            int nextX0 = (int)(m00 * xc + m01 * yc + m02);
            // The next line is hell. I hate this line. I wish it was never born and hope it'll die soon
            int nextY0 = (int)(m10 * yc + m11 * xc + m12); // you motherf*cker I hate u and you were adopted
            int xGrid, yGrid; // pos inside the map grid
            xGrid = (int)(nextX0 / gp.tileSize);
            yGrid = (int)(nextY0 / gp.tileSize);
            // collision with window bounds
            if(nextY0 < 0 || yGrid >= gp.currentMap.tiles.length || nextX0 < 0 || xGrid >= gp.currentMap.tiles[yGrid].length)
              collision = true;
            else if(gp.currentMap.tiles[yGrid][xGrid].collision) collision = true; // collision with tiles
          }
        }

        if(!collision) y = nextY; // update only if there's no collision
      }
    }
  }

  public void draw(Graphics2D g2) {

    g2.setTransform(at); // askip this function is not supposed to be used this way but it works
    g2.drawImage(sprite, x, y, gp.tileSize, gp.tileSize, null);

  }
}
