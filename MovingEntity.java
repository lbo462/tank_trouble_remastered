public abstract class MovingEntity extends Entity{
    double nextX,nextY;
    double nextA; // next angle
    double prevAngle; // previous angle
    double angle;
    double increaseSpeed; // value tp add to trueSpeed on key pressed
    double speed; // actual speed to add to pos
    double maxSpeed;
    boolean isMoving;

    double width, height;


    public void updateNextPosition(){
        nextX = x + (int)(speed * Math.sin(Math.toRadians(angle)));
        nextY = y + (int)(speed * Math.cos(Math.toRadians(angle)));
    }

    //abstract void update(); is not specified as it is an abstract class (i.e. supports abstract methods from mother class)
    abstract void collision();
    abstract void updatePosition();
}
