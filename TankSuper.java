import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;

// This class creates a tank with special (additional) capacities
public class TankSuper extends Tank{
    public boolean capacityActive; // true if the capacity is in use
    public double capacityDuration; // how long does the capacity lasts
    public double capacityCooldown; // how long it takes to reuse the ability
    public boolean capacityButtonPressed; // true if the capacity button is presssed
    public double lastUse; // last use of the capacity
    public double activationTime; // time at which the capacity was activated

    // for drawing bar beneath player
    double timeRemaining; // time remaining from next possible capacity activation
    double pourcentage = 1; // corresponding pourcentage wrt cooldown


    public TankSuper(int number, int x, int y, Image image, Image deadImage, GamePanel gp, double duration, double cooldown) {
        super(number, x, y, image, deadImage, gp);
        this.capacityDuration = duration;
        this.capacityCooldown = cooldown;
        this.lastUse = System.currentTimeMillis() - this.capacityCooldown; // make the capacity directly usable
        this.activationTime = System.currentTimeMillis();
    }

    @Override
    public void reset(int x, int y){
      super.reset(x, y);
      /* reset capacity */
      this.pourcentage = 1;
      this.lastUse = System.currentTimeMillis() - this.capacityCooldown;
      this.capacityActive = false;
      this.activationTime = System.currentTimeMillis() - this.capacityCooldown;
    }

    @Override
    public void update() {
      this.keyPressed();

      /* Check if the capacity can be activated */
      double currentTime = System.currentTimeMillis();
      if(capacityButtonPressed && !capacityActive && capacityCooldown<currentTime-lastUse){ //checks if the cooldown is over
          capacityActive=true;
          activationTime = currentTime;
      } else if(capacityActive && capacityDuration<currentTime-activationTime) { // checks if the duration is over
         capacityActive=false;
         lastUse = currentTime;
      }

      /* Update pourcentage */
      if(this.capacityActive) {
        timeRemaining = (int)(this.capacityDuration-(currentTime-activationTime));
        pourcentage = timeRemaining / this.capacityDuration;
      } else {
        if(this.capacityCooldown>currentTime-lastUse) {
          timeRemaining = (int)(this.capacityCooldown-(currentTime-lastUse));
          pourcentage = 1 - timeRemaining / this.capacityCooldown;
        }
        else pourcentage = 1;
      }

      super.update();
    }

    @Override // adds a cool cooldown evolution bar
    public void draw(Graphics2D g2) {
      g2.setColor(Color.BLACK);
      g2.drawRect(getX()-width/2-1, getY()+height+1, width+1, height/8+1);
      if(this.capacityActive) g2.setColor(Color.RED);
      else g2.setColor(Color.BLUE);
      g2.fillRect(getX()-width/2, getY()+height+2, (int)(width * pourcentage), height/8);

      super.draw(g2);
    }

    @Override // adds capacity button
    public void keyPressed(){
        super.keyPressed();
        if(this.number == 1){
            this.capacityButtonPressed = keyH.a;
        }else if(this.number == 2){
            this.capacityButtonPressed = keyH.p;
        }
    }
}
