import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Tile {

  GamePanel gp;
  public BufferedImage image;
  public boolean collision; // Can we get though and can we eat the wall ?
  public String tileNum;

  public Tile(GamePanel gp, String tileNum) {
    this.gp = gp;
    this.tileNum = tileNum;

    // set-up which are the ones with collision depending on the tileNum
    if(tileNum.equals("1")) collision = true; // wall
    // maybe add new if needed ...

    if(!tileNum.equals("0")) { // the "0" tile is the nil tile, it won't be drawn and has no image
      try {
        image = ImageIO.read(getClass().getResourceAsStream("/assets/tiles/"+tileNum+".png")); // load the image

      } catch(IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void draw(Graphics2D g2, int x, int y, int width, int height) {
    if(!tileNum.equals("0"))
      g2.drawImage(image, x, y, width, height, null); // null is the observer. Useless for BufferedImage, don't worry about it
  }

  public String toString() {
    return "Tile " + tileNum + " / collision " + collision;
  }

}
