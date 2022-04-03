public class Tank_TiTank extends Tank_Super {

  public boolean activated; // was the capacity activated?

  public Tank_TiTank(int number, int x, int y, GamePanel gp){
      super(number, x, y, gp.im.titank, gp.im.deadTitank, gp, 2500, 500);
      this.activated = false;
  }

  @Override
  public void update(){
    if(this.capacityActive && !activated) {
      activate(); // try to scale the tank
      this.collision(); // check if the tank is going to be stuck inside a boundary
      if(collision) deactivate(); // if a collsion will be caused by the scale, do not scale
    } else if(!this.capacityActive && activated)
      deactivate(); // scale down when capacity is over
    super.update();
  }

  // activate capacity
  void activate() {
    width = gp.tileSize*3;
    height = gp.tileSize*3;
    x -= gp.tileSize;
    maxSpeed = 5;
    activated = true;
  }
  // deactivate capacity
  void deactivate() {
    width = gp.tileSize;
    height = gp.tileSize;
    x += gp.tileSize;
    maxSpeed = 3;
    activated = false;
  }

  @Override // eventually shoot big bullets
  public void shoot(){
    if(shotPressed && System.currentTimeMillis() - lastShot > 100) {
      Bullet b;
      if(capacityActive) {
        b = new Bullet_Big(getX()-10, getY()-10, this.angle, gp); // create big bullet
        speed -= 10; // un peu de recul pour nerf
        gp.s.grosPew.stop();
        gp.s.grosPew.play();
      } else {
        b = new Bullet(getX()-5, getY()-5, this.angle, gp.im.bullet, gp); // normal bullet
        gp.s.pew.stop();
        gp.s.pew.play();
      }
      bullets.add(b);
      lastShot = System.currentTimeMillis();
    }
  }

  @Override // remove the wall if there's a collision
  public void collision() {
    super.collision();
    if(capacityActive && activated) {
        collision = false;
        // rotation matrix coeffs
        double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
        double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();

        Tile currentTile = gp.currentMap.tiles[0][0]; // tile with which we should check collision, initialise with random stuff
        int w = gp.tileSize-1; // true size of a tile

        for(int i=x; i<=x+width; i+=w/2) {
          for(int j=(int)nextY; j<=(int)nextY+height; j+=w/2) {
            // next position of the tank
            int nx = (int)(m00 * i + m01 * j + m02);
            int ny = (int)(m10 * j + m11 * i + m12);

            // first check collision with outside bounds of the map
            if(ny < 0 || ny >= gp.height || nx < 0 || nx >= gp.width)
              collision = true;
            else if(collisionWithTiles) {
              // find the tile to check collision
              int xCorner = nx;
              int yCorner = ny; // position of the 4 corners of the tank
              // position of the tile where the corner sits
              int xGrid = (int)(xCorner / gp.tileSize); int yGrid = (int)(yCorner / gp.tileSize);
              currentTile = gp.currentMap.tiles[yGrid][xGrid]; // retrieve the tile
              if(debug) currentTile.debug = true;
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
                  ) {
                    currentTile.destroy();
                  }
              }
          }
        }
      }
    }
  }
}
