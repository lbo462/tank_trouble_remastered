import java.awt.Graphics2D;
import java.awt.AlphaComposite;

// Super funny tank that has a "super" capacity : go through walls for a little time (when A is pressed)
public class Tank_Phantom extends Tank_Super{
    public Tank_Phantom(int number, int x, int y, String image, GamePanel gp, KeyHandler keyH){
        super(number, x, y, image, gp, keyH,3000,20000);
    }

    public void update(){
        this.keyPressed();
        this.capacityActivation();
        if(this.capacityActivated)
            collisionWithTiles = false;
        else
            collisionWithTiles = true;
        super.update();
    }

    @Override
    public void draw(Graphics2D g2) {
      // transparency
      AlphaComposite alcom;
      if(this.capacityActivated)
        alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
      else
        alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
      g2.setComposite(alcom);

      super.draw(g2);
      alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
      g2.setComposite(alcom);
    }
}
