import java.awt.Graphics2D;
import java.awt.AlphaComposite;

// Super funny tank that has a "super" capacity : go through walls for a short period of time (when "A" is pressed)
public class TankPhantom extends TankSuper{

  public boolean activated; // true if capacity activated

  public TankPhantom(int number, int x, int y, GamePanel gp){
      super(number, x, y, gp.im.phantom, gp.im.deadPhantom, gp, 3000, 1000);
  }

  @Override
  public void update(){
    super.update();
    /* deactivate collision with tiles when capacity is active */
    if(this.capacityActive && !activated) {
      collisionWithTiles = false;
      dash(10); // make a small dash, see MovingEntity
      activated = true;
    }
    else if(!this.capacityActive && activated) {
      collisionWithTiles = true;
      activated = false;
    }

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
