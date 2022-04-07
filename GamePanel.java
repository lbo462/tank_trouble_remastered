import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.AlphaComposite;
import javax.swing.*;
import java.awt.Font;
import java.util.ArrayList;
import java.awt.event.*;


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
  public ArrayList<Dust> dust; // contains dust
  public JPanel restartPane;
  public JPanel closePane;
  public final Color defaultBlueFrame = new Color(1, 49, 180);
  public final Color hover_orange = new Color(255, 127, 0);
  public final Color selectedRedFrame = new Color(237, 0, 0);

  private int FPS;
  private Thread gameThread;
  private int gamesToPlay; // number of games to play
  private int numberOfGames; // how much games were played
  private int winner; // number of the winner

  public GamePanel(int width, int height, int nbXtiles, int nbYtiles, int[] characters, int mapNumber, int gamesToPlay) {
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
    this.gamesToPlay = gamesToPlay;
    this.numberOfGames = 1;
    this.tankDead = false;

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
    if(!gameOver) {
      if(!paused) {
        currentMap.update(); // update tiles
        // update tanks
        for(Tank t: players) {
          if(t.dead && currentTime - t.timeDied > 600) { // check if a tank died
            System.out.println("Player_" + t.number + " exploded. " + (gamesToPlay - numberOfGames) + " games left.");
            resetGame();
            if(numberOfGames > gamesToPlay) goToEndMenu();
            break;
          } else if(t.dead) tankDead = true;
          if(!tankDead) t.update();
        }

        // update dust
        for(int i = 0; i < dust.size(); i++) {
          dust.get(i).update();
          if(dust.get(i).dead) dust.remove(i); // remove dead dust
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
    if(!gameOver) {
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

      g2.dispose();
    }
  }

  void goToEndMenu() {
    gameOver = true;
    s.music.stop();
    s.end.play();
    // find winner
    int iMaxScore = 0;
    for(int i = 0; i < players.length; i++){
      if(players[i].score > players[iMaxScore].score) iMaxScore = i;
    }
    winner = players[iMaxScore].number;
    System.out.println("Player_"+winner+" won.");

    this.removeAll();

    JPanel globalPane = new JPanel();
    globalPane.setLayout(null);
    globalPane.setBounds(0,0,width,height);
    globalPane.setBackground(defaultBlueFrame);

    // display winner
    JLabel winnerLbl = new JLabel("<html><body>Player_"+winner+" won !  GG<br>Scores :</body></html>");
    winnerLbl.setFont(new Font("Serif", Font.BOLD, 25));
    winnerLbl.setForeground(defaultBlueFrame);
    winnerLbl.setBounds(10,10,width/2,100);
    winnerLbl.setBackground(Color.white);
    winnerLbl.setOpaque(true);
    globalPane.add(winnerLbl);

    // display score
    for(Tank t: players) {

      JLabel playerScoreLbl = new JLabel();
      if(t.numberOfShoots != 0) playerScoreLbl.setText("Player_"+t.number+" : "+t.score+" | "+Double.toString(100*t.score/t.numberOfShoots)+"% hits");
      else playerScoreLbl.setText("Player_"+t.number+" : "+t.score+" | no hits");
      playerScoreLbl.setFont(new Font("Serif", Font.BOLD, 25));
      playerScoreLbl.setForeground(defaultBlueFrame);
      playerScoreLbl.setBounds(10,30+70*t.number,width/2,70);
      playerScoreLbl.setBackground(Color.white);
      playerScoreLbl.setOpaque(true);
      globalPane.add(playerScoreLbl);
    }

    // restart button
    restartPane = new JPanel();
    restartPane.setLayout(null);
    restartPane.setBounds((width/2)-160,590,160,90);
    restartPane.setBackground(defaultBlueFrame);
    restartPane.addMouseListener(this);
    JLabel restartLbl = new JLabel("Play again");
    restartLbl.setFont(new Font("Serif", Font.BOLD, 25));
    restartLbl.setForeground(defaultBlueFrame);
    restartLbl.setBounds(10,10,140,70);
    restartLbl.setBackground(Color.white);
    restartLbl.setOpaque(true);
    restartPane.add(restartLbl);
    globalPane.add(restartPane);

    // close button
    closePane = new JPanel();
    closePane.setLayout(null);
    closePane.setBounds((width/2),590,160,90);
    closePane.setBackground(defaultBlueFrame);
    closePane.addMouseListener(this);
    JLabel closeLbl = new JLabel("Quit");
    closeLbl.setFont(new Font("Serif", Font.BOLD, 25));
    closeLbl.setForeground(defaultBlueFrame);
    closeLbl.setBounds(10,10,140,70);
    closeLbl.setBackground(Color.white);
    closeLbl.setOpaque(true);
    closePane.add(closeLbl);
    globalPane.add(closePane);

    JLabel background = new JLabel(new ImageIcon("assets/menu/BackgroundMenu.jpg"));
    background.setBounds(0,0,width,height);
    globalPane.add(background);

    this.add(globalPane);
  }

  public void mouseClicked(MouseEvent e){
    if(e.getSource() == restartPane) returnToMenu(); // go back to main menu
    if(e.getSource() == closePane) System.exit(0); // end programm
  }

  public void mousePressed(MouseEvent e){}

  public void mouseReleased(MouseEvent e){}

  public void mouseEntered(MouseEvent e){ //Detecting mouse entering
    if(e.getSource() == restartPane) restartPane.setBackground(hover_orange);
    if(e.getSource() == closePane) closePane.setBackground(hover_orange);
  }

  public void mouseExited(MouseEvent e){
    if(e.getSource() == restartPane) restartPane.setBackground(defaultBlueFrame);
    if(e.getSource() == closePane) closePane.setBackground(defaultBlueFrame);
  }

  void returnToMenu () {
    System.out.println("Returning to menu...");
    // Reset JFrame ...
    StartingWindow topFrame = (StartingWindow) SwingUtilities.getWindowAncestor(this); // retrieve mother JFrame
    topFrame.initGUI();
    this.gameOver = false;
    this.paused = true;
    s.end.stop(); // stop music
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
