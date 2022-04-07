import java.awt.Graphics2D;
import java.awt.Image;

public abstract class PowerUp extends Entity{
    boolean isAlive;
    public int identifier;
    public final int powerUpSize = 10;

    public PowerUp(GamePanel gPanel, int xCoord, int yCoord, Image powerImage){
        this.gp = gPanel;
        this.x = xCoord;
        this.y = yCoord;
        this.sprite = powerImage;
        this.isAlive = true;
        this.width = powerUpSize;
        this.height = powerUpSize;
    }

    abstract void activateEffect(Tank player);

    @Override
    void update() {
        for(Tank player:gp.players){
            double distance = Math.sqrt((player.x-this.x)^2+(player.y-this.y)^2); //distance (2-norm) between tank and power-up
            if(distance<5){
                isAlive = false;
                activateEffect(player);
            }
        }
    }

    @Override
    void draw(Graphics2D g2) {
        g2.drawImage(sprite, x, y, (int)width, (int)height, null);
    }

}
