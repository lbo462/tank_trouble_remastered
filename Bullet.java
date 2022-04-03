import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Image;

public class Bullet extends MovingEntity {

  public boolean UoDcollision;// Up or Down collisions
  public boolean LoRCollision;// Left or Right collisions
  public Tile currentTile;
  public Vector currPos, lastPos;

  public Bullet(int x, int y, double direction, Image image, GamePanel gp) {
    this.x = x;
    this.y = y;
    this.angle = direction;
    this.sprite = image;
    this.gp = gp;

    this.width = 10;
    this.height = 10;
    this.speed = 4; // in pixels
    this.lifeTime = 5000; // in ms

    this.dead = false;
    this.at = new AffineTransform();
    at.rotate(Math.toRadians(-this.angle), x+(int)(width/2), y+(int)(height/2)); // make the inital rotation
    int xGrid = (int)(x / gp.tileSize); int yGrid = (int)(y / gp.tileSize);
    this.currentTile = gp.currentMap.tiles[yGrid][xGrid]; // retrieve the tile where the bullet stands
    this.currPos = new Vector(x, y);
    this.lastPos = currPos.copy();

    this.bornAt = System.currentTimeMillis();
  }

  public void update() {
    y += speed;
    currPos = new Vector(getX(), getY());
    this.collision(); // check wall bouncing
    this.collisionWithTanks(); // eventually destroy few tanks ;)
    lastPos = currPos.copy();

    // check if it time has gone by ...
    double currentTime = System.currentTimeMillis();
    if(currentTime - bornAt > lifeTime) dead = true;
  }

  public void draw(Graphics2D g2) {
    AffineTransform saveAt = g2.getTransform();
    g2.transform(at);
    g2.drawImage(sprite, x, y, (int)width, (int)height, null);
    g2.setTransform(saveAt);
  }

  @Override
  void collision() {
    UoDcollision = false; LoRCollision = false;

    // rotation matrix coeffs
    double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
    double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();

    int w = gp.tileSize-1; // true size of a tile

    // next position of the center of the bullet
    int nx = (int)(m00 * (x+width/2) + m01 * (y+height/2) + m02);
    int ny = (int)(m10 * (y+height/2) + m11 * (x+width/2) + m12);

    // first check collision with outside bounds of the map
    if(ny < height/2 || ny >= gp.height - height/2)
       UoDcollision = true;
    else if(nx < width/2 || nx >= gp.width - width/2)
       LoRCollision = true;
    else {
      /* find the tile to check collision */
      // position of the tile where the center sits
      int xGrid = (int)(nx / gp.tileSize); int yGrid = (int)(ny / gp.tileSize);
      currentTile = gp.currentMap.tiles[yGrid][xGrid]; // retrieve the tile where the bullet stands
      if(debug) currentTile.debug = true;
      // check if the hitbox hits the tile somewhere
      if(currentTile.collision) {
        // I used the same coeffs that I used for drawing the tile
        for(int i=(int)(nx-width/2); i<(int)(nx+width/2); i++) {
          for(int j=(int)(ny-height/2); j<(int)(ny+height/2); j++) {
            if(currentTile.up && i > currentTile.x+1+3*w/8 && i < currentTile.x+1+3*w/8 + w/4 && j > currentTile.y+1 && j < currentTile.y+1 + w/2+1)
              if(lastPos.x > currentTile.x+1+3*w/8 && lastPos.x < currentTile.x+1+3*w/8 + w/4) UoDcollision = true;
              else LoRCollision = true;

            if(currentTile.down && i > currentTile.x+1+3*w/8 && i < currentTile.x+1+3*w/8 + w/4 && j > currentTile.y+1+w/2 && j < currentTile.y+1+w/2 + w/2+1)
              if(lastPos.x > currentTile.x+1+3*w/8 && lastPos.x < currentTile.x+1+3*w/8 + w/4) UoDcollision = true;
              else LoRCollision = true;

            if(currentTile.right && i > currentTile.x+1+w/2 && i < currentTile.x+1+w/2 + w/2+1 && j > currentTile.y+1+3*w/8 && j < currentTile.y+1+3*w/8 + w/4)
              if(lastPos.y > currentTile.y+1+3*w/8 && lastPos.y < currentTile.y+1+3*w/8 + w/4) LoRCollision = true;
              else UoDcollision = true;

            if(currentTile.left && i > currentTile.x+1 && i < currentTile.x+1 + w/2+1 && j > currentTile.y+1+3*w/8 && j < currentTile.y+1+3*w/8 + w/4)
              if(lastPos.y > currentTile.y+1+3*w/8 && lastPos.y < currentTile.y+1+3*w/8 + w/4) LoRCollision = true;
              else UoDcollision = true;
          }
        }
      }
    }
    if(UoDcollision || LoRCollision) currentTile.life--;
    if(UoDcollision && LoRCollision) {
      // If hits a corner, make demi-tour
      angle += 180;
      at.rotate(Math.toRadians(180), x+(int)(width/2), y+(int)(height/2));
    }
    else if(UoDcollision) { // same sin opposite cos
      angle = 180 - angle;
      at.rotate(Math.toRadians(180 - 2*this.angle), x+(int)(width/2), y+(int)(height/2));
    } else if(LoRCollision) { // same cos opposite sin
      angle *= -1;
      at.rotate(Math.toRadians(-2*this.angle), x+(int)(width/2), y+(int)(height/2));
    }
  }

  // wait few ms and check if a player was hit
  public void collisionWithTanks(){
    double currentTime = System.currentTimeMillis();
    if(currentTime - bornAt > 300) { // don't auto kill the shooter
      for(Tank t: gp.players) { // for all tanks ...
        // check every pixel of the tank
        // rotation matrix coeffs of the tank
        double m00 = t.at.getScaleX(), m01 = t.at.getShearX(), m02 = t.at.getTranslateX();
        double m10 = t.at.getScaleY(), m11 = t.at.getShearY(), m12 = t.at.getTranslateY();
        for(int i=t.x; i<t.x+t.width; i++) {
          for(int j=t.y; j<t.y+t.height; j++) {
            // every pixel of the tank
            int xt = (int)(m00 * i + m01 * j + m02);
            int yt = (int)(m10 * j + m11 * i + m12);
            if(getX() == xt && getY() == yt && !t.dead) { // if the bullet center hits the tank somewhere
              t.dead = true; // kill player
              t.timeDied = currentTime; // record its time of death
              gp.im.resetExplosions(); // flush images i.e. reset gifs animation
              this.dead = true; // kill this bullet
              /* play a little boom */
              gp.s.explosionSound.stop();
              gp.s.explosionSound.play();
              break;
            }
          }
        }
      }
    }
  }
}
