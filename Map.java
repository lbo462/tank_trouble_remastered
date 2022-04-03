import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

// Everything that the map concerns is implemented in this class
public class Map {

  public GamePanel gp;
  public Tile[][] tiles;

  // Create a matrix of tiles, then fills it with a "labyrinth" randomly generated
  public Map(GamePanel gp) {
    this.gp = gp;
    this.tiles = new Tile[gp.nbYtiles][gp.nbXtiles];

    generateMap();
  }

  public Map(int number, GamePanel gp) {
    this.gp = gp;
    this.tiles = new Tile[gp.nbYtiles][gp.nbXtiles];


    int[][] tilesInt = new int[gp.nbYtiles][gp.nbXtiles];
    try {
      // read map file
      InputStream is = getClass().getResourceAsStream("/assets/maps/"+number+"/map"+number+".txt");
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      // extract data from file
      for(int i = 0; i <= gp.nbYtiles; i++) {
        String line = br.readLine(); // recovers sometings like 0 0 0 0 1 1 1 1 0 0 0 0 ...
        for(int j = 0; j <= gp.nbXtiles; j++) {
          //String numbers[] = line.split(" "); // split into ["0", "0", "0", "0", "1", "1", ...]
          tilesInt[i][j] = Integer.parseInt("1");
        }
        System.out.println(i);
      }

      br.close(); // close the buffer reader

    } catch(Exception e) {}
    mapIntToTiles(tilesInt); // transform number to Tiles

  }

  // This method will generate a random labyrinth
  // Then it will randomly erase some of the walls (such that the map is playable enough, otherwise it's too hard to play)
  public void generateMap() {
    // generate everything with int and finally translate to Tiles to save RAM
    int[][] tilesInt = new int[gp.nbYtiles][gp.nbXtiles];

    /* fill the map with wall such that it forms a shape like this :
      1 1 1 1 1 1 1 1 1 1 1 1 1
      1 0 1 0 1 0 1 0 1 0 1 0 1
      1 1 1 1 1 1 1 1 1 1 1 1 1
      1 0 1 0 1 0 1 0 1 0 1 0 1
      1 1 1 1 1 1 1 1 1 1 1 1 1
      1 0 1 0 1 0 1 0 1 0 1 0 1
      1 1 1 1 1 1 1 1 1 1 1 1 1
    */
    for(int i = 0; i < tilesInt.length; i++) {
      if(i % 2 == 0)
        for(int j = 0; j < tilesInt[i].length; j++) tilesInt[i][j] = 1;
      else
        for(int j = 0; j < tilesInt[i].length; j++) tilesInt[i][j] = (j+1) % 2;
    }

    // assign a different number to each empty cell.
    for(int i = 0; i < tilesInt.length; i++) {
      for(int j = 0; j < tilesInt[i].length; j++) {
        if(tilesInt[i][j] == 0)
          tilesInt[i][j] = j + i * gp.nbXtiles; // magic formula corresponding to 1D-index <3
          // with this formula, we're sure that every number is different
      }
    }

    // while every cell has not the same number, continue
    boolean finallyFinished = false;
    while(!finallyFinished) {

      // randomly choose a wall to destroy
      int xToKill = 1, yToKill = 1;
      while(tilesInt[yToKill][xToKill] != 1) {
        xToKill = (int)(Math.random() * (gp.nbXtiles-2)) + 1; // random x that can't be a wall
        // deduce a random y so that it's a wall
        if(xToKill % 2 == 0)
          yToKill = (int)(Math.random()*((gp.nbYtiles-1)/2)) * 2 + 1;
        else
          yToKill = (int)(Math.random()*((gp.nbYtiles-2)/2)) * 2 + 2;
      }

      // DESTROY THE WALL (wall cries in background)
      // and give the new neighbours the same number so that they can live in harmony

      int cell_1, cell_2;
      if(tilesInt[yToKill - 1][xToKill] == 1) { // one cell to the right and the other to the left
        cell_1 = tilesInt[yToKill][xToKill - 1];
        cell_2 = tilesInt[yToKill][xToKill + 1];
      }
      else { // one cell above, the other below
        cell_1 = tilesInt[yToKill - 1][xToKill];
        cell_2 = tilesInt[yToKill + 1][xToKill];
      }

      if(cell_1 != cell_2) { // don't break too much walls
        // update the values
        for(int i = 1; i < gp.nbYtiles - 1; i += 1) {
          for(int j = 1; j < gp.nbXtiles - 1; j += 1) {
            if(tilesInt[i][j] == cell_2) {
              tilesInt[i][j] = cell_1;
              tilesInt[yToKill][xToKill] = cell_1;
            }
          }
        }
      }

      // check if this iteration finally ended the process ...
      // i.e. every free cell has the same number : total harmony
      int harmonyValue = -1;
      finallyFinished = true;
      for(int i = 1; i < tilesInt.length; i++) {
        for(int j = 1; j < tilesInt[i].length; j++) {
          if(tilesInt[i][j] != 1) { // don't check walls
            if(harmonyValue != -1 && tilesInt[i][j] != harmonyValue) {
              finallyFinished = false;
              continue;
            }
            else if(harmonyValue == -1)
              harmonyValue = tilesInt[i][j];
          }
        }
      }
    }

    // randomly remove a certain amount of walls
    for(int i = 0; i < 120; i++) {
      int xToKill = 1, yToKill = 1;
      while(tilesInt[yToKill][xToKill] != 1) {
        xToKill = (int)(Math.random() * (gp.nbXtiles-2)) + 1; // random x that can't be a wall
        // deduce a random y so that it's a wall
        if(xToKill % 2 == 0)
          yToKill = (int)(Math.random()*((gp.nbYtiles-1)/2)) * 2 + 1;
        else
          yToKill = (int)(Math.random()*((gp.nbYtiles-2)/2)) * 2 + 2;
      }
      tilesInt[yToKill][xToKill] = 0;
    }

    // up right
    tilesInt[1]              [1]               = 0;
    tilesInt[1]              [2]               = 0;
    tilesInt[2]              [1]               = 0;
    tilesInt[2]              [2]               = 0;

    // down left
    tilesInt[gp.nbYtiles - 2][gp.nbXtiles - 2] = 0;
    tilesInt[gp.nbYtiles - 2][gp.nbXtiles - 3] = 0;
    tilesInt[gp.nbYtiles - 3][gp.nbXtiles - 2] = 0;
    tilesInt[gp.nbYtiles - 3][gp.nbXtiles - 3] = 0;

    // remove alone cells
    for(int i = 1; i < tilesInt.length - 1; i++) {
      for(int j = 1; j < tilesInt[i].length - 1; j++) {
        if(tilesInt[i+1][j]!=1 && tilesInt[i-1][j]!=1 && tilesInt[i][j+1]!=1 && tilesInt[i][j-1]!=1)
          tilesInt[i][j] = 0;
      }
    }

    // replace every value that is not a wall by 0 (empty tile) instead of the harmony value
    for(int i = 0; i < tilesInt.length; i++) {
      for(int j = 0; j < tilesInt[i].length; j++) {
        if(tilesInt[i][j] != 1) tilesInt[i][j] = 0;
      }
    }

    // finally transform the tilesInt[][] into actual Tiles
    mapIntToTiles(tilesInt);
  }

  public void mapIntToTiles(int[][] tilesInt) {
    for(int i = 0; i < tilesInt.length; i++) {
      for(int j = 0; j < tilesInt[i].length; j++) {
        int x = j * gp.tileSize;
        int y = i * gp.tileSize;
        boolean u = false, r = false, l = false, d = false;
        if(tilesInt[i][j] == 1 && i > 0 && i < tilesInt.length-1 && j > 0 && j < tilesInt[i].length-1) {
          // r = right, l = left, u = up, d = down st
          /* Represent the presence of tiles around this one
              x | u | x
              l | x | r
              x | d | x
          */

          if(tilesInt[i-1][j] == 1) u = true;
          if(tilesInt[i][j+1] == 1) r = true;
          if(tilesInt[i][j-1] == 1) l = true;
          if(tilesInt[i+1][j] == 1) d = true;
        }
        tiles[i][j] = new Tile(gp, x, y, u, d, r, l);
      }
    }
  }

  public void draw(Graphics2D g2) {
    // draw the tiles
    for(int i = 0; i < gp.nbYtiles; i++) {
      for(int j = 0; j < gp.nbXtiles; j++) {
        tiles[i][j].draw(g2);
      }
    }
  }
}
