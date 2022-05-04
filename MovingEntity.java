import java.awt.geom.AffineTransform;

public abstract class MovingEntity extends Entity{
    public double nextX, nextY; // next position
    public double nextA; // next angle
    public double prevAngle; // previous angle
    public double angle; // actual angle
    public double increaseSpeed; // value tp add to trueSpeed on key pressed
    public double speed; // actual speed to add to pos
    public double maxSpeed;
    public double bornAt; // time of birth
    public double lifeTime; // how much it should last
    public double dashedAt;
    public boolean dashing;
    public boolean dead; // true if dead
    public AffineTransform at; // matrice of rotation

    // perform a dash
    public void dash(int speed) {
      this.speed += speed;
      this.dashing = true;
      this.dashedAt = System.currentTimeMillis();
    }

    //abstract void update(); is not specified as it is an abstract class (i.e. supports abstract methods from mother class)
    abstract void collision(); // sets the collision variables to evaluate the state of the entity
    /* get the position (X, Y) in the reference frame, reverting the rotating matrix at(Affine Transform) */
    public int getX() {
      double m00 = at.getScaleX(), m01 = at.getShearX(), m02 = at.getTranslateX();
      return (int)(m00 * (x+width/2) + m01 * (y+height/2) + m02);
    }
    public int getY() {
      double m10 = at.getScaleY(), m11 = at.getShearY(), m12 = at.getTranslateY();
      return (int)(m10 * (y+height/2) + m11 * (x+width/2) + m12);
    }
}
