import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

// Carries every kind of tiles and in one word : manage them. Kinda kool.
public class TileManager {

  GamePanel gp;
  Tile[] tile; // carries the tiles

  public TileManager(GamePanel gp) {
    this.gp = gp;
    tile = new Tile[10]; // number of different tiles
    getTileImage(); // set-up image for every tiles
  }

  public void getTileImage() {
    try {

      // set-up every kind of tile ... boring stuff
      tile[0] = new Tile();
      tile[0].image = ImageIO.read(getClass().getResourceAsStream("/assets/tiles/ground.png")); // load the image

      // copy+paste above stuff to add other tiles ...

    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  // draw tiles (useful)
  public void draw(Graphics2D g2) {
    int tileSize = 100;
    for(int i = 0; i*tileSize < gp.width ; i++) {
      for(int j = 0; j*tileSize < gp.height ; j++) {
        tile[0].draw(g2, i*tileSize, j*tileSize, tileSize, tileSize);
      }
    }

  }

}
