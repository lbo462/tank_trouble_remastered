public class PhantomTank extends SuperTank{
    public PhantomTank(int number, int x, int y, GamePanel gp, KeyHandler keyH){
        super(number, x, y, gp, keyH,3000,20000);
    }

    public void update(){
        if(keyH.aPressed){
            capacityActivation();
        }
        if(capacityActivated){
            this.keyPressed();
            this.rotation();
            this.translation();
            this.updatePosition();
            this.shoot();
            this.deadBulletRemoval();
        }else{
            super.update();
        }
    }
}
