import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Bullet extends Entity {

  double bornAt;
  double lifeTime = 5000; // time to live in ms
  public boolean dead = false;
  double direction;

  AffineTransform at = new AffineTransform();

  public Bullet(int x, int y, double direction,GamePanel gp) {

    this.x = x;
    this.y = y;
    this.gp = gp;
    this.direction = direction;
    this.speed = 4;
    bornAt = System.currentTimeMillis();

    try {
      sprite = ImageIO.read(getClass().getResourceAsStream("assets/entities/bullet.png")); // load the sprite sa mère
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void update() {

    int nextX = x + (int)(speed * Math.sin(Math.toRadians(direction)));
    int nextY = y + (int)(speed * Math.cos(Math.toRadians(direction)));

    // next pos on the grid
    int xGrid = (int)(nextX / gp.tileSize);
    int yGrid = (int)(nextY / gp.tileSize);

    boolean LoRCollision = false, UoDcollision = false; // Left or Right / Up or Down collisions

    if(nextY < 0 || yGrid >= gp.currentMap.tiles.length || nextX < 0 || xGrid >= gp.currentMap.tiles[yGrid].length) {
      if(nextY < 0 || yGrid >= gp.currentMap.tiles.length) UoDcollision = true;
      else if(nextX < 0 || xGrid >= gp.currentMap.tiles[yGrid].length) LoRCollision = true;
    }
    else if(gp.currentMap.tiles[yGrid][xGrid].collision) {
      // actual pos
      int axGrid = (int)(x / gp.tileSize);
      int ayGrid = (int)(y / gp.tileSize);
      if(axGrid != xGrid) LoRCollision = true;
      else if(ayGrid != yGrid) UoDcollision = true;
    }

    if(LoRCollision || UoDcollision) {
      if(UoDcollision) {
        // left or right
        // find the angle with same cos and opposite sin
        direction = 180 - direction;
      } else if(LoRCollision) {
        // up or down
        // find the angle with same sin and oppsite cos
        direction *= -1;
      }

      x += speed * Math.sin(Math.toRadians(direction));
      y += speed * Math.cos(Math.toRadians(direction));
    } else {
      x = nextX;
      y = nextY;
    }

    double currentTime = System.currentTimeMillis();

    // wait few ms and check if a player was hit
    if(currentTime - bornAt > 500) { // don't auto kill the shooter
      for(Tank t: gp.players) {

        for(int i = x - gp.tileSize/2; i < x + gp.tileSize/2; i++) {
          for(int j = y - gp.tileSize/2; j < y + gp.tileSize/2; j++) {
            if(i == t.getX() && j == t.getY()) {
              System.out.println(t.number);
              t.dead = true; // kill player
              this.dead = true; // kill this bullet
              break;
            }
          }
        }
      }
    }
    // check it is time to die
    if(currentTime - bornAt > lifeTime) dead = true;

  }

  public void draw(Graphics2D g2) {
    g2.drawImage(sprite, x, y, null);
  }
}
