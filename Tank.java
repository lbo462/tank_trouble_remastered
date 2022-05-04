import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Image;

// TANK BOUM BOUM
public class Tank extends MovingEntity {
  public int number;
  public boolean collision; // check collision in update
  public boolean collisionWithTiles; // used to phantom
  public boolean slowed; // used for kitty
  public int score;
  public int numberOfShoots;
  public ArrayList<Bullet> bullets; // contains active bullets
  public ArrayList<Effect> effects; // effects of the tank
  public double lastShot; // time of the last shot
  public KeyHandler keyH;
  public Image deadSprite; // image displayed when the tank dies
  public boolean upPressed,downPressed,leftPressed,rightPressed,shotPressed;
  public double timeToSlow; // time to stay slowed
  public double timeSlowed; // time slowed started
  public double timeDied; // time at which tank died
  public double angularSpeed;

  public Tank(int number, int x, int y, Image image, Image deadImage, GamePanel gp) {
    this.gp = gp;
    this.keyH = gp.keyH;
    this.number = number;
    this.score = 0;
    this.numberOfShoots = 0;
    this.sprite = image;
    this.deadSprite = deadImage;

    reset(x, y); // initialise every variables
  }

  // initialize every variable
  public void reset(int x, int y) {
    //this.debug = true;
    this.x = x;
    this.y = y;
    this.timeToSlow = 5000; // duration of the slow
    this.increaseSpeed = 0.2; // acceleration
    this.speed = 0; // current speed
    this.angularSpeed = 3;
    this.maxSpeed = 3;
    this.width = gp.tileSize;
    this.height = gp.tileSize;
    this.angle = 0;
    this.dead = false;
    this.collisionWithTiles = true;
    this.slowed = false;
    this.dashing = true;
    this.dashedAt = System.currentTimeMillis() - 1000;
    this.at = new AffineTransform();
    this.bullets = new ArrayList<Bullet>();
    this.effects = new ArrayList<Effect>();
  }

  public void kill() {
    /* play a little boom */
    gp.s.explosionSound.setFramePosition(0);
    gp.s.explosionSound.start();

    this.dead = true; // kill player
    this.timeDied = System.currentTimeMillis(); // record its time of death
    gp.im.resetExplosions(); // flush images i.e. reset gifs animation
  }

  public void update() {
    double currentTime = System.currentTimeMillis();
    if(!dead) { // only update if alive
      if(slowed && currentTime-timeSlowed > timeToSlow) slow(false); // verify slow time
      if(dashing && currentTime-dashedAt > 1000 || speed <= maxSpeed) dashing = false; // verify dashing time
      // update effects
      for(int i = 0; i < effects.size(); i++) {
        effects.get(i).update();
        if(effects.get(i).dead) effects.remove(i); // remove trash
      }

      this.keyPressed(); // verify which keys are pressed
      if(leftPressed || rightPressed) { // ROTATION
        prevAngle = this.angle; // angle before the transformation
        if(leftPressed)
          this.angle += angularSpeed;
        if(rightPressed)
          this.angle -= angularSpeed;
        nextA = prevAngle - this.angle; // angle difference to adjust between then and now
        at.rotate(Math.toRadians(nextA), x+(int)(width/2), y+(int)(height/2)); // do the rotation at the right spot
      }
      /* ***** */
      nextY = y;
      if((upPressed || downPressed) && !dashing) { // TRANSLATION
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
      if(Math.abs(speed) > 0 && gp.frame % 20 == 0 && !collision && collisionWithTiles)
        for(int i = 0; i < 15; i++) gp.particles.add(new ParticleDust(getX(), getY(), gp.im.dust));

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
    for(Effect e: effects) e.draw(x, y, width, g2);
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

      for(int r=0; r<width/2; r++) {
        for(int a=0; a<=360; a++) {
          int i = x+width/2 + (int)(r * Math.cos(Math.toRadians(a)));
          int j = y+height/2 + (int)(r * Math.sin(Math.toRadians(a)));

          // next position of the pixels
          int nx = (int)(m00 * i + m01 * j + m02);
          int ny = (int)(m10 * j + m11 * i + m12);

          g2.setColor(Color.RED);
          g2.drawRect(nx, ny, 1, 1);
        }
      }
    }
  }

  // add an effect to the tank
  public void addEffect(Effect e) {
    effects.add(e);
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
    Bullet b = new Bullet(getX()-5, getY()-5, this.angle, gp.im.bullet, gp);
    bullets.add(b);
    this.numberOfShoots++;
    gp.s.pew.setFramePosition(0);
    gp.s.pew.start();
    lastShot = System.currentTimeMillis();
  }

  // Removing bullets when they stayed
  public void updateBullets(){
    int prevScore = score;
    for(int i = 0; i < bullets.size(); i++) {
      bullets.get(i).update();
      if(bullets.get(i).killed && !this.dead && prevScore == score) score++;
      else if(bullets.get(i).killed && this.dead) score--;
      if(bullets.get(i).killed || bullets.get(i).dead) bullets.remove(i);
    }
  }

  @Override
  public void collision() {
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

    if(collisionWithTiles && !collision) {
      for(int r=0; r<width/2; r++) {
        for(int a=0; a<=360; a++) {
          int i = x+width/2 + (int)(r * Math.cos(Math.toRadians(a)));
          int j = (int)(nextY+height/2 + r * Math.sin(Math.toRadians(a)));

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
    } else if(leftPressed || rightPressed) { // if the rotation creates a collision, invert the rotation
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
      upPressed = keyH.z;
      downPressed = keyH.s;
      leftPressed = keyH.q;
      rightPressed = keyH.d;
      shotPressed = keyH.space;
    } else if(number == 2) {
      upPressed = keyH.o;
      downPressed = keyH.l;
      leftPressed = keyH.k;
      rightPressed = keyH.m;
      shotPressed = keyH.enter;
    }

  }
}
