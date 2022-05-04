import java.awt.Graphics2D;
import java.awt.Image;

//PowerUp class is the mother class of the reset cool down and speed up power up. The class is never called as it is, it is always its daughter that are called.

public abstract class PowerUp extends Entity{
    public boolean isAlive;

    public PowerUp(GamePanel gp, int x, int y, Image powerImage){
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.sprite = powerImage;
        this.isAlive = true;
        this.width = gp.tileSize;
        this.height = gp.tileSize;
    }

    abstract void activateEffect(Tank player);

    @Override
    public void update() {
        for(Tank t: gp.players){
          double distance = Math.sqrt(Math.pow((t.getX()-this.x-width/2), 2)+Math.pow((t.getY()-this.y-height/2),2)); //distance (2-norm) between tank and power-up
          if(distance < gp.tileSize){
              activateEffect(t);
          }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(sprite, x, y, (int)width, (int)height, null);
    }

}
