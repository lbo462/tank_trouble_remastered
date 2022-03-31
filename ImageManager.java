import javax.swing.ImageIcon;
import java.awt.Image;

public class ImageManager {

  Image background;
  Image dust;

  Image bullet;
  Image bulletKitty;
  Image explodedKitty;

  Image painTank;
  Image defaultExplosion;
  Image phantom;
  Image deadPhantom;
  Image kitty;
  Image deadKitty;
  Image titank;
  Image deadTitank;

  public ImageManager() {
    try {
      background = new ImageIcon(getClass().getResource("assets/background.gif")).getImage();
      dust = new ImageIcon(getClass().getResource("assets/entities/dust.png")).getImage();

      // bullets
      bullet = new ImageIcon(getClass().getResource("assets/entities/bullet/bullet.png")).getImage();
      bulletKitty = new ImageIcon(getClass().getResource("assets/entities/bullet/kitty.png")).getImage();
      explodedKitty = new ImageIcon(getClass().getResource("assets/entities/bullet/explodedKitty.png")).getImage();

      // tanks
      painTank = new ImageIcon(getClass().getResource("assets/entities/tank/painTank.png")).getImage();
      defaultExplosion = new ImageIcon(getClass().getResource("assets/entities/tank/defaultExplosion.gif")).getImage();

      phantom = new ImageIcon(getClass().getResource("assets/entities/tank/phantom.png")).getImage();
      deadPhantom = new ImageIcon(getClass().getResource("assets/entities/tank/defaultExplosion.gif")).getImage();

      kitty = new ImageIcon(getClass().getResource("assets/entities/tank/kittyTank.png")).getImage();
      deadKitty = new ImageIcon(getClass().getResource("assets/entities/tank/defaultExplosion.gif")).getImage();

      titank = new ImageIcon(getClass().getResource("assets/entities/tank/TiTank.png")).getImage();
      deadTitank = new ImageIcon(getClass().getResource("assets/entities/tank/defaultExplosion.gif")).getImage();

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
