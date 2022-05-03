import java.util.ArrayList;
import java.awt.Graphics2D;

public class Tank_Jiro extends Tank_Super {

  public ArrayList<Particle_Fire> fire = new ArrayList<Particle_Fire>();

  public Tank_Jiro(int number, int x, int y, GamePanel gp) {
    super(number, x, y, gp.im.tankjiro, gp.im.deadTankjiro, gp, 5000, 10000);
  }

  @Override
  public void reset(int x, int y) {
    super.reset(x, y);
    this.fire = new ArrayList<Particle_Fire>(); // empty fire
  }

  @Override
  public void update() {
    super.update();
    if(this.capacityActive && System.currentTimeMillis() - lastShot > 1) {
      fireThings(); // fire things whenever the capacity is active
    }
  }

  @Override
  public void draw(Graphics2D g2) {
    /* draw the fire */
    for(int i = 0; i < fire.size(); i++) fire.get(i).draw(g2);
    super.draw(g2);
  }

  // start fire
  public void fireThings() {
    fire.add(new Particle_Fire(getX(), getY(), angle, gp, this));
  }

  @Override
  public void updateBullets(){
    int prevScore = score;
    super.updateBullets();


    for(int i = 0; i < fire.size(); i++) {
      fire.get(i).update();
      if(fire.get(i).killed && !this.dead && prevScore == score) score++;
      else if(fire.get(i).killed && this.dead) score--;
      if(fire.get(i).killed || fire.get(i).dead) fire.remove(i);
    }
  }
}
