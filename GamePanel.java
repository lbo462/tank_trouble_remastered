import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Color;
import java.awt.AlphaComposite;
import javax.swing.*;
import java.awt.Font;
import java.util.ArrayList;


public class GamePanel extends JPanel implements Runnable {

  boolean musicOn = false;

  boolean paused = false;
  boolean gameOver = false;
  double timeOver; // time at which the game ended
  double timePaused; // used to regulate between two press on pause button
  int gamesToPlay = 3; // number of games to play
  int numberOfGames = 1; // how much games were played
  int winner; // number of the winner
  int frame; // number of frame

  public int width;
  public int height;

  public int nbXtiles; // number of tiles on the x-axis
  public int nbYtiles;

  public int tileSize; // size a single tile

  int FPS = 60;

  public KeyHandler keyH = new KeyHandler();
  public Sound s = new Sound(); // sound player
  public ImageManager im = new ImageManager();
  Thread gameThread;
  public Map currentMap; // public because entities need it for collisions
  Tank[] players;
  ArrayList<Dust> dust; // contains dust

  public GamePanel(int width, int height, int nbXtiles, int nbYtiles, int[] characters) {
    tileSize = width / nbXtiles;
    timePaused = System.currentTimeMillis(); // initialise with random stuff

    this.width = width;
    this.height = height;
    this.nbXtiles = nbXtiles;
    this.nbYtiles = nbYtiles;
    this.players = new Tank[2];
    this.dust = new ArrayList<Dust>();
    frame = 0;

    for(int i = 0; i < characters.length; i++) {
      // configure positions
      int pos[] = playerPos(i+1);
      int x = pos[0], y = pos[1];

      // configure character chosen
      switch(characters[i]) {
        case 1:
          players[i] = new Tank(i+1, x, y, im.painTank, im.defaultExplosion, this, keyH);
          break;
        case 2:
          players[i] = new Tank_Phantom(i+1, x, y, this, keyH);
          break;
        case 3:
          players[i] = new Tank_Kitty(i+1, x, y, this, keyH);
          break;
        case 4:
          players[i] = new Tank_TiTank(i+1, x, y, this, keyH);
          break;
      }
    }
    this.addKeyListener(keyH);

    System.out.println("Generating map ...");
    currentMap = new Map(this);

    // music maestro
    if(musicOn) s.music.loop();
  }

  // start Thread, start the game
  public void startGameThread(){
    gameThread = new Thread(this);
    gameThread.start();

  }

  // return the position of the player of number 1 or 2 in the form {x, y}
  public int[] playerPos(int number) {
    int pos[] = new int[2];
    switch (number) {
      case 1: // first player, top left corner
        pos[0] = tileSize;
        pos[1] = tileSize;
        break;
      case 2: // second player bottom right corner
        pos[0] = tileSize*(nbXtiles-2);
        pos[1] = tileSize*(nbYtiles-2);
        break;
    }
    return pos;
  }

  public void resetGame() {

    for(int i = 0; i < players.length; i++) {
      // configure positions
      int pos[] = new int[2];
      pos = playerPos(i+1);
      players[i].reset(pos[0], pos[1]); // reset player given its coordinates
    }

    numberOfGames ++;
  }

  @Override
  public void run(){

    this.requestFocus(); // get the window focus

    // FPS managing
    double drawInterval = 1000000000 / FPS; //time interval between two frame
    double nextDrawTime = System.nanoTime() + drawInterval; //system time to draw the next frame

    // Game loop
    while(gameThread != null) {
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
      frame++;

    }
  }

  // update for each frame
  public void update() {
    double currentTime = System.currentTimeMillis();
    if(gameOver) {
      if(currentTime - timeOver > 3000) {
        System.out.println("Returning to menu...");
        // Reset JFrame ...
        StartingWindow topFrame = (StartingWindow) SwingUtilities.getWindowAncestor(this); // retrieve mother JFrame
        topFrame.initGUI();
        gameOver = false;
        paused = true;
        s.end.stop();
      }
    } else {
      if(!paused) {
        // update tanks
        for(Tank t: players) {
          t.update();
          if(t.dead && currentTime - t.timeDied > 600) { // check if a tank died
            System.out.println("Player_" + t.number + " exploded. " + (gamesToPlay - numberOfGames) + " games left.");
            resetGame();
            if(numberOfGames > gamesToPlay) { // if the number of games to play has been reached
              numberOfGames = 1;
              gameOver = true;
              s.music.stop();
              s.end.play();
              // find winner
              int iMaxScore = 0;
              for(int i = 0; i < players.length; i++)
                if(players[i].score > players[iMaxScore].score) iMaxScore = i;
              winner = players[iMaxScore].number;
              timeOver = currentTime; // record time of end-game for "animation"
            }
          }
        }
        // update dust
        for(int i = 0; i < dust.size(); i++) {
          dust.get(i).update();
          // remove dead dust
          if(dust.get(i).dead){
            dust.remove(i);
          }
        }
        if(keyH.escapePressed && currentTime - timePaused > 500) {
          paused = true;
          s.music.stop();
          timePaused = currentTime;
        }
      } else {
        if(keyH.escapePressed && currentTime - timePaused > 500) {
          paused = false;
          if(musicOn) s.music.loop();
          timePaused = currentTime;
        }
      }
    }
  }

  // draw at each frame
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g; // g2 is our drawing god

    // draw the background
    g2.drawImage(im.background, 0, 0, width, height, this);

    // draw dust
    for(int i = 0; i < dust.size(); i++) dust.get(i).draw(g2);

    // draw players (and bullets)
    for(Tank t: players)
      t.draw(g2);
    g2.setColor(Color.BLACK);

    currentMap.draw(g2); // draw the map

    // draw pause logo
    if(paused) {
      // transparency
      g2.setColor(Color.WHITE);
      AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
      g2.setComposite(alcom);

      // two rects of the logo
      g2.fillRect(width - 100, 20, 25, 80);
      g2.fillRect(width - 55, 20, 25, 80);

      // reset transparency
      alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
      g2.setComposite(alcom);
    }
    if(gameOver) {
      // transparency
      g2.setColor(Color.RED);
      AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
      g2.setComposite(alcom);

      // change font
      Font currentFont = g.getFont();
      Font newFont = currentFont.deriveFont(currentFont.getSize() * 5F);
      g2.setFont(newFont);

      // two rects of the logo
      g2.drawString("Player_"+winner+" won", 200, 300);
      g2.setFont(currentFont);

      // reset transparency
      alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
      g2.setComposite(alcom);
    }

    g2.dispose();

  }
}
