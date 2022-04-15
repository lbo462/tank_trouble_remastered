import java.awt.Graphics2D;

public class Eff_SpeedBoost extends Effect {
  public Eff_SpeedBoost(Tank player) {
    super(player, 10000);
    super.setSprite(im.moreSpeed);
    t.maxSpeed += 1;
  }

  @Override
  void update() {
    super.update();
    if(this.dead) t.maxSpeed -= 1;
  }

  @Override
  void draw(int x, int y, int size, Graphics2D g2) {
    g2.drawImage(this.sprite, x, y, size, size, null);
  }
}
