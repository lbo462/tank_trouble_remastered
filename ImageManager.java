import javax.swing.ImageIcon;
import java.awt.Image;

public class ImageManager {

  public Image[] background;
  public Image dust, dustWall;

  public Image UoDfull, UoDcrack1, UoDcrack2, UoDcrack3;
  public Image LoRfull, LoRcrack1, LoRcrack2, LoRcrack3;

  public Image bullet;
  public Image bulletKitty;
  public Image explodedKitty;

  public Image painTank;
  public Image defaultExplosion;
  public Image phantom;
  public Image deadPhantom;
  public Image kitty;
  public Image deadKitty;
  public Image titank;
  public Image deadTitank;

  public ImageManager() {
    background = new Image[3];
    try {
      // default background
      background[0] = new ImageIcon(getClass().getResource("assets/maps/background.gif")).getImage();
      // custom maps background
      background[1] = new ImageIcon(getClass().getResource("assets/maps/1/background.gif")).getImage();
      background[2] = new ImageIcon(getClass().getResource("assets/maps/2/background.gif")).getImage();
      dust = new ImageIcon(getClass().getResource("assets/entities/dust.png")).getImage();
      dustWall = new ImageIcon(getClass().getResource("assets/entities/dustWall.gif")).getImage();

      // tiles
      UoDfull = new ImageIcon(getClass().getResource("assets/tiles/UoDfull.png")).getImage();
      UoDcrack1 = new ImageIcon(getClass().getResource("assets/tiles/UoDcrack1.png")).getImage();
      UoDcrack2 = new ImageIcon(getClass().getResource("assets/tiles/UoDcrack2.png")).getImage();
      UoDcrack3 = new ImageIcon(getClass().getResource("assets/tiles/UoDcrack3.png")).getImage();
      LoRfull = new ImageIcon(getClass().getResource("assets/tiles/LoRfull.png")).getImage();
      LoRcrack1 = new ImageIcon(getClass().getResource("assets/tiles/LoRcrack1.png")).getImage();
      LoRcrack2 = new ImageIcon(getClass().getResource("assets/tiles/LoRcrack2.png")).getImage();
      LoRcrack3 = new ImageIcon(getClass().getResource("assets/tiles/LoRcrack3.png")).getImage();

      // bullets
      bullet = new ImageIcon(getClass().getResource("assets/entities/bullet/bullet.png")).getImage();
      bulletKitty = new ImageIcon(getClass().getResource("assets/entities/bullet/kitty.png")).getImage();
      explodedKitty = new ImageIcon(getClass().getResource("assets/entities/bullet/explodedKitty.gif")).getImage();

      // tanks
      painTank = new ImageIcon(getClass().getResource("assets/entities/tank/painTank.png")).getImage();
      defaultExplosion = new ImageIcon(getClass().getResource("assets/entities/tank/defaultExplosion.gif")).getImage();

      phantom = new ImageIcon(getClass().getResource("assets/entities/tank/phantom.gif")).getImage();
      deadPhantom = new ImageIcon(getClass().getResource("assets/entities/tank/phantomExplosion.gif")).getImage();

      kitty = new ImageIcon(getClass().getResource("assets/entities/tank/kittyTank.gif")).getImage();
      deadKitty = new ImageIcon(getClass().getResource("assets/entities/tank/kittyExplosion.gif")).getImage();

      titank = new ImageIcon(getClass().getResource("assets/entities/tank/TiTank.gif")).getImage();
      deadTitank = new ImageIcon(getClass().getResource("assets/entities/tank/titankExplosion.gif")).getImage();

    }  catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void resetExplosions() {
    defaultExplosion.flush();
    deadPhantom.flush();
    deadKitty.flush();
    deadTitank.flush();
  }
}
