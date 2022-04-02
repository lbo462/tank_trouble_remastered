import java.awt.geom.AffineTransform;

public abstract class MovingEntity extends Entity{
    public double nextX, nextY; // next position
    public double nextA; // next angle
    public double prevAngle; // previous angle
    public double angle; // actual angle
    public double increaseSpeed; // value tp add to trueSpeed on key pressed
    public double speed; // actual speed to add to pos
    public double maxSpeed;
    public boolean dead;
    public AffineTransform at; // matrice of rotation

    //abstract void update(); is not specified as it is an abstract class (i.e. supports abstract methods from mother class)
    abstract void collision(); // sets the collision variables to evaluate the state of the entity
    /* get the position (X, Y) in the reference frame, reverting the rotating matrix at(Affine Transform) */
    abstract int getX();
    abstract int getY();
}
