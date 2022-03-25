import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Color;

// Super funny tank that has a "super" capacity : create slow zone
public class Tank_Kitty extends Tank_Super{

    ArrayList<Bullet_Kitty> bulletsKitty = new ArrayList<Bullet_Kitty>();

    public Tank_Kitty(int number, int x, int y, GamePanel gp, KeyHandler keyH){
        super(number, x, y, "kittyTank.png", gp, keyH,100,1);
    }

    @Override
    public void update(){
        super.update();
        if(this.capacityActive && bulletsKitty.size() < 5)
            shoot();
    }

    @Override
    public void reset(int x, int y) {
      super.reset(x, y);
      bulletsKitty = new ArrayList<Bullet_Kitty>();
    }

    @Override
    public void deadBulletRemoval(){
      super.deadBulletRemoval();

      for(int i = bulletsKitty.size()-1; i >= 0; i--) {
        Bullet_Kitty current = bulletsKitty.get(i);
        current.update();
        if(current.dead || (bulletsKitty.size() > 5 && !current.exploded)){
          bulletsKitty.remove(i);
        }
      }
    }

    @Override
    public void shoot(){
      if(System.currentTimeMillis() - lastShot > 100) {
        if(shotPressed && !capacityActive) {
          bullets.add(new Bullet(getX(), getY(), this.angle, "bullet.png", gp));
          clipPew.stop();
          clipPew.play();
        } else if(capacityActive) {
          bulletsKitty.add(new Bullet_Kitty(getX(), getY(), this.angle, "kitty.png", "explodedKitty.png", gp));
          clipPew.stop();
          clipPew.play();
        }

        lastShot = System.currentTimeMillis();
      }
    }

    @Override
    public void draw(Graphics2D g2) {
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
}
