import java.awt.Graphics2D;
import java.awt.AlphaComposite;

// Super funny tank that has a "super" capacity : go through walls for a little time (when A is pressed)
public class Tank_Phantom extends Tank_Super{
    public Tank_Phantom(int number, int x, int y, GamePanel gp, KeyHandler keyH){
        super(number, x, y, "phantom.png", gp, keyH,3000,20000);
    }

    @Override
    public void update(){
        super.update();
        if(this.capacityActive)
            collisionWithTiles = false;
        else
            collisionWithTiles = true;
    }

    @Override
    public void draw(Graphics2D g2) {
      // transparency
      AlphaComposite alcom;
      if(this.capacityActive)
        alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
      else
        alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
      g2.setComposite(alcom);

      super.draw(g2);
      alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
      g2.setComposite(alcom);
    }
}
