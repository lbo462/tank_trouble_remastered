import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.event.*;
import java.awt.Font;
import javax.swing.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, MouseListener {

  private boolean musicOn;
  public boolean paused;
  public boolean gameOver;
  public double timePaused; // used to regulate between two press on pause button
  public boolean tankDead; // is a tank dead ?
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
  public ArrayList<PowerUp> pu;
  public ArrayList<Particle> particles; // contains dust
  public HoverButton restartButton;
  public HoverButton closeButton;
  public final Color defaultBlueFrame = new Color(1, 49, 180);
  public final Color hover_orange = new Color(255, 127, 0);
  public final Color selectedRedFrame = new Color(237, 0, 0);

  private int FPS;
  private Thread gameThread;
  private int gamesToPlay; // number of games to play
  private int numberOfGames; // how much games were played
  private int winner; // number of the winner
  private boolean musicIntroFinished;

  public GamePanel(int width, int height, int nbXtiles, int nbYtiles, int[] characters, int mapNumber, int gamesToPlay) {
    this.width = width;
    this.height = height;
    this.nbXtiles = nbXtiles;
    this.nbYtiles = nbYtiles;
    this.tileSize = width / nbXtiles;
    this.musicOn = true;
    this.paused = false;
    this.gameOver = false;
    this.timePaused = System.currentTimeMillis(); // initialise with random stuff
    this.players = new Tank[2];
    this.pu = new ArrayList<PowerUp>();
    this.particles = new ArrayList<Particle>();
    this.frame = 0;
    this.FPS = 60;
    this.gamesToPlay = gamesToPlay;
    this.numberOfGames = 1;
    this.tankDead = false;

    this.s = new Sound();
    this.im = new ImageManager();
    this.keyH = new KeyHandler();
    this.addKeyListener(keyH);


    System.out.println("Generating map ...");
    currentMap = new Map(mapNumber, this); // creates the map

    System.out.println("Spawning players ...");
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
          players[i] = new TankPhantom(i+1, x, y, this);
          break;
        case 3:
          players[i] = new TankKitty(i+1, x, y, this);
          break;
        case 4:
          players[i] = new TankTiTank(i+1, x, y, this);
          break;
        case 5:
          players[i] = new TankAuto(i+1, x, y, this);
          break;
        case 6:
          players[i] = new TankJiro(i+1, x, y, this);
          break;
      }
    }

    // music maestro
    if(musicOn) s.intro.start();
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
    if(!gameOver) {
      if(!paused) {
        currentMap.update(); // update tiles
        // update tanks
        for(Tank t: players) {
          if(t.dead && currentTime - t.timeDied > 600) { // check if a tank died
            System.out.println("Player_" + t.number + " exploded. " + (gamesToPlay - numberOfGames+1) + " games left.");
            resetGame();
            if(numberOfGames > gamesToPlay) goToEndMenu();
            break;
          } else if(t.dead) tankDead = true;
          if(!tankDead) t.update();
        }

        // eventually pop new power ups
        if(frame % 1000 == 0) {
          double ratio = 0.5;

          double rand = Math.random();
          int x = (int)(tileSize + Math.random() * (width-2*tileSize));
          int y = (int)(tileSize + Math.random() * (height-2*tileSize));
          int xOnGrid = (int)(x / tileSize);
          int yOnGrid = (int)(y / tileSize);

          while(currentMap.tiles[yOnGrid][xOnGrid].collision) {
            x = (int)(Math.random() * (width-tileSize));
            y = (int)(Math.random() * (height-tileSize));
            xOnGrid = (int)(x / tileSize);
            yOnGrid = (int)(y / tileSize);
          }

          if(rand < ratio)
            pu.add(new PUResetCoolDown(this, xOnGrid*tileSize, yOnGrid*tileSize));
          else
            pu.add(new PUSpeedUp(this, xOnGrid*tileSize, yOnGrid*tileSize));
        }

        // update power ups
        for(int i = 0; i < pu.size(); i++) {
          pu.get(i).update();
          if(!pu.get(i).isAlive) pu.remove(i); // remove trash
        }

        // update particles
        for(int i = 0; i < particles.size(); i++) {
          particles.get(i).update();
          if(particles.get(i).dead) particles.remove(i); // remove dead dust
        }

        if(keyH.escape && currentTime - timePaused > 500) { // check pause
          paused = true;
          timePaused = currentTime;
        }
      } else { // cancel pause
        if(keyH.escape && currentTime - timePaused > 500) {
          paused = false;
          timePaused = currentTime;
        }
      }
      if(musicOn && !musicIntroFinished && !s.intro.isActive()) {
        musicIntroFinished = true;
        s.mainLoop.loop(99);
      }
    }
  }

  // draw at each frame
  @Override
  public void paintComponent(Graphics g) {
    if(!gameOver) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g; // g2 is our drawing god

      // draw the background
      currentMap.drawBackground(g2);

      // draw dust
      for(int i = 0; i < particles.size(); i++) particles.get(i).draw(g2);

      // draw power ups
      for(int i = 0; i < pu.size(); i++) pu.get(i).draw(g2);

      // draw players (and bullets)
      for(Tank t: players)
        t.draw(g2);
      g2.setColor(Color.BLACK);

      currentMap.draw(g2); // draw the map

      // draw pause logo
      if(paused) drawLogo(g2);

      g2.dispose();
    }
  }

  public void goToEndMenu() {
    gameOver = true;
    s.intro.stop();
    s.mainLoop.stop();
    s.end.start();
    // find winner
    int iMaxScore = 0;
    for(int i = 0; i < players.length; i++){
      if(players[i].score > players[iMaxScore].score) iMaxScore = i;
    }
    winner = players[iMaxScore].number;
    System.out.println("Player_"+winner+" won.");

    this.removeAll();
    this.repaint();

    JPanel endPanel = new JPanel();
    endPanel.setLayout(null);
    endPanel.setBounds(0,0,width,height);
    endPanel.setBackground(Color.WHITE);

    Font font = new Font("Serif", Font.BOLD, 25);
    Font bigFont = new Font("Serif", Font.BOLD, 100);

    // display winner
    JLabel winnerLabel = new JLabel("Player "+winner+" won !");
    winnerLabel.setFont(bigFont);
    winnerLabel.setBackground(Color.WHITE);
    winnerLabel.setForeground(Color.RED);
    winnerLabel.setBounds(0,30,width,120);
    winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    winnerLabel.setOpaque(true);
    this.add(winnerLabel);

    // display score
    JLabel player1ScoreLabel = new JLabel();
    player1ScoreLabel.setText("Player 1 : "+players[0].score+" points");
    player1ScoreLabel.setFont(font);
    player1ScoreLabel.setBackground(Color.WHITE);
    player1ScoreLabel.setForeground(Color.BLACK);
    player1ScoreLabel.setBounds(width/4,170,width/2,70);
    player1ScoreLabel.setOpaque(true);
    this.add(player1ScoreLabel);

    JLabel player2ScoreLabel = new JLabel();
    player2ScoreLabel.setText("Player 2 : "+players[1].score+" points");
    player2ScoreLabel.setFont(font);
    player2ScoreLabel.setBackground(Color.WHITE);
    player2ScoreLabel.setForeground(Color.BLACK);
    player2ScoreLabel.setBounds(width/4,260,width/2,70);
    player2ScoreLabel.setOpaque(true);
    this.add(player2ScoreLabel);

    //Restart button
    restartButton = new HoverButton();
    restartButton.setBounds((width/2)-160,590,160,90);
    restartButton.setText("Play again");
    restartButton.setFont(font);
    restartButton.setHorizontalAlignment(SwingConstants.CENTER);
    restartButton.addMouseListener(this);
    endPanel.add(restartButton);

    // close button
    closeButton = new HoverButton();
    closeButton.setBounds((width/2),590,160,90);
    closeButton.setText("Quit");
    closeButton.setFont(font);
    closeButton.setHorizontalAlignment(SwingConstants.CENTER);
    closeButton.addMouseListener(this);
    endPanel.add(closeButton);

    this.add(endPanel);
  }

  public void mouseClicked(MouseEvent e){
    if(e.getSource() == restartButton) returnToMenu(); // go back to main menu
    if(e.getSource() == closeButton) System.exit(0); // end programm
  }

  public void mousePressed(MouseEvent e){}

  public void mouseReleased(MouseEvent e){}

  public void mouseEntered(MouseEvent e){}

  public void mouseExited(MouseEvent e){}

  public void returnToMenu () {
    // Reset JFrame ...
    MenuWindow topFrame = (MenuWindow) SwingUtilities.getWindowAncestor(this); // retrieve mother JFrame
    topFrame.dispose();
    topFrame = new MenuWindow();
  }

  public void drawLogo(Graphics2D g2) {
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
    this.tankDead = false;
    for(int i = 0; i < players.length; i++) {
      // configure positions
      Vector pos = new Vector();
      pos = playerPos(i+1);
      players[i].reset(pos.x, pos.y); // reset player given its coordinates
    }
    numberOfGames ++;
  }
}
