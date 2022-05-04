//This class is used when TankTiTank uses its a special ability and shoots at the same time (bullets are bigger)

public class BulletBig extends Bullet {

  public BulletBig(int x, int y, double direction, GamePanel gp) {
    super(x, y, direction, gp.im.bullet, gp);
    this.width *= 2; // make the bullet twice bigger
    this.height *= 2;
    this.lifeTime = 600; // shorten its life
    this.speed = 8; // make the bullet twice faster
  }

  @Override
  public void update() {
    super.update();
    gp.particles.add(new ParticleDust(getX()+5, getY()+5, gp.im.dust)); // adding some dust
  }

  @Override
  public void collision() {
      UoDcollision = false; LoRCollision = false;

      // rotation matrix coeffs
      double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
      double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();
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
        if(currentTile.collision) currentTile.destroy(); // remove collisions
      }
      if(UoDcollision && LoRCollision) {
        // If the bullet hits a corner, make turn around
        angle += 180;
        at.rotate(Math.toRadians(180), x+(int)(width/2), y+(int)(height/2));
      }
      else if(UoDcollision) { // same sin opposite cos, Snell-Descarte
        angle = 180 - angle;
        at.rotate(Math.toRadians(180 - 2*this.angle), x+(int)(width/2), y+(int)(height/2));
      } else if(LoRCollision) { // same cos opposite sin
        angle *= -1;
        at.rotate(Math.toRadians(-2*this.angle), x+(int)(width/2), y+(int)(height/2));
      }
  }
}
