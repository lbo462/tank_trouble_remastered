import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Image;

// TANK BOUM BOUM
public class Tank extends MovingEntity {

  boolean debug = false;

  int number;
  int width, height;
  double lastShot; // time of the last shot
  GamePanel gp;
  KeyHandler keyH;

  AffineTransform at; // enable rotation
  Image deadSprite; // image displayed when the tank dies

  boolean dead;
  boolean collision; // check collision in update
  boolean collisionWithTiles; // used to phantom
  boolean slowed; // used for kitty
  boolean upPressed,downPressed,leftPressed,rightPressed,shotPressed;
  int score;

  double timeToSlow; // time to stay slowed
  double timeSlowed; // time slowed started
  double timeDied; // time at which tank died

  ArrayList<Bullet> bullets; // contains active bullets

  public Tank(int number, int x, int y, Image image, Image deadImage, GamePanel gp, KeyHandler keyH) {

    this.gp = gp;
    this.keyH = keyH;
    this.number = number;
    this.score = 0;
    this.sprite = image;
    this.deadSprite = deadImage;

    reset(x, y); // initialise every variables
  }

  // reset every variables
  public void reset(int x, int y) {
    dead = false;
    collisionWithTiles = true;
    slowed = false;
    timeToSlow = 5000;
    this.x = x;
    this.y = y;
    this.increaseSpeed = 0.2;
    this.speed = 0;
    this.maxSpeed = 3;
    this.angle = 0;
    this.width = gp.tileSize;
    this.height = gp.tileSize;
    this.isMoving = false;
    at = new AffineTransform();
    bullets = new ArrayList<Bullet>();
  }

  public void update() {
    double currentTime = System.currentTimeMillis();
    if(!dead) {
      if(slowed && currentTime-timeSlowed > timeToSlow) {
        slow(false);
      }
      this.keyPressed();
      this.rotation();
      this.translation();
      this.collision();
      if(Math.abs(speed) > 0 && gp.frame % 20 == 0 && !collision) {
        for(int i = 0; i < 15; i++)
        gp.dust.add(new Dust(getX(), getY(), gp.im.dust));
      }
      this.updatePosition();
      this.shoot();
      this.deadBulletRemoval();
    }
  }

  public void slow(boolean shouldSlow) {
    if(shouldSlow) {
      if(!slowed) maxSpeed /= 2;
      slowed = true;
      timeSlowed = System.currentTimeMillis();
    } else if(slowed) {
      slowed = false;
      maxSpeed *= 2;
    }

  }

  // Drawing the current picture
  public void draw(Graphics2D g2) {
    AlphaComposite originalAlcom = (AlphaComposite)g2.getComposite();
    for(Bullet b: bullets) b.draw(g2);

    AffineTransform saveAt = g2.getTransform();
    g2.transform(at);
    g2.setComposite(originalAlcom);
    g2.drawImage(sprite, x, y, width, height, null);
    if(dead) g2.drawImage(deadSprite, x-width, y-height, 3*width, 3*height, null);
    if(slowed && !dead) {
      AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
      g2.setComposite(alcom);

      g2.setColor(Color.RED);
      g2.fillOval(x+5, y+5, width-10, height-10);

      g2.setColor(Color.BLACK);
      alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
      g2.setComposite(alcom);
    }

    g2.setTransform(saveAt);

    if(debug) { // display hitbox
      // rotation matrix coeffs
      double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
      double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();

      for(int i=x; i<=x+width; i++) {
        for(int j=(int)nextY; j<=(int)nextY+height; j++) {
          // next position of the tank
          int nx = (int)(m00 * i + m01 * j + m02);
          int ny = (int)(m10 * j + m11 * i + m12);
          g2.setColor(Color.RED);
          g2.drawRect(nx, ny, 1, 1);
        }
      }
    }
  }

  // get the position (X, Y) in the reference frame, reverting the rotating matrix at(Affine Transform)
  public int getX() {
    double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
    return (int)(m00 * (x+width/2) + m01 * (y+height/2) + m02);
  }

  public int getY() {
    double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();
    return (int)(m10 * (y+height/2) + m11 * (x+width/2) + m12);
  }

  // ROTATION
  public void rotation(){
    if(leftPressed || rightPressed) {
      prevAngle = this.angle; // angle before the transformation
      // keyboard inputs
      if(leftPressed)
        this.angle += 3;
      if(rightPressed)
        this.angle -= 3;
      nextA = prevAngle - this.angle; // angle difference to adjust between then and now
      at.rotate(Math.toRadians(nextA), x+(int)(width/2), y+(int)(height/2)); // do the rotation at the right spot
    }
  }

  // TRANSLATION
  public void translation(){
    nextY = y; // keep new y in a var before updating the real y: care for collisions
    if(upPressed || downPressed) {
      // keyboard inputs
      if(upPressed)
        speed += increaseSpeed;
      if(downPressed)
        speed -= increaseSpeed;
      if(Math.abs(speed) > maxSpeed) speed = speed/Math.abs(speed) * maxSpeed;
    } else {
      speed *= 0.9;
      if(Math.abs(speed) < 0.1) speed = 0;
    }
    nextY += (int)speed;
  }

  public void shoot(){
    if(shotPressed && System.currentTimeMillis() - lastShot > 100) {
      bullets.add(new Bullet(getX(), getY(), this.angle, gp.im.bullet, gp));
      gp.s.pew.stop();
      gp.s.pew.play();
      lastShot = System.currentTimeMillis();
    }
  }

  // Removing bullets when they stayed
  public void deadBulletRemoval(){
    for(int i = 0; i < bullets.size(); i++) {
      bullets.get(i).update();
      if(this.dead) {
        score--; // -1 if the player killed himself
        break;
      }
      else {
        for(Tank t: gp.players) { // check if someonelse died in the process
          if(t.dead) {
            score++;
            break;
          }
        }
      }
      if(bullets.get(i).dead){
        bullets.remove(i);
      }
    }
  }

  public void keyPressed() {
    upPressed = false;
    downPressed = false;
    leftPressed = false;
    rightPressed = false;
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

  }

  public void collision() {
    collision = false;

    // rotation matrix coeffs
    double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
    double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();

    Tile currentTile = gp.currentMap.tiles[0][0]; // tile with which we should check collision, initialise with random stuff
    int w = gp.tileSize-1; // true size of a tile

    for(int i=x; i<=x+width; i++) {
      for(int j=(int)nextY; j<=(int)nextY+height; j++) {
        // next position of the tank
        int nx = (int)(m00 * i + m01 * j + m02);
        int ny = (int)(m10 * j + m11 * i + m12);

        // first check collision with outside bounds of the map
        if(ny < height/2 || ny >= gp.height - height/2 || nx < width/2 || nx >= gp.width - width/2)
          collision = true;
        else if(collisionWithTiles) {
          // find the tile to check collision
          if((i==x && j==(int)nextY) || (i==x+width && j==(int)nextY) || (i==x && j==(int)nextY+height) || (i==x+width && j==(int)nextY+height)) {
            int xCorner = nx;
            int yCorner = ny; // position of the 4 corners of the tank
            // position of the tile where the corner sits
            int xGrid = (int)(xCorner / gp.tileSize); int yGrid = (int)(yCorner / gp.tileSize);
            currentTile = gp.currentMap.tiles[yGrid][xGrid]; // retrieve the tile
            if(debug) currentTile.debug = true;
          }
          // check if the hitbox hits the tile somewhere
          if(currentTile.collision) {
            // I used the same coeffs that I used for drawing the tile
            if((currentTile.up
              && nx > currentTile.x+1+3*w/8 && nx < currentTile.x+1+3*w/8 + w/4
              && ny > currentTile.y+1 && ny < currentTile.y+1 + w/2+1)
              || (currentTile.down
              && nx > currentTile.x+1+3*w/8 && nx < currentTile.x+1+3*w/8 + w/4
              && ny > currentTile.y+1+w/2 && ny < currentTile.y+1+w/2 + w/2+1)
              || (currentTile.right
              && nx > currentTile.x+1+w/2 && nx < currentTile.x+1+w/2 + w/2+1
              && ny > currentTile.y+1+3*w/8 && ny < currentTile.y+1+3*w/8 + w/4)
              || (currentTile.left
              && nx > currentTile.x+1 && nx < currentTile.x+1 + w/2+1
              && ny > currentTile.y+1+3*w/8 && ny < currentTile.y+1+3*w/8 + w/4)
              )
                collision = true;
          }
        }
      }
    }
  }

  @Override
  void updatePosition() {
    if(!collision) { // update only if there's no collision
      y = (int)nextY;
    } else if(leftPressed || rightPressed) {
      this.angle = prevAngle;
      at.rotate(Math.toRadians(-nextA), x+(int)(width/2), y+(int)(height/2)); // cancel the rotation
    }
  }
}
