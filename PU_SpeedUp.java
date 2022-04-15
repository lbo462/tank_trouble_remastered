
public class PU_SpeedUp extends PowerUp{

    public PU_SpeedUp(GamePanel gPanel, int xCoord, int yCoord) {
        super(gPanel, xCoord, yCoord, gPanel.im.speedUp);
    }

    @Override
    void activateEffect(Tank player) {
        player.addEffect(new Eff_SpeedBoost(player));
        this.isAlive = false;
    }

}
