public abstract class MovingEntity extends Entity{
    int nextX,nextY;
    double angle;
    double speed;


    public void updateNextPosition(){
        nextX = x + (int)(speed * Math.sin(Math.toRadians(angle)));
        nextY = y + (int)(speed * Math.cos(Math.toRadians(angle)));
    }

    //abstract void update(); is not specified as it is an abstract class (i.e. supports abstract methods from mother class)
    abstract void collision();
    abstract void updatePosition();
}
