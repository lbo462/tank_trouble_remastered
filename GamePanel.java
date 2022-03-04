import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

  final int width = 1080;
  final int height = 480;

  KeyHandler keyH = new KeyHandler();
  Thread gameThread;

  int x = 10;
  int y = 10;
  int speed = 1;

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
    // Game loop
    while(gameThread != null) {

      // UPDATE
      update();

      // DRAW
      repaint();

    }
  }

  public void update() {

    System.out.println(keyH.rightPressed);

    if(keyH.upPressed)
      y -= speed;
    if(keyH.leftPressed)
      x -= speed;
    if(keyH.downPressed)
      y += speed;
    if(keyH.rightPressed)
      x += speed;

  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D)g;
    g2.setColor(Color.white);
    g2.fillRect(x, y, 30, 30);
    g2.dispose();

  }

}
