public class Particle_Fire extends Particle {

  public GamePanel gp;
  public Tank owner; // fire owner can't be killed by it
  public int radius; // radius of the particle
  public boolean killed;

  public Particle_Fire(int x, int y, double direction, GamePanel gp, Tank owner) {
    super(x, y, gp.im.fireParticle);

    this.gp = gp;
    this.owner = owner;
    this.radius = 5;
    this.speed = 4;
    this.killed = false;
    this.width = 2*radius;
    this.height = 2*radius;
    at.rotate(Math.toRadians((int)(-direction)), this.x+5, this.y+5);
    this.lifeTime = 500 + Math.random() * 600; // more or less random
  }

  @Override
  public void update() {
    this.x += (int)(Math.random()*9)-4; // random x displacement
    this.y += speed;

    this.collision();
    super.update();
  }

  @Override
  public void collision() {
    for(Tank t: gp.players) {
      if(t.number != owner.number) {
        for(int r = 0; r < radius + t.width/2; r += 5) {
          for(int a = 0; a < 360; a += 2) {
            // corresponding pos in cartesian coordinates
            int xCord = (int)(getX() + r * Math.cos(Math.toRadians(a)));
            int yCord = (int)(getY() + r * Math.sin(Math.toRadians(a)));

            if(xCord == t.getX() && yCord == t.getY()) {
              t.kill();

              this.killed = true;
              this.dead = true;
              break;
            }
          }
        }
      }
    }
  }
}
