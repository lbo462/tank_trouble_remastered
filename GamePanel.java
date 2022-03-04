import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

  final int width = 1080;
  final int height = 480;

  int FPS = 60;

  KeyHandler keyH = new KeyHandler();
  Thread gameThread;
  Tank player1 = new Tank(this, keyH);
  TileManager tileM = new TileManager(this);

  public GamePanel() {
    this.setPreferredSize(new Dimension(width, height));
    this.setBackground(Color.black);
    this.setDoubleBuffered(true); // Increase game performance
    this.setFocusable(true); // so the window can be focused : needed to receive input from user

    this.addKeyListener(keyH);
  }

  public void startGameThread(){
    gameThread = new Thread(this);
    gameThread.start();

  }

  @Override
  public void run(){

    // FPS managing
    double drawInterval = 1000000000 / FPS; //time interval between two frame
    double nextDrawTime = System.nanoTime() + drawInterval; //system time to draw the next frame

    // Game loop
    while(gameThread != null) {

      long currentTime = System.nanoTime(); // in nanoseconds
      update();
      repaint();

      // Wait correct amount of time to achieve correct FPS
      // Here, we take into account the time needed to update and paint the current frame so that we respect the correct FPS
      try {
        double remainingTime = nextDrawTime - System.nanoTime(); //time left until the next frame should be drawn
        if(remainingTime < 0) remainingTime = 0; // sanity check
        Thread.sleep((long) (remainingTime/1000000)); // convert nanoseconds to ms
        nextDrawTime += drawInterval;
      } catch(InterruptedException e) {
        e.printStackTrace(); //if there was a problem during the Thread.sleep)(), print it
      }


    }
  }

  public void update() {
    player1.update();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D)g; // g2 is our drawing god

    tileM.draw(g2); // draw the tiles
    player1.draw(g2); // draw player1, ce bg

    g2.dispose();

  }

}
