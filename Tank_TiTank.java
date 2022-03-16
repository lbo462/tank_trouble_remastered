import java.awt.Graphics2D;

public class Tank_TiTank extends Tank_Super {
  public Tank_TiTank(int number, int x, int y, GamePanel gp, KeyHandler keyH){
      super(number, x, y, "TiTank.png", gp, keyH,2000,20000);
  }

  @Override
  public void update(){
      super.update();
      if(this.capacityActivated) {
        width = gp.tileSize*2;
        height = gp.tileSize*2;
      }
      else {
        width = gp.tileSize;
        height = gp.tileSize;
      }
  }

  @Override
  // remove the wall if there's a collision
  public void collision() {
    super.collision();
    if(capacityActivated) {
      double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
      double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();
      for(int i = nextY - (int)(height)/2; i <= nextY + (int)(height)/2; i += (int)(height/4)) {
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
            // remove collisions hence remove wall
            currentTile.collision = false;
            currentTile.up = false;
            currentTile.down = false;
            currentTile.right = false;
            currentTile.left = false;
          }
        }
      }
    }

  }
}
