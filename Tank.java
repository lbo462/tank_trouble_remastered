import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

// TANK BOUM BOUM
public class Tank extends MovingEntity {
  public int number;
  public boolean collision; // check collision in update
  public boolean collisionWithTiles; // used to phantom
  public boolean slowed; // used for kitty
  public int score;
  public ArrayList<Bullet> bullets; // contains active bullets
  public double lastShot; // time of the last shot
  public KeyHandler keyH;
  public Image deadSprite; // image displayed when the tank dies
  public boolean upPressed,downPressed,leftPressed,rightPressed,shotPressed;
  public double timeToSlow; // time to stay slowed
  public double timeSlowed; // time slowed started
  public double timeDied; // time at which tank died

  public Tank(int number, int x, int y, Image image, Image deadImage, GamePanel gp) {
    this.gp = gp;
    this.keyH = gp.keyH;
    this.number = number;
    this.score = 0;
    this.sprite = image;
    this.deadSprite = deadImage;

    reset(x, y); // initialise every variables
  }

  // reset/initialise every variables
  public void reset(int x, int y) {
    this.x = x;
    this.y = y;
    this.timeToSlow = 5000; // duration of the slow
    this.increaseSpeed = 0.2; // acceleration
    this.speed = 0; // current speed
    this.maxSpeed = 3;
    this.width = gp.tileSize;
    this.height = gp.tileSize;
    this.angle = 0;
    this.dead = false;
    this.collisionWithTiles = true;
    this.slowed = false;
    this.at = new AffineTransform();
    this.bullets = new ArrayList<Bullet>();
  }

  public void update() {
    double currentTime = System.currentTimeMillis();
    if(!dead) { // only update if alive
      if(slowed && currentTime-timeSlowed > timeToSlow) slow(false); // verify slow time

      this.keyPressed(); // verify which keys are pressed
      if(leftPressed || rightPressed) { // ROTATE
        prevAngle = this.angle; // angle before the transformation
        if(leftPressed)
          this.angle += 3;
        if(rightPressed)
          this.angle -= 3;
        nextA = prevAngle - this.angle; // angle difference to adjust between then and now
        at.rotate(Math.toRadians(nextA), x+(int)(width/2), y+(int)(height/2)); // do the rotation at the right spot
      }
      /* ***** */
      nextY = y;
      if(upPressed || downPressed) { // TRANSLATE
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

      this.collision(); // check collision with tiles

      /* add some dust */
      if(Math.abs(speed) > 0 && gp.frame % 20 == 0 && !collision)
        for(int i = 0; i < 15; i++) gp.dust.add(new Dust(getX(), getY(), gp.im.dust));

      this.updatePosition(); // update as a function of collisions
      if(shotPressed && System.currentTimeMillis() - lastShot > 100) this.shoot();
      this.updateBullets();
    }
  }

  // Drawing the current picture
  public void draw(Graphics2D g2) {
    AlphaComposite originalAlcom = (AlphaComposite)g2.getComposite();
    for(Bullet b: bullets) b.draw(g2);

    AffineTransform saveAt = g2.getTransform();
    g2.transform(at);
    g2.setComposite(originalAlcom);
    g2.drawImage(sprite, x, y, width, height, gp);
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

    if(debug) { // display hitbox (only corners)
      // rotation matrix coeffs
      double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
      double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();

      ArrayList<Tile> toCheck = new ArrayList<Tile>(); // tiles with which we should check collision
      int w = gp.tileSize-1; // true size of a tile

      for(int i=x; i<=x+width; i+=width) {
        for(int j=(int)nextY; j<=(int)nextY+height; j+=height) {
          // next position of the pixels
          int nx = (int)(m00 * i + m01 * j + m02);
          int ny = (int)(m10 * j + m11 * i + m12);

          g2.setColor(Color.RED);
          g2.drawRect(nx, ny, 1, 1);
        }
      }
    }
  }

  public void slow(boolean shouldSlow) {
    if(shouldSlow) {
      if(!slowed) maxSpeed = 1;
      slowed = true;
      timeSlowed = System.currentTimeMillis();
    } else if(slowed) {
      slowed = false;
      maxSpeed = 3;
    }
  }

  public void shoot(){
    bullets.add(new Bullet(getX()-5, getY()-5, this.angle, gp.im.bullet, gp));
    gp.s.pew.stop();
    gp.s.pew.play();
    lastShot = System.currentTimeMillis();
  }

  // Removing bullets when they stayed
  public void updateBullets(){
    for(int i = 0; i < bullets.size(); i++) {
      bullets.get(i).update();
      if(this.dead) {
        score--; // -1 if the player killed himself
        break;
      } else {
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

  @Override
  void collision() {
    collision = false;

    // rotation matrix coeffs
    double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
    double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();

    ArrayList<Tile> toCheck = new ArrayList<Tile>(); // tiles with which we should check collision
    int w = gp.tileSize-1; // true size of a tile

    // search for the tiles to check
    for(int i=x; i<=x+width; i+=width) {
      for(int j=(int)nextY; j<=(int)nextY+height; j+=height) {
        // next position of the pixels
        int nx = (int)(m00 * i + m01 * j + m02);
        int ny = (int)(m10 * j + m11 * i + m12);

        // first check collision with outside bounds of the map
        if(ny < height/2 || ny >= gp.height - height/2 || nx < width/2 || nx >= gp.width - width/2)
          collision = true;
        else if(collisionWithTiles) {
          // find the tile to check collision
          int xCorner = nx;
          int yCorner = ny; // position of the 4 corners of the tank
          // position of the tile where the corner sits
          int xGrid = (int)(xCorner / gp.tileSize); int yGrid = (int)(yCorner / gp.tileSize);
          Tile currentTile = gp.currentMap.tiles[yGrid][xGrid]; // retrieve the tile
          if(debug) currentTile.debug = true;
          toCheck.add(currentTile);
        }
      }
    }

    // now check collision with these tiles
    if(collisionWithTiles && !collision) { // maybe the tank already collided with map boundaries
      for(int i=x; i<=x+width; i++) {
        for(int j=(int)nextY; j<=(int)nextY+height; j++) {
          // next position of the pixels
          int nx = (int)(m00 * i + m01 * j + m02);
          int ny = (int)(m10 * j + m11 * i + m12);

          for(Tile currentTile: toCheck) {
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
  }

  // update position checking collision
  public void updatePosition() {
    if(!collision) { // update y only if there's no collision
      y = (int)nextY;
    } else if(leftPressed || rightPressed) { // if the rotation creates a collsion, invert the rotation
      this.angle = prevAngle;
      at.rotate(Math.toRadians(-nextA), x+(int)(width/2), y+(int)(height/2)); // cancel the rotation
    }
  }


  // verify which keys are pressed depending on the player number
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
}
