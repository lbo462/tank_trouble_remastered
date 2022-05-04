
public class PUSpeedUp extends PowerUp{

    public PUSpeedUp(GamePanel gPanel, int xCoord, int yCoord) {
        super(gPanel, xCoord, yCoord, gPanel.im.speedUp);
    }

    @Override
    public void activateEffect(Tank player) {
        player.addEffect(new EffSpeedBoost(player));
        this.isAlive = false;
    }

}
