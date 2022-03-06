import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

  public final int width = 960; // changeable
  public final int height = (width * 9)/16; // keep a 16/9 format

  public final int nbXtiles = 32; // number of tiles on the x-axis, changeable
  public final int nbYtiles = 18; // CAREFUL THE FORMAT SHOULD BE 16/9 TO RESPECT WIDTH AND HEIGHT

  public final int tileSize = width / nbXtiles; // size a single tile

  int FPS = 60;

  KeyHandler keyH = new KeyHandler();
  Thread gameThread;
  public Map currentMap = new Map(this, "1"); // public because entities need it for collisions
  Tank player1 = new Tank(this, keyH);

  public GamePanel() {
    this.setPreferredSize(new Dimension(width, height));
    this.setBackground(Color.black);
    this.setDoubleBuffered(true); // Increase game performance
    this.setFocusable(true); // so the window can be focused : needed to receive input from user

    this.addKeyListener(keyH);
  }

  // start Thread, start the game
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

  // update for each frame
  public void update() {
    player1.update();
  }

  // draw at each frame
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D)g; // g2 is our drawing god

    currentMap.draw(g2); // draw the map
    player1.draw(g2); // draw player1, ce bg

    g2.dispose();

  }
}
