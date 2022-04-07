import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.Dimension;
import javax.swing.ImageIcon;

public class MenuWindow  extends JFrame implements MouseListener {
  public final Color defaultBlueFrame = new Color(1, 49, 180);
  public final Color selectedRedFrame = new Color(237, 0, 0);
  public final Color hover_orange = new Color(255, 127, 0);
  public final Color transparent = new Color(0, 0, 0,0);
  public final Font defaultFont = new Font("Serif", Font.BOLD, 25);
  public final int width = 1050;
  public final int height = 750;
  public final int nbXtiles = 35; // number of tiles on the x-axis
  public final int nbYtiles = 25;
  public final int nbTanks = 4;
  public final int nbMaps = 2;
  public final int buttonWidth = 200;
  public final int buttonHeight = 80;
  public final int previewPanelSide = 330;

  //variable describing the advancement of the GUI
  public int stateOfGUI;//0:beginning 1:player1's tank 2:player2's tank 3:map
  public int[] characters;//tanks choosen by players
  public int choiceMap;//map choosen by players
  public int nbGames;//numbers of games to be played
  
  JPanel containerGlobal;

  //background image
  ResizeImageLabel background; 

  //Tank dictionnary
  String[] tankNames;
  ImageIcon[] tankImages;
  String[] tankDescriptions;

  //Map dictionnary
  String[] mapNames;
  ImageIcon[] mapImages;
  String[] mapDescriptions;

  //Buttons to choose tank and map
  HoverButton[] tankSelection;
  HoverButton[] mapSelection;

  //Panel displaying pieces of information about the selection
  PreviewPanel preview;

  //Index of the display
  int previewIndex;

  //JPanel displaying the number of games
  NumberChoicePanel nbGamesPanel;

  //Button to change step/advance/start
  JButton nextButton;

  //Gif for introduction
  ResizeImageLabel animation;

  public GamePanel gamePanel;

  public MenuWindow(){
    String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
    try {
      UIManager.setLookAndFeel(lookAndFeel);
    }   
    catch (Exception e) {
        e.printStackTrace();
    }
    
    this.setTitle("Tank Trouble");
    this.setSize(width, height);
    this.setResizable(false);
    this.setGlobalParameters();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    this.add(containerGlobal);
    this.logoAnimation();
    this.startGUI();
    
    this.repaint();
  }

  public void logoAnimation(){
    animation = new ResizeImageLabel();
    animation.setSize(width, height);
    animation.setBackground(Color.orange);
    animation.updateResizedImageIcon(new ImageIcon("assets/menu/tankTrouble.gif"));
    containerGlobal.add(animation);
    this.repaint();
    System.out.println("animation start");
    try {
      Thread.sleep(2500);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println("animation end");
    containerGlobal.remove(animation);
  }

  public void startGUI(){
      this.setStartingParameters();
      stateOfGUI = 0;
  }

  public void setGlobalParameters(){
    containerGlobal = new JPanel();
    containerGlobal.setLayout(null);
    containerGlobal.setBounds(0,0,width,height);
    containerGlobal.setBackground(transparent);
  }

  public void setStartingParameters(){
    ImageIcon backImage = new ImageIcon("assets/menu/tankTroubleMenu.png");
    background = new ResizeImageLabel();
    background.setBounds(0,0,width,height);
    background.setBackground(Color.white);
    background.updateResizedImageIcon(backImage);
    
    nextButton = new HoverButton();
    nextButton.setText("START");
    nextButton.setFont(defaultFont);
    nextButton.setBounds((width-buttonWidth)/2,590,buttonWidth,buttonHeight);
    nextButton.setBackground(Color.white);
    nextButton.addMouseListener(this);

    containerGlobal.add(nextButton);
    containerGlobal.add(background,-1);
  }

  public void setPlayerParameters(){
    characters  = new int[2];
    characters[0] = 0;
    characters[1] = 0;

    //Filling in the tank arrays:
    tankNames = new String[nbTanks];
    tankImages = new ImageIcon[nbTanks];
    tankDescriptions = new String[nbTanks]; 

    tankNames[0] = "Pain Tank";
    tankImages[0] = new ImageIcon("assets/entities/tank/defaultTank.gif");
    tankDescriptions[0] = "Original tank with no capacity. Vintage = for the connaisseur.";

    tankNames[1] = "Phantom Tank";
    tankImages[1] = new ImageIcon("assets/entities/tank/phantom.gif");
    tankDescriptions[1] = "Tank with the ability to cross walls. When you want to go fufu.";

    tankNames[2] = "Kitty Tank";
    tankImages[2] = new ImageIcon("assets/entities/tank/kittyTank.gif");
    tankDescriptions[2] = "Cute tank which can shot bullets to slow ennemies. One day cats will rule over the world.";

    tankNames[3] = "TiTank";
    tankImages[3] = new ImageIcon("assets/entities/tank/TiTank.gif");
    tankDescriptions[3] = "A tank with the ability to become bigger and break walls. Yes Ricco, \"Kaboom\".";

    preview = new PreviewPanel(width/2-previewPanelSide,50,previewPanelSide,transparent,defaultFont,60);
    previewIndex = 0;
    preview.updatePreviewPanel(tankNames[previewIndex], tankImages[previewIndex], tankDescriptions[previewIndex]);     
    containerGlobal.add(preview,0);

    tankSelection = new HoverButton[nbTanks];
    for (int i=0;i<tankSelection.length;i++){
      tankSelection[i] = new HoverButton();
      tankSelection[i].setBounds(width/2-(2-i)*buttonWidth,500,buttonWidth,buttonHeight);
      tankSelection[i].setIcon(tankImages[i]);
      tankSelection[i].addMouseListener(this);
      containerGlobal.add(tankSelection[i]);
    }

    this.repaint();
  }

  public void setMapParameters(){
    mapNames = new String[nbMaps];
    mapImages = new ImageIcon[nbMaps];
    mapDescriptions = new String[nbMaps];

    mapNames[0] = "   Vanilla 1";
    mapImages[0] = new ImageIcon("assets/map/1/background.gif");
    mapDescriptions[0] = "map 1";

    mapNames[1] = "   Vanilla 2";
    mapImages[1] = new ImageIcon("assets/map/2/background.gif");
    mapDescriptions[1] = "map 2";

    for(HoverButton button:tankSelection){
      containerGlobal.remove(button);
      containerGlobal.revalidate();
      containerGlobal.repaint();
    }

    previewIndex = 0;
    preview.updatePreviewPanel(mapNames[previewIndex], mapImages[previewIndex], mapDescriptions[previewIndex]);

    mapSelection = new HoverButton[nbMaps];
    for (int i=0;i<mapSelection.length;i++){
      mapSelection[i] = new HoverButton();
      mapSelection[i].setBounds(width/2-(2-i)*buttonWidth,500,buttonWidth,buttonHeight);
      //mapSelection[i].setIcon(mapImages[i]);
      setBackground(Color.darkGray);
      mapSelection[i].addMouseListener(this);
      containerGlobal.add(mapSelection[i],-1);
      containerGlobal.revalidate();
      containerGlobal.repaint();
    }
  }

  public void setOptionsParameters(){
    containerGlobal.remove(preview);
    for (HoverButton button:mapSelection){
      containerGlobal.remove(button);
    }

    nbGamesPanel = new NumberChoicePanel(width/2-250, height/2-250, 500, 500, 1, defaultFont, Color.green);
    containerGlobal.add(nbGamesPanel);
  }

  public void updateAndResizeImageIcon(JComponent c,ImageIcon img){
    if(c instanceof JLabel){
      ((JLabel)c).setIcon(new ImageIcon(img.getImage().getScaledInstance(this.getSize().width,this.getSize().height , Image.SCALE_DEFAULT)));
    }else if(c instanceof JButton){
      ((JButton)c).setIcon(new ImageIcon(img.getImage().getScaledInstance(this.getSize().width,this.getSize().height , Image.SCALE_DEFAULT)));
    }
  }

  @Override
  public void mouseClicked(MouseEvent e){
    switch(stateOfGUI){
      case 1,2:
        for (int i = 0;i<tankSelection.length;i++){
          if(e.getSource() == tankSelection[i]){
            previewIndex = i;
            preview.updatePreviewPanel(tankNames[previewIndex], tankImages[previewIndex], tankDescriptions[previewIndex]);
            
            this.repaint();
          }
        }
        break;
      case 3:
        for (int i = 0;i<mapSelection.length;i++){
          if(e.getSource() == mapSelection[i]){
            choiceMap = i;
            preview.updatePreviewPanel(mapNames[previewIndex], mapImages[previewIndex], mapDescriptions[previewIndex]);
          }
        }
        break;
    }
    if(e.getSource() == nextButton){
      switch(stateOfGUI){
        case 0:
          this.setPlayerParameters();
          previewIndex = 0;
          break;
        case 1:
          characters[0] = this.previewIndex+1;
          previewIndex = 0;
          break;
        case 2:
          characters[1] = this.previewIndex+1;
          this.setMapParameters();
          break;
        case 3:
          choiceMap = this.previewIndex;
          this.setOptionsParameters();
        default:
          nbGames = nbGamesPanel.number;
          this.getContentPane().removeAll(); // remove all the element of the menu
          // add game panel
          gamePanel = new GamePanel(width, height, nbXtiles, nbYtiles, characters,choiceMap,nbGames);
          gamePanel.setPreferredSize(new Dimension(width, height));
          gamePanel.setBackground(Color.black);
          gamePanel.setDoubleBuffered(true); // Increase game performance
          gamePanel.setFocusable(true); // so the panel can be focused : needed to receive input from user
          this.add(gamePanel);
          this.pack();
          // starts the game's loop
          gamePanel.startGameThread();
          break;
      }
      stateOfGUI++; 
    }
    this.revalidate();
    this.repaint();
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
