// Power up that reset the cooldown of the capacities of the super tanks
public class PUResetCoolDown extends PowerUp{

    public PUResetCoolDown(GamePanel gPanel, int xCoord, int yCoord) {
        super(gPanel, xCoord, yCoord, gPanel.im.resetCooldown);
    }

    @Override
    public void activateEffect(Tank player) {
        if(player instanceof TankSuper){
            TankSuper superPlayer = (TankSuper)player;
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
