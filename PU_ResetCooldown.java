
public class PU_ResetCooldown extends PowerUp{

    public PU_ResetCooldown(GamePanel gPanel, int xCoord, int yCoord) {
        super(gPanel, xCoord, yCoord, gPanel.im.resetCooldown);
    }

    @Override
    void activateEffect(Tank player) {
        if(player instanceof Tank_Super){
            Tank_Super superPlayer = (Tank_Super)player;
            if(!superPlayer.capacityActive) {
              superPlayer.pourcentage = 1;
              superPlayer.lastUse = System.currentTimeMillis() - superPlayer.capacityCooldown;
              superPlayer.capacityActive = false;
              superPlayer.activationTime = System.currentTimeMillis() - superPlayer.capacityCooldown;
              this.isAlive = false;
            }
        }
    }

}
