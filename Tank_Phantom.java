import java.awt.Graphics2D;
import java.awt.AlphaComposite;

// Super funny tank that has a "super" capacity : go through walls for a short period of time (when A is pressed)
public class Tank_Phantom extends Tank_Super{
  public Tank_Phantom(int number, int x, int y, GamePanel gp){
      super(number, x, y, gp.im.phantom, gp.im.deadPhantom, gp,3000,20000);
  }

  @Override
  public void update(){
    super.update();
    /* deactivate collision with tiles when capacity is active */
    if(this.capacityActive)
      collisionWithTiles = false;
    else
      collisionWithTiles = true;
  }

  @Override // make it transparent under capacity
  public void draw(Graphics2D g2) {
    if(this.capacityActive) {
      AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f); // transparency
      g2.setComposite(alcom);
      super.draw(g2);
      /* reset transparency */
      alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
      g2.setComposite(alcom);
    } else super.draw(g2);
  }
}
