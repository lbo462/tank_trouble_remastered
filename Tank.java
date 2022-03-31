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
      double prevAngle = this.angle; // angle before the transformation
      // keyboard inputs
      if(leftPressed)
        this.angle += 3;
      if(rightPressed)
        this.angle -= 3;
      double angleToRotate = prevAngle - this.angle; // angle difference to adjust between then and now
      at.rotate(Math.toRadians(angleToRotate), x+(int)(width/2), y+(int)(height/2)); // do the rotation at the right spot
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
      speed *= 0.75;
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
    double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
    double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();
    for(int i = (int)(nextY - height/2); i <= nextY + (int)(height)/2; i += (int)(height/4)) {
      for(int j = x - (int)(width)/2; j <= x + (int)(width)/2; j += (int)(width/4)) {
        int xc = j + height/2, yc = i + width/2; // position of the center of the tank
        int nextX0 = (int)(m00 * xc + m01 * yc + m02);
        // The next line is hell. I hate this line. I wish it was never born and hope it'll die soon
        int nextY0 = (int)(m10 * yc + m11 * xc + m12); // you motherf*cker I hate u and you were adopted
        int xGrid, yGrid; // pos inside the map grid
        xGrid = (int)(nextX0 / gp.tileSize);
        yGrid = (int)(nextY0 / gp.tileSize);
        // collision with window bounds
        if(nextY0 < height/2 || nextY0 >= gp.height - height/2 || nextX0 < width/2 || nextX0 >= gp.width - width/2)
          collision = true;
        else if(gp.currentMap.tiles[yGrid][xGrid].collision && collisionWithTiles) { // collision with tile
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
  }

  @Override
  void updatePosition() {
    if(!collision) { // update only if there's no collision
      y = (int)nextY;
    }
  }
}
