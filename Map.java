import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Map {

  public GamePanel gp;
  public BufferedImage background;
  public Tile[][] tiles;
  public String mapNumber;


  public Map(GamePanel gp, String mapNumber) {
    this.gp = gp;
    this.mapNumber = mapNumber;
    this.tiles = new Tile[gp.nbYtiles][gp.nbXtiles];

    // load the background image
    try {
      background = ImageIO.read(getClass().getResourceAsStream("assets/maps/"+this.mapNumber+"/background.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    loadMap(); // load tiles
  }

  // load the tiles in tiles[][]
  public void loadMap() {
    try {
      // read map file
      InputStream is = getClass().getResourceAsStream("assets/maps/"+this.mapNumber+"/map");
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      // extract data from file
      for(int i = 0; i < gp.nbYtiles; i++) {
        String line = br.readLine(); // recovers sometings like 0 0 0 0 1 1 1 1 0 0 0 0 ...
        for(int j = 0; j < gp.nbXtiles; j++) {
          String numbers[] = line.split(" "); // split into ["0", "0", "0", "0", "1", "1", ...]
          tiles[i][j] = new Tile(gp, numbers[j]);
        }
      }

      br.close(); // close the buffer reader

    } catch(Exception e) {

    }
  }

  public void draw(Graphics2D g2) {
    // draw the background
    g2.drawImage(background, 0, 0, gp.width, gp.height, null);

    // draw the tiles
    for(int i = 0; i < gp.nbYtiles; i++) {
      for(int j = 0; j < gp.nbXtiles; j++) {
        tiles[i][j].draw(g2, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize);

      }
    }

  }
}
