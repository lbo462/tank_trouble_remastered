import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.UIManager.*;


public class MenuWindow  extends JFrame implements MouseListener {
  public final Color transparent = new Color(0, 0, 0,0);
  public final Font defaultFont = new Font("Serif", Font.BOLD, 25);
  public final int nbXtiles = 35; // number of tiles on the x-axis
  public final int nbYtiles = 25;
  public final int tileSize = 33;
  public final int width = nbXtiles * tileSize;
  public final int height = nbYtiles * tileSize;
  public final int nbTanks = 6;
  public final int nbMaps = 3;
  public final int buttonWidth = 200;
  public final int buttonHeight = 80;
  public final int previewPanelSide = 330;

  // Variables describing the advancement of the GUI
  public int stateOfGUI;// 0:beginning | 1:player1's tank | 2:player2's tank | 3:map choice | 4:options choice
  public int[] characters;//tanks choosen by players
  public int choiceMap;//map choosen by players
  public int nbGames;//numbers of games to be played
  public Font titleFont = new Font("serif",Font.BOLD,25); //imported font

  public JPanel containerGlobal;
  // Background image
  public ResizeImageLabel background;

  // Tank dictionnary
  public String[] tankNames;
  public ImageIcon[] tankImages;
  public String[] tankDescriptions;

  // Map dictionnary
  public String[] mapNames;
  public ImageIcon[] mapImages;
  public String[] mapDescriptions;

  // Buttons to choose tank and map
  JPanel buttonPanel;
  public HoverButton[] tankSelection;
  public HoverButton[] mapSelection;

  // Panel displaying pieces of information about the selection
  public PreviewPanel preview;

  // Index of the display
  public int previewIndex;

  // JPanel displaying the number of games
  public NumberChoicePanel nbGamesPanel;

  //Button to change step/advance/start
  public HoverButton nextButton;

  // Gif for introduction
  public ResizeImageLabel animation;

  // Display player's number choosing among other things
  public JLabel topLabel;

  public GamePanel gamePanel;

  public Clip menuMusic;

  public MenuWindow(){
    System.out.println("Starting menu ...");
    this.setIconImage(new ImageIcon("assets/icon.png").getImage());
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // play music
    try{
      AudioInputStream is = AudioSystem.getAudioInputStream(new File("assets/sounds/MusicMenu.wav").getAbsoluteFile());
      menuMusic = AudioSystem.getClip();
      menuMusic.open(is);
      menuMusic.loop(99);
    } catch(Exception e) {
      e.printStackTrace();
    }

    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
          if ("Nimbus".equals(info.getName())) {
              UIManager.setLookAndFeel(info.getClassName());
              break;
          }
      }
    } catch (Exception e) {
      try{
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }catch(Exception f){
        System.out.println("problem with look and feel");
        f.printStackTrace();
      }
    }
    this.setTitle("Tank Trouble Remastered");
    this.setSize(width, height);
    this.setResizable(false);
    this.setGlobalParameters();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    this.add(containerGlobal);
    this.logoAnimation();

    ImageIcon backImage = new ImageIcon("assets/menu/tankTroubleMenu.png");
    background = new ResizeImageLabel();
    background.setBounds(0,0,width,height);
    background.setBackground(Color.white);
    background.updateResizedImageIcon(backImage);

    nextButton = new HoverButton();
    nextButton.setText("START");
    nextButton.setFont(defaultFont);
    nextButton.setBackground(Color.WHITE);
    nextButton.setBounds((width-buttonWidth)/2,690,buttonWidth,buttonHeight);
    nextButton.setBackground(Color.white);
    nextButton.addMouseListener(this);

    containerGlobal.add(nextButton);
    containerGlobal.add(background,-1);
    this.repaint();
    stateOfGUI = 0;
    //stateOfGUI = -1;
  }

  public void logoAnimation(){
    animation = new ResizeImageLabel();
    animation.setSize(width, height);
    animation.setBackground(Color.orange);
    animation.updateResizedImageIcon(new ImageIcon("assets/menu/tankTrouble.gif"));
    containerGlobal.add(animation);
    containerGlobal.repaint();
    try {
      Thread.sleep(2500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    containerGlobal.remove(animation);
  }

  public void setGlobalParameters(){
    containerGlobal = new JPanel();
    containerGlobal.setBounds(0,0,width,height);
    containerGlobal.setBackground(transparent);
    containerGlobal.setLayout(null);
    this.createDictionnaries();
  }

  public void setPlayerParameters(){
    characters  = new int[2];
    characters[0] = 0;
    characters[1] = 0;

    topLabel = new JLabel();
    topLabel.setBounds(0, 0, 300, 50);
    topLabel.setFont(defaultFont);
    topLabel.setHorizontalAlignment(SwingConstants.CENTER);
    topLabel.setBackground(Color.BLUE);
    topLabel.setOpaque(true);
    topLabel.setForeground(Color.RED);
    topLabel.setText("Player 1 tank choice");
    containerGlobal.add(topLabel, 0);

    preview = new PreviewPanel(width/2-previewPanelSide,50,previewPanelSide,transparent,60);
    previewIndex = 0;
    preview.updatePreviewPanel(tankNames[previewIndex], tankImages[previewIndex], tankDescriptions[previewIndex]);
    containerGlobal.add(preview,0);

    buttonPanel = new JPanel();

    buttonPanel.setBounds(width/2-2*buttonWidth, 500, 4*buttonWidth, (int)(nbTanks/4+1)*buttonHeight);
    buttonPanel.setLayout(new GridLayout(0,4));
    tankSelection = new HoverButton[nbTanks];
    for (int i=0;i<tankSelection.length;i++){
      tankSelection[i] = new HoverButton();
      buttonPanel.add(tankSelection[i]);
      tankSelection[i].setIcon(tankImages[i]);
      tankSelection[i].addMouseListener(this);
    }
    containerGlobal.add(buttonPanel);

    nextButton.setText("NEXT");

    this.repaint();
  }

  public void setMapParameters(){

    topLabel.setText("Map choice");

    for(HoverButton button:tankSelection){
      buttonPanel.remove(button);
      buttonPanel.revalidate();
      buttonPanel.repaint();
    }
    //As we have less than 4 maps, we only need one line
    buttonPanel.setSize(4*buttonWidth, buttonHeight);

    previewIndex = 0;
    preview.updatePreviewPanel(mapNames[previewIndex], mapImages[previewIndex], mapDescriptions[previewIndex]);

    mapSelection = new HoverButton[nbMaps];
    for (int i=0;i<mapSelection.length;i++){
      mapSelection[i] = new HoverButton();
      mapSelection[i].setIcon(mapImages[i]);
      mapSelection[i].setText(mapNames[i]);
      mapSelection[i].setFont(defaultFont);
      mapSelection[i].setHorizontalTextPosition(SwingConstants.CENTER);
      mapSelection[i].addMouseListener(this);
      buttonPanel.add(mapSelection[i],0);
      buttonPanel.revalidate();
      buttonPanel.repaint();
    }
  }

  public void setOptionsParameters(){
    containerGlobal.repaint();
    topLabel.setText("Number of games");

    choiceMap = this.previewIndex;

    containerGlobal.remove(preview);
    containerGlobal.remove(buttonPanel);

    nbGamesPanel = new NumberChoicePanel(width/2-250, height/2-250, 500, 500, 1, defaultFont);
    containerGlobal.add(nbGamesPanel);
  }

  public void createDictionnaries(){
    //Creating tank dictionnaries
    tankNames = new String[nbTanks];
    tankImages = new ImageIcon[nbTanks];
    tankDescriptions = new String[nbTanks];

    tankNames[0] = "Default Tank";
    tankImages[0] = new ImageIcon("assets/entities/tank/defaultTank.gif");
    tankDescriptions[0] = "Original tank with no special ability. Vintage.";

    tankNames[1] = "Phantom";
    tankImages[1] = new ImageIcon("assets/entities/tank/phantom.gif");
    tankDescriptions[1] = "Tank capable of crossing walls. Furtive.";

    tankNames[2] = "Tankitty";
    tankImages[2] = new ImageIcon("assets/entities/tank/kittyTank.gif");
    tankDescriptions[2] = "Tank capable of slowing down ennemies. Cute.";

    tankNames[3] = "TiTank";
    tankImages[3] = new ImageIcon("assets/entities/tank/TiTank.gif");
    tankDescriptions[3] = "Tank capable of becoming bigger and breaking walls. Huge.";

    tankNames[4] = "Autotank";
    tankImages[4] = new ImageIcon("assets/entities/tank/autoTank.gif");
    tankDescriptions[4] = "Default tank, but automated (at least we tried). Smart.";

    tankNames[5] = "Tankjiro";
    tankImages[5] = new ImageIcon("assets/entities/tank/Tankjiro.gif");
    tankDescriptions[5] = "Tank capable of throwing fire at its enemies. Fireproof.";

    //Creating map Dctionnaries
    mapNames = new String[nbMaps];
    mapImages = new ImageIcon[nbMaps];
    mapDescriptions = new String[nbMaps];

    mapNames[0] = "Random";
    mapImages[0] = new ImageIcon("assets/maps/background.gif");
    mapDescriptions[0] = "Randomly generated map";

    mapNames[1] = "Custom map 1";
    mapImages[1] = new ImageIcon("assets/maps/1/background.gif");
    mapDescriptions[1] = "custom map 1";

    mapNames[2] = "Custom map 2";
    mapImages[2] = new ImageIcon("assets/maps/2/background.gif");
    mapDescriptions[2] = "custom map 2";
  }

  public void updateAndResizeImageIcon(JComponent c,ImageIcon img){
    if(c instanceof JLabel){
      ((JLabel)c).setIcon(new ImageIcon(img.getImage().getScaledInstance(c.getSize().width/2,c.getSize().height/2, Image.SCALE_DEFAULT)));
    }else if(c instanceof JButton){
      ((JButton)c).setIcon(new ImageIcon(img.getImage().getScaledInstance(c.getSize().width/2,c.getSize().height/2, Image.SCALE_DEFAULT)));
    }
  }

  @Override
  public void mouseClicked(MouseEvent e){
    switch(stateOfGUI){
      case 1:
      case 2:
        for (int i = 0;i<tankSelection.length;i++){
          if(e.getSource() == tankSelection[i]){//changing the tank displayed when clicking on a button
            if(i!=previewIndex){//changing only if the desired tank isn't displayed
              previewIndex = i;
              preview.updatePreviewPanel(tankNames[previewIndex], tankImages[previewIndex], tankDescriptions[previewIndex]);
              this.repaint();
            }
          }
        }
        break;
      case 3:
        for (int i = 0;i<mapSelection.length;i++){
          if(e.getSource() == mapSelection[i]){//changing the map displayed when clicking on a button
            if(i!=previewIndex){//changing only if the desired map isn't displayed
              previewIndex = i;
              preview.updatePreviewPanel(mapNames[previewIndex], mapImages[previewIndex], mapDescriptions[previewIndex]);
              this.repaint();
            }
          }
        }
        break;
    }
    if(e.getSource() == nextButton){
      switch(stateOfGUI){
        case 0: // first start menu
          // start tanks choices

          /* remove background image */
          containerGlobal.remove(background);
          JPanel newbackground = new JPanel();
          newbackground.setBounds(0,0,width,height);
          newbackground.setBackground(Color.WHITE);
          containerGlobal.add(newbackground);
          containerGlobal.repaint();

          this.setPlayerParameters();
          previewIndex = 0; // preview first tank
          break;
        case 1: // first tank chosen
          characters[0] = this.previewIndex+1; // register choice
          previewIndex = 0; // reset preview
          topLabel.setText("Player 2 tank choice");
          preview.updatePreviewPanel(tankNames[previewIndex], tankImages[previewIndex], tankDescriptions[previewIndex]);
          preview.repaint();
          break;
        case 2:
          characters[1] = this.previewIndex+1;
          topLabel.setText("Choose map ...");
          this.setMapParameters();
          break;
        case 3: // choose number of games
          this.setOptionsParameters();
          break;
        default:
          nbGames = nbGamesPanel.number;
          this.getContentPane().removeAll(); // remove all the element of the menu
          // add game panel

          // uncomment to skip menu
/*
          characters = new int[2];
          characters[0] = 1;
          characters[1] = 5;
          choiceMap = 1;
*/
          gamePanel = new GamePanel(width, height, nbXtiles, nbYtiles, characters,choiceMap,nbGames);
          gamePanel.setPreferredSize(new Dimension(width, height));
          gamePanel.setBackground(Color.black);
          gamePanel.setDoubleBuffered(true); // Increase game performance
          gamePanel.setFocusable(true); // so the panel can be focused : needed to receive input from user
          this.add(gamePanel);
          this.pack();
          // starts the game's loop
          gamePanel.startGameThread();
          menuMusic.stop();
          break;
      }
      stateOfGUI++;
    }
  }

  @Override
  public void mousePressed(MouseEvent e){}

  @Override
  public void mouseReleased(MouseEvent e){}

  @Override
  public void mouseEntered(MouseEvent e){}

  @Override
  public void mouseExited(MouseEvent e){}
}
