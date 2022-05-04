import javax.swing.ImageIcon;

public class PUBullet extends PowerUp{

    public PUBullet(GamePanel gPanel, int xCoord, int yCoord) {
        super(gPanel, xCoord, yCoord, (new ImageIcon("assets/entities/powerup/bullet.png")).getImage());
    }

    @Override
    void activateEffect(Tank player) {
        player.bulletAmplifier++;
    }
    
}
