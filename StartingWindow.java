import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Dimension;

public class StartingWindow extends JFrame implements MouseListener {

      JPanel[] containerNumPlayer1;
      JPanel[] containerMap;
      JPanel[] containerNumPlayer2;
      JPanel containerPlayer1; //Panel containing "Player 1"
      JPanel containerPlayer2; //Panel containing "Player 2"
      JPanel containerLaunch;
      JLabel[] NumPlayer2;
      public GamePanel gamePanel;
      public int characters[] = new int[2];
      public final Color defaultBlueFrame = new Color(1, 49, 180);
      public final Color hover_orange = new Color(255, 127, 0);
      public final Color selectedRedFrame = new Color(237, 0, 0);
      int choiceMap;
      public final int width = 1050;
      public final int height = 750;
      public final int nbXtiles = 35; // number of tiles on the x-axis
      public final int nbYtiles = 25;

  	public StartingWindow() {
      this.setTitle("Tank trouble");
      this.setSize(width, height);
      this.setResizable(false);
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);

      initGUI();

      // display game window
      this.setLocationRelativeTo(null);
      this.setVisible(true);
    }
      public void initGUI(){

        System.out.println("Lauching menu ...");
        gamePanel = null;
        this.getContentPane().removeAll(); // remove all the element of the menu

        choiceMap = 0;
        characters[0] = 0;
        characters[1] = 0;

  		  JPanel containerGlobal = new JPanel();
  		  containerGlobal.setLayout(null);
  		  containerGlobal.setBounds(0,0,width,height);
        containerGlobal.setBackground(defaultBlueFrame);

        //1ST LINE MAP SELECTION
        containerMap = new JPanel[3];
        JLabel[] Map = new JLabel[3];
        for(int i  = 0 ; i <containerMap.length ; i++) { //3 squares for the choice of the map
          containerMap[i] = new JPanel();
          containerMap[i].setLayout(null);
          containerMap[i].setBounds(10+i*340,20,330,330); //size of the square 330*330 px
          if(i == choiceMap)
            containerMap[i].setBackground(selectedRedFrame); //default selected map is 0 (random)
          else
            containerMap[i].setBackground(defaultBlueFrame);
          containerMap[i].addMouseListener(this);
          Map[i] = new JLabel(new ImageIcon("assets/menu/Map"+i+".png")); //size of image is 310*310 px
          Map[i].setBounds(10,10,310,310);
          Map[i].setBackground(Color.white); //if no image loaded default color is white
        }

        //2ND LINE PLAYER 1 SELECTION
        containerPlayer1 = new JPanel();
        containerPlayer1.setLayout(null);
        containerPlayer1.setBounds(30,380,150,80);
        containerPlayer1.setBackground(defaultBlueFrame);
        JLabel textPlayer1 = new JLabel("   Player 1");
        textPlayer1.setFont(new Font("Serif", Font.BOLD, 25));
        textPlayer1.setForeground(selectedRedFrame);
        textPlayer1.setBounds(10,10,130,60);
        textPlayer1.setBackground(Color.white);
        textPlayer1.setOpaque(true); //allows to see the background color

        containerNumPlayer1 = new JPanel[4]; //can chose up to 4 diferent tank
        JLabel[] NumPlayer1 = new JLabel[4];
        for(int i  = 0 ; i <containerNumPlayer1.length ; i++){
          containerNumPlayer1[i] = new JPanel();
          containerNumPlayer1[i].setLayout(null);
          containerNumPlayer1[i].setBounds(200+i*200,380,180,80);
          if(i==characters[0])
            containerNumPlayer1[i].setBackground(selectedRedFrame);
          else
            containerNumPlayer1[i].setBackground(defaultBlueFrame);
          containerNumPlayer1[i].addMouseListener(this);
          NumPlayer1[i] = new JLabel("            "+String.valueOf(i+1));
          NumPlayer1[i].setFont(new Font("Serif", Font.BOLD, 25));
          NumPlayer1[i].setForeground(selectedRedFrame);
          NumPlayer1[i].setBounds(10,10,160,60);
          NumPlayer1[i].setBackground(Color.white);
          NumPlayer1[i].setOpaque(true);
        }

        //3RD LINE PLAYER 2 SELECTION
        containerPlayer2 = new JPanel(); //Panel containing "Player 2"
        containerPlayer2.setLayout(null);
        containerPlayer2.setBounds(30,500,150,80);
        containerPlayer2.setBackground(defaultBlueFrame);
        JLabel textPlayer2 = new JLabel("   Player 2");
        textPlayer2.setFont(new Font("Serif", Font.BOLD, 25));
        textPlayer2.setForeground(selectedRedFrame);
        textPlayer2.setBounds(10,10,130,60);
        textPlayer2.setBackground(Color.white);
        textPlayer2.setOpaque(true); //allows to see the background color

        containerNumPlayer2 = new JPanel[4]; //can chose up to 4 diferent tank
        NumPlayer2 = new JLabel[4];
        for(int i  = 0 ; i <containerNumPlayer2.length ; i++){
          containerNumPlayer2[i] = new JPanel();
          containerNumPlayer2[i].setLayout(null);
          containerNumPlayer2[i].setBounds(200+i*200,500,180,80);
          if(i==characters[1])
            containerNumPlayer2[i].setBackground(selectedRedFrame);
          else
            containerNumPlayer2[i].setBackground(defaultBlueFrame);
          containerNumPlayer2[i].addMouseListener(this);
          NumPlayer2[i] = new JLabel("            "+String.valueOf(i+1));
          NumPlayer2[i].setFont(new Font("Serif", Font.BOLD, 25));
          NumPlayer2[i].setForeground(selectedRedFrame);
          NumPlayer2[i].setBounds(10,10,160,60);
          NumPlayer2[i].setBackground(Color.white);
          NumPlayer2[i].setOpaque(true);
        }

        //4TH LINE LAUNCH BUTTON
        containerLaunch = new JPanel();
        containerLaunch.setLayout(null);
        containerLaunch.setBounds((width/2)-80,590,160,90); // width/2 - half the square size to be sure to have the button in the middle of the screen
        containerLaunch.setBackground(defaultBlueFrame);
        containerLaunch.addMouseListener(this);
        JLabel play = new JLabel(" LAUNCH !!");
        play.setFont(new Font("Serif", Font.BOLD, 25));
        play.setForeground(selectedRedFrame);
        play.setBounds(10,10,140,70);
        play.setBackground(Color.white);
        play.setOpaque(true);

        JLabel background = new JLabel(new ImageIcon("assets/menu/BackgroundMenu.jpg"));
        background.setBounds(0,0,width,height);

        //ADDING EVERY PANEL
        for(int i = 0 ; i < containerMap.length ; i ++){ //adding the 3 map square
            containerMap[i].add(Map[i]);
            containerGlobal.add(containerMap[i]);
        }
        for(int i = 0 ; i < containerNumPlayer1.length ; i ++){ //adding the 1 2 3 4 squares
            containerNumPlayer1[i].add(NumPlayer1[i]);
            containerGlobal.add(containerNumPlayer1[i]);
            containerNumPlayer2[i].add(NumPlayer2[i]);
            containerGlobal.add(containerNumPlayer2[i]);
        }
        containerPlayer1.add(textPlayer1); //adding Player 1 & Player 2 square
        containerPlayer2.add(textPlayer2);
        containerLaunch.add(play);
        containerGlobal.add(containerPlayer1);
        containerGlobal.add(containerPlayer2);
        containerGlobal.add(containerLaunch);
        containerGlobal.add(background);
        add(containerGlobal);

        this.repaint();

        gamePanel = null;
  	}

      public void mouseClicked(MouseEvent e){
        for (int i = 0;i<containerMap.length;i++){
          if(e.getSource() == containerMap[i]){
            containerMap[choiceMap].setBackground(defaultBlueFrame);
            containerMap[i].setBackground(selectedRedFrame);
            choiceMap=i;
          }
        }
        for (int i = 0;i<containerNumPlayer1.length;i++){
          if(e.getSource() == containerNumPlayer1[i]){
            containerNumPlayer1[characters[0]].setBackground(defaultBlueFrame);
            containerNumPlayer1[i].setBackground(selectedRedFrame);
            characters[0]=i;
          }
          if(e.getSource() == containerNumPlayer2[i]){
            containerNumPlayer2[characters[1]].setBackground(defaultBlueFrame);
            containerNumPlayer2[i].setBackground(selectedRedFrame);
            characters[1]=i;
          }
        }
        if(e.getSource() == containerLaunch){
          // containerLaunch.setBackground(selectedRedFrame);
          //choiceMap is 0 for random, 1 for virgin, 2 for cool map
          characters[0] += 1;
          characters[1] += 1;
          this.getContentPane().removeAll(); // remove all the element of the menu

          // add game panel
          gamePanel = new GamePanel(width, height, nbXtiles, nbYtiles, characters);
          gamePanel.setPreferredSize(new Dimension(width, height));
          gamePanel.setBackground(Color.black);
          gamePanel.setDoubleBuffered(true); // Increase game performance
          gamePanel.setFocusable(true); // so the panel can be focused : needed to receive input from user
          this.add(gamePanel);
          this.pack();

          gamePanel.startGameThread(); // start game loop
        }
      }

      public void mousePressed(MouseEvent e){}

      public void mouseReleased(MouseEvent e){}

      public void mouseEntered(MouseEvent e){ //Detecting mouse entering the square
        for (int i=0;i<containerMap.length;i++){ //Checking the map selection line
            if(e.getSource() == containerMap[i] && choiceMap != i) containerMap[i].setBackground(hover_orange);
        }
        for (int i=0;i<containerNumPlayer1.length;i++){ //Checking the tank selection line
            if(e.getSource() == containerNumPlayer1[i] && characters[0] != i) containerNumPlayer1[i].setBackground(hover_orange);
            if(e.getSource() == containerNumPlayer2[i] && characters[1] != i) containerNumPlayer2[i].setBackground(hover_orange);
        }
        if(e.getSource() == containerLaunch) containerLaunch.setBackground(hover_orange); //Checking the launch button
      }

      public void mouseExited(MouseEvent e){ //Detecting mouse exiting th square
        for (int i=0;i<containerMap.length;i++){ //Checking the map selection line
          if(e.getSource() == containerMap[i] && choiceMap != i) containerMap[i].setBackground(defaultBlueFrame);
        }
        for (int i=0;i<containerNumPlayer1.length;i++){ //Checking the tank selection line
          if(e.getSource() == containerNumPlayer1[i] && characters[0] != i) containerNumPlayer1[i].setBackground(defaultBlueFrame);
          if(e.getSource() == containerNumPlayer2[i] && characters[1] != i) containerNumPlayer2[i].setBackground(defaultBlueFrame);
        }
        if(e.getSource() == containerLaunch) containerLaunch.setBackground(defaultBlueFrame); //Checking the launch button
      }
}
