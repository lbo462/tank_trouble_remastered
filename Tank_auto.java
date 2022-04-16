import java.awt.Image;

public class Tank_auto extends Tank {

  public double angleGoal;

  public Tank_auto(int number, int x, int y, Image image, Image deadImage, GamePanel gp) {
    super(number, x, y, image, deadImage, gp);
  }

  @Override
  public void collision() {
    super.collision();
    if(collision) dash(-10);
  }

  @Override
  public void keyPressed() {
    upPressed = false;
    downPressed = false;
    leftPressed = false;
    rightPressed = false;
    shotPressed = false;

    int angleDiff = 0;
    double distance = 0;

    for(Tank t: gp.players) { // CHOOSE A TARGET
      if(t != this) { // choose a player to target but not ourself
        int deltaX = t.getX() - this.getX(); // difference of position in the x-direction between this tank and targeted player
        int deltaY = t.getY() - this.getY(); // difference of position in the y-direction

        // CALCULATE DISTANCE TO THE TARGET
        distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        // shoot
        if(distance < 500) shotPressed = true; // cirtually pressed the shot button

        // COMPUTE ANGLE TO ROTATE
        if(deltaY == 0) deltaY = 1; // do not divide by zero trick
        angleGoal = Math.toDegrees(Math.atan(deltaX / deltaY));
        if(Math.abs(angleGoal) < 90 && deltaY < 0) angleGoal += 180; // Leo, you have a scheme on yçur phone
        angleDiff = (int)(this.angle - angleGoal);
        if(Math.abs(angleDiff) > 3) {
          if(Math.abs(angleDiff) > 360 - 12) {
            if(angleDiff < 0)
              leftPressed = true;
            else
              rightPressed = true;
          } else {
            if(angleDiff > 0)
              leftPressed = true;
            else
              rightPressed = true;
          }
          this.angle %= 360;
        }
        break;
      }
    }
    if(Math.abs(angleDiff) <=  60 || Math.abs(angleDiff) >= 300) upPressed = true; // always move forward, never give up
  }
}
