import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Bullet extends MovingEntity {

  double bornAt;
  double lifeTime = 5000; // LifeTime of a bullet in ms
  public boolean dead = false;

  AffineTransform at = new AffineTransform();
  private boolean UoDcollision;// Up or Down collisions
  private boolean LoRCollision;// Left or Right collisions

  public Bullet(int x, int y, double direction, String image, GamePanel gp) {
    this.x = x;
    this.y = y;
    this.gp = gp;
    this.angle = direction;
    this.speed = 4;

    bornAt = System.currentTimeMillis();

    try {
      sprite = ImageIO.read(getClass().getResourceAsStream("assets/entities/bullet/"+image)); // load the sprite
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.width = sprite.getWidth();
    this.height = sprite.getHeight();
  }

  public void update() {
    this.updateNextPosition();
    this.collision();
    this.didItTouch();
    this.deadOrAlive();
    this.updatePosition();

  }

  public void draw(Graphics2D g2) {
    g2.drawImage(sprite, (int)(x-this.width), (int)(y-this.height), null);
  }

  @Override
  void collision() {
    nextX += width/2;
    nextY += height/2;
    LoRCollision = false;
    UoDcollision = false;
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
          deltaX -= width/2;
        if(deltaY > 0)
          deltaY -= height/2;

        if(Math.abs(deltaX) < width/2 && deltaY < 0) { // coming from top
          if(currentTile.up) LoRCollision = true;
          else if(currentTile.down) UoDcollision = true;
        }
        if(Math.abs(deltaX) < width/2 && deltaY > 0) { // coming from bottom
          if(currentTile.down) LoRCollision = true;
          else if(currentTile.up) UoDcollision = true;
        }
        if(Math.abs(deltaY) < height/2 && deltaX > 0) { //coming from right
          if(currentTile.right) UoDcollision = true;
          else if(currentTile.left) LoRCollision = true;
        }
        if(Math.abs(deltaY) < height/2 && deltaX < 0) { //coming from left
          if(currentTile.left) UoDcollision = true;
          else if(currentTile.right) LoRCollision = true;
        }

      }

    }
    if(UoDcollision) {
      // left or right
      // find the angle with same cos and opposite sin
      angle = 180 - angle;
    } else if(LoRCollision) {
      // up or down
      // find the angle with same sin and oppsite cos
      angle *= -1;
    }
  }
  // wait few ms and check if a player was hit
  public void didItTouch(){
    double currentTime = System.currentTimeMillis();
    if(currentTime - bornAt > 500) { // don't auto kill the shooter
      for(Tank t: gp.players) {

        for(int i = x - gp.tileSize/2; i < x + gp.tileSize/2; i++) {
          for(int j = y - gp.tileSize/2; j < y + gp.tileSize/2; j++) {
            if(i == t.getX() && j == t.getY()) {
              t.dead = true; // kill player
              this.dead = true; // kill this bullet
              break;
            }
          }
        }
      }
    }
  }

  // check it is time to die
  public void deadOrAlive(){
    double currentTime = System.currentTimeMillis();
    if(currentTime - bornAt > lifeTime) dead = true;
  }

  public void updatePosition(){
    x += speed * Math.sin(Math.toRadians(angle));
    y += speed * Math.cos(Math.toRadians(angle));
  }

}
