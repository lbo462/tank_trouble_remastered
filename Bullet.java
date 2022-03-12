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

    int nextX = x+5 + (int)(speed * Math.sin(Math.toRadians(direction)));
    int nextY = y+5 + (int)(speed * Math.cos(Math.toRadians(direction)));

    boolean LoRCollision = false, UoDcollision = false; // Left or Right / Up or Down collisions

    // collision with window bounds
    if(nextY < gp.tileSize/2 || nextY >= gp.height - gp.tileSize/2) UoDcollision = true;
    else if(nextX < gp.tileSize/2 || nextX >= gp.width - gp.tileSize/2) LoRCollision = true;
    else { // collision with tiles
      // next pos on the grid
      int xGrid = (int)(nextX / gp.tileSize);
      int yGrid = (int)(nextY / gp.tileSize);

      if(gp.currentMap.tiles[yGrid][xGrid].collision) {
        Tile currentTile = gp.currentMap.tiles[yGrid][xGrid];
        int xTile = (int)(xGrid * gp.tileSize) + gp.tileSize/2;
        int yTile = (int)(yGrid * gp.tileSize) + gp.tileSize/2;
        int deltaX = nextX - xTile;
        int deltaY = nextY - yTile;

        // Magic piece of code, do not touch
        if(deltaX > 0)
          deltaX -= 5;
        if(deltaY > 0)
          deltaY -= 5;

        if(Math.abs(deltaX) < 5 && deltaY < 0) { // coming from top
          if(currentTile.up) LoRCollision = true;
          else if(currentTile.down) UoDcollision = true;
        }
        if(Math.abs(deltaX) < 5 && deltaY > 0) { // coming from bottom
          if(currentTile.down) LoRCollision = true;
          else if(currentTile.up) UoDcollision = true;
        }
        if(Math.abs(deltaY) < 5 && deltaX > 0) { //coming from right
          if(currentTile.right) UoDcollision = true;
          else if(currentTile.left) LoRCollision = true;
        }
        if(Math.abs(deltaY) < 5 && deltaX < 0) { //coming from left
          if(currentTile.left) UoDcollision = true;
          else if(currentTile.right) LoRCollision = true;
        }


      }

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
    }
    x += speed * Math.sin(Math.toRadians(direction));
    y += speed * Math.cos(Math.toRadians(direction));

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
    g2.drawImage(sprite, x-5, y-5, null);
  }
}
