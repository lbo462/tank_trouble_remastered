import javax.swing.ImageIcon;

public class ResetCooldown extends PowerUp{

    public ResetCooldown(GamePanel gPanel, int xCoord, int yCoord) {
        super(gPanel, xCoord, yCoord,(new ImageIcon("assets/entities/powerup/clock.png")).getImage());
    }

    @Override
    void activateEffect(Tank player) {
        if(player instanceof Tank_Super){
            Tank_Super superPlayer = (Tank_Super)player;
            superPlayer.timeRemaining = 0;
            superPlayer.pourcentage = 0;
        }
    }
    
}
