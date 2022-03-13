import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public class PhantomTank extends SuperTank{
    public PhantomTank(int number, int x, int y, String image, GamePanel gp, KeyHandler keyH){
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
    }
}
