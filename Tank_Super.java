import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;

// This class creates a tank with special (additional) capacities
public class Tank_Super extends Tank{
    boolean capacityActivated;//true if the capacity is in use
    double capacityDuration;// how long does the capacity lasts
    double capacityCooldown;// how long it takes to reuse the ability
    boolean capacityButtonPressed;// true if the capacity button is presssed
    double lastUse; // last use of the capacity
    double activationTime; // time at which the capacity was activated

    // for drawing bar beneath player
    double timeRemaining;
    double pourcentage = 1;


    public Tank_Super(int number, int x, int y, String image, GamePanel gp, KeyHandler keyH, double duration, double cooldown) {
        super(number, x, y, image, gp, keyH);
        this.capacityDuration = duration;
        this.capacityCooldown = cooldown;
        this.lastUse = System.currentTimeMillis() - this.capacityCooldown;
        this.activationTime = System.currentTimeMillis();
    }

    //Check if the capacity is activated and that it can be activated
    public void capacityActivation(){
        double currentTime = System.currentTimeMillis();
        if(this.capacityButtonPressed && !this.capacityActivated && this.capacityCooldown<currentTime-lastUse){ //checks if the cooldown is over
            this.capacityActivated=true;
            activationTime = currentTime;
        } else if(this.capacityActivated && this.capacityDuration<currentTime-activationTime) { // checks if the duration is over
           this.capacityActivated=false;
           lastUse = currentTime;
        }
    }

    public void keyPressed(){
        super.keyPressed();
        if(this.number == 1){
            this.capacityButtonPressed = keyH.aPressed;
        }else if(this.number == 2){
            this.capacityButtonPressed = keyH.mPressed;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
      double currentTime = System.currentTimeMillis();
      AffineTransform saveAt = g2.getTransform();
      g2.setColor(Color.BLACK);
      if(this.capacityActivated) {
        timeRemaining = (int)(this.capacityDuration-(currentTime-activationTime));
        pourcentage = timeRemaining / this.capacityDuration;
      } else {
        if(this.capacityCooldown>currentTime-lastUse) {
          timeRemaining = (int)(this.capacityCooldown-(currentTime-lastUse));
          pourcentage = 1 - timeRemaining / this.capacityCooldown;
        }
        else pourcentage = 1;
      }

      // draw avancement
      g2.setColor(Color.BLACK);
      g2.drawLine(getX()-1-gp.tileSize/2, getY()-1 + gp.tileSize+2, getX()+1 + gp.tileSize-gp.tileSize/2, getY()-1+ gp.tileSize+2);
      g2.drawLine(getX()-1-gp.tileSize/2, getY()-1 + gp.tileSize+2, getX()-1 -gp.tileSize/2, getY()+1 + gp.tileSize/16+ gp.tileSize+2);
      g2.drawLine(getX()-1-gp.tileSize/2, getY()+1 + gp.tileSize/16 + gp.tileSize+2, getX()+1 + gp.tileSize -gp.tileSize/2, getY()+1 + gp.tileSize/16+ gp.tileSize+2);
      g2.drawLine(getX()+1 + gp.tileSize-gp.tileSize/2, getY()-1 + gp.tileSize+2, getX()+1 + gp.tileSize -gp.tileSize/2, getY()+1 + gp.tileSize/16+ gp.tileSize+2);
      if(this.capacityActivated) g2.setColor(Color.RED);
      else g2.setColor(Color.BLUE);
      g2.drawRect(getX()-gp.tileSize/2, getY() + gp.tileSize + 2, (int)(gp.tileSize * pourcentage), gp.tileSize/16);

      g2.transform(at);
      g2.drawImage(sprite, x, y, gp.tileSize, gp.tileSize, null);
      g2.setTransform(saveAt);

      for(int i = 0; i < bullets.size(); i++) bullets.get(i).draw(g2);

    }
}
