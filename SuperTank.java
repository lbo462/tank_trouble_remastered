public class SuperTank extends Tank{
    boolean capacityActivated;//true if the capacity is in use
    double capacityDuration;// how long does the capacity lasts
    double capacityCooldown;// how long it takes to reuse the ability
    double cooldownEnd;// Time at which the capacity will be reusable 
    double durationEnd;// Time at which the capacity will end
    boolean capacityButtonPressed;// true if the capacity button is presssed


    public SuperTank(int number, int x, int y, GamePanel gp, KeyHandler keyH, double duration, double cooldown) {
        super(number, x, y, gp, keyH);
        this.capacityDuration = duration;
        this.capacityCooldown = cooldown;
    }

    //Check if the capacity is activated and that it can be activated
    public void capacityActivation(){
        double currentTime = System.currentTimeMillis();
        if(this.cooldownEnd<currentTime){ //checks if the cooldown is over
            this.capacityActivated=true;
            this.cooldownEnd = currentTime + this.capacityCooldown;
            this.durationEnd = currentTime + this.capacityDuration;
        }
    }

    public void keyPressed(){
        super.keyPressed();
        if(this.number == 1){
            this.capacityButtonPressed = keyH.aPressed;
        }else if(this.number == 2){
            this.capacityButtonPressed = keyH.aPressed;
        }
    }


}
