
public class Bullet_Big extends Bullet {

  public Bullet_Big(int x, int y, double direction, GamePanel gp) {
    super(x, y, direction, gp.im.bullet, gp);
    width *= 2;
    height *= 2;
    lifeTime = 1000;
    speed = 8;
  }

  @Override
  public void update() {
    super.update();
    gp.dust.add(new Dust(x, y, gp.im.dust));
  }

  @Override
  void collision() {
    nextX += width/2;
    nextY += height/2;
    // collision with window bounds
    if(nextY < gp.tileSize/2 || nextY >= gp.height - gp.tileSize/2) {}
    else if(nextX < gp.tileSize/2 || nextX >= gp.width - gp.tileSize/2) {}
    else { // collision with tiles
      // next pos on the grid
      int xGrid = (int)(nextX / gp.tileSize);
      int yGrid = (int)(nextY / gp.tileSize);

      if(gp.currentMap.tiles[yGrid][xGrid].collision) {
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
