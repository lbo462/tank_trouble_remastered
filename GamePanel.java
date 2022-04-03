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

  private boolean musicOn;
  public boolean paused;
  public boolean gameOver;
  public double timeOver; // time at which the game ended
  public double timePaused; // used to regulate between two press on pause button
  public int frame; // index of the current frame, uselfull for animation
  public int width;
  public int height;
  public int nbXtiles; // number of tiles on the x-axis
  public int nbYtiles;
  public int tileSize; // size a single tile
  public KeyHandler keyH;
  public Sound s; // sound player
  public ImageManager im;
  public Map currentMap; // public because entities need it for collisions
  public Tank[] players;
  public ArrayList<Dust> dust; // contains dust

  private int FPS;
  private Thread gameThread;
  private int gamesToPlay; // number of games to play
  private int numberOfGames; // how much games were played
  private int winner; // number of the winner

  public GamePanel(int width, int height, int nbXtiles, int nbYtiles, int[] characters, int mapNumber) {
    this.width = width;
    this.height = height;
    this.nbXtiles = nbXtiles;
    this.nbYtiles = nbYtiles;
    this.tileSize = width / nbXtiles;
    this.musicOn = false;
    this.paused = false;
    this.gameOver = false;
    this.timePaused = System.currentTimeMillis(); // initialise with random stuff
    this.players = new Tank[2];
    this.dust = new ArrayList<Dust>();
    this.frame = 0;
    this.FPS = 60;
    this.gamesToPlay = 3;
    this.numberOfGames = 1;

    this.s = new Sound();
    this.im = new ImageManager();
    this.keyH = new KeyHandler();
    this.addKeyListener(keyH);

    // initialise players
    for(int i = 0; i < characters.length; i++) {
      // configure positions
      Vector pos = playerPos(i+1); // spawn point
      int x = pos.x, y = pos.y;

      // configure character chosen
      switch(characters[i]) {
        case 1:
          players[i] = new Tank(i+1, x, y, im.painTank, im.defaultExplosion, this);
          break;
        case 2:
          players[i] = new Tank_Phantom(i+1, x, y, this);
          break;
        case 3:
          players[i] = new Tank_Kitty(i+1, x, y, this);
          break;
        case 4:
          players[i] = new Tank_TiTank(i+1, x, y, this);
          break;
      }
    }

    System.out.println("Generating map ...");
    currentMap = new Map(mapNumber, this); // creates the map

    // music maestro
    if(musicOn) s.music.loop();
  }

  // start Thread, start the game
  public void startGameThread(){
    gameThread = new Thread(this);
    gameThread.start();
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
        this.gameOver = false;
        this.paused = true;
        s.end.stop(); // stop music
      }
    } else {
      if(!paused) {

        for(int i=0; i<currentMap.tiles.length; i++)
          for(int j=0; j<currentMap.tiles[i].length; j++)
            currentMap.tiles[i][j].debug = false;
        // update tanks
        for(Tank t: players) {
          t.update();
          if(t.dead && currentTime - t.timeDied > 600) { // check if a tank died
            System.out.println("Player_" + t.number + " exploded. " + (gamesToPlay - numberOfGames) + " games left.");
            resetGame();
            if(numberOfGames > gamesToPlay) { // if the number of games to play has been reached
              numberOfGames = 1; // reset games counter
              gameOver = true;
              s.music.stop();
              s.end.play();
              // find winner
              int iMaxScore = 0;
              for(int i = 0; i < players.length; i++)
                if(players[i].score > players[iMaxScore].score) iMaxScore = i;
              winner = players[iMaxScore].number;
              timeOver = currentTime; // record time of end-game for animation
            }
          }
        }

        currentMap.update(); // update tiles to destroy them

        // update dust
        for(int i = 0; i < dust.size(); i++) {
          dust.get(i).update();
          // remove dead dust
          if(dust.get(i).dead){
            dust.remove(i);
          }
        }
        if(keyH.escapePressed && currentTime - timePaused > 500) { // check pause
          paused = true;
          s.music.stop();
          timePaused = currentTime;
        }
      } else { // cancel pause
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
    currentMap.drawBackground(g2);

    // draw dust
    for(int i = 0; i < dust.size(); i++) dust.get(i).draw(g2);

    // draw players (and bullets)
    for(Tank t: players)
      t.draw(g2);
    g2.setColor(Color.BLACK);

    currentMap.draw(g2); // draw the map

    // draw pause logo
    if(paused) drawLogo(g2);

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

  void drawLogo(Graphics2D g2) {
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

  // return the spawn point of the player of number 1 or 2
  public Vector playerPos(int number) {
    Vector pos = new Vector();
    switch (number) {
      case 1: // first player, top left corner
        pos.x = tileSize;
        pos.y = tileSize;
        break;
      case 2: // second player bottom right corner
        pos.x = tileSize*(nbXtiles-2);
        pos.y = tileSize*(nbYtiles-2);
        break;
    }
    return pos;
  }

  // reset game
  public void resetGame() {
    for(int i = 0; i < players.length; i++) {
      // configure positions
      Vector pos = new Vector();
      pos = playerPos(i+1);
      players[i].reset(pos.x, pos.y); // reset player given its coordinates
    }
    numberOfGames ++;
  }
}
