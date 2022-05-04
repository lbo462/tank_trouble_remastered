import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Color;

// Super funny tank that has a "super" capacity : create slow zone
public class TankKitty extends TankSuper{

    public ArrayList<BulletKitty> bulletsKitty = new ArrayList<BulletKitty>();

    public TankKitty(int number, int x, int y, GamePanel gp){
      super(number, x, y, gp.im.kitty, gp.im.deadKitty, gp, 100, 1);
    }

    @Override
    public void reset(int x, int y) {
      super.reset(x, y);
      this.bulletsKitty = new ArrayList<BulletKitty>(); // empty bulletsKitty
    }

    @Override
    public void update(){
      super.update();
      if(this.capacityActive && bulletsKitty.size() < 5 && System.currentTimeMillis() - lastShot > 100) {
        shootKittys(); // shoot kittys whenever the capacity is active
        lastShot = System.currentTimeMillis();
      }
    }

    @Override
    public void draw(Graphics2D g2) {
      /* draw the BulletsKitty */
      for(int i = 0; i < bulletsKitty.size(); i++) bulletsKitty.get(i).draw(g2);

      // draw number of BulletsKitty remaining
      g2.setColor(Color.PINK);
      int rectWidth = gp.tileSize / 5;
      for(int i = 0; i < 5-bulletsKitty.size(); i ++) {
        g2.setColor(Color.PINK);
        g2.fillRect(getX()-width/2+i*rectWidth, getY()-height, rectWidth-2, height / 4);
        g2.setColor(Color.BLACK);
        g2.drawRect(getX()-width/2+i*rectWidth, getY()-height, rectWidth-2, height / 4);
      }

      super.draw(g2);
    }

    @Override
    public void updateBullets(){
      super.updateBullets();

      /* update BulletsKitty */
      for(int i = bulletsKitty.size()-1; i >= 0; i--) {
        BulletKitty current = bulletsKitty.get(i);
        current.update();
        if(current.dead || (bulletsKitty.size() > 5 && !current.exploded)){
          bulletsKitty.remove(i);
        }
      }
    }

    // add one BulletsKitty
    public void shootKittys() {
      bulletsKitty.add(new BulletKitty(getX()-5, getY()-5, this.angle, gp));
      gp.s.pew.setFramePosition(0);
      gp.s.pew.start();
    }
}
