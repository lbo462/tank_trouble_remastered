import java.awt.Graphics2D;

// This class is the effect of the power up "speed up" (which correspond to the PUSpeedUp class)

public class EffSpeedBoost extends Effect {
  public EffSpeedBoost(Tank player) {
    super(player, 10000);
    super.setSprite(im.moreSpeed);
    t.maxSpeed += 1;
  }

  @Override
  public void update() {
    super.update();
    if(this.dead) t.maxSpeed -= 1;
  }

  @Override
  public void draw(int x, int y, int size, Graphics2D g2) {
    g2.drawImage(this.sprite, x, y-(int)(0.65*size), size, (int)(size*1.65), null);
  }
}
