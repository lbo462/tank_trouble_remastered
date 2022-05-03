import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Color;

// Super funny tank that has a "super" capacity : create slow zone
public class Tank_Kitty extends Tank_Super{

    public ArrayList<Bullet_Kitty> bulletsKitty = new ArrayList<Bullet_Kitty>();

    public Tank_Kitty(int number, int x, int y, GamePanel gp){
      super(number, x, y, gp.im.kitty, gp.im.deadKitty, gp, 100, 1);
    }

    @Override
    public void reset(int x, int y) {
      super.reset(x, y);
      this.bulletsKitty = new ArrayList<Bullet_Kitty>(); // empty bullets_kitty
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
      /* draw the bullets_kitty */
      for(int i = 0; i < bulletsKitty.size(); i++) bulletsKitty.get(i).draw(g2);

      // draw number of bullets_kitty remaining
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

      /* update bullets_kitty */
      for(int i = bulletsKitty.size()-1; i >= 0; i--) {
        Bullet_Kitty current = bulletsKitty.get(i);
        current.update();
        if(current.dead || (bulletsKitty.size() > 5 && !current.exploded)){
          bulletsKitty.remove(i);
        }
      }
    }

    // add one bullets_kitty
    public void shootKittys() {
      bulletsKitty.add(new Bullet_Kitty(getX()-5, getY()-5, this.angle, gp));
      gp.s.pew.setFramePosition(0);
      gp.s.pew.start();
    }
}
