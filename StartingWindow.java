import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.LinkedList;

public class StartingWindow extends JFrame implements ActionListener {

  private JTextField choice1;
  private JTextField choice2;
  private JTextArea dispResult;
  private JButton dispButton;
  private GamePanel gamePanel;
  private int characters[] = new int[2]; // contains the number of the tank chosen

  public final int width = 1050;
  public final int height = 750;

  public final int nbXtiles = 35; // number of tiles on the x-axis
  public final int nbYtiles = 25;

  public StartingWindow(){


    this.setTitle("Tank trouble");
    this.setSize(width, height);
    this.setResizable(false);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    initGUI();

    // display game window
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  public void initGUI() {

    System.out.println("Lauching menu ...");
    gamePanel = null;
    this.getContentPane().removeAll(); // remove all the element of the menu

    /* Selection Pane */

    JPanel paneSelection = new JPanel();
    paneSelection.setBounds(20, 20, 360, 150);
    paneSelection.setLayout(null);
    paneSelection.setBackground(Color.green);

    JLabel lblSelection = new JLabel();
    lblSelection.setText("Enter the number of a the tank wanted : \n1: painTank (basic)\n2: PhantomTank (go though walls)\n3: Kitty tank (launch slowing cakes)\n4: TiTank (can be huge)");
    lblSelection.setBounds(10, 10, 340, 100);
    paneSelection.add(lblSelection);

    choice1 = new JTextField();
    choice1.setBounds(150, 70, 60, 50);
    choice2 = new JTextField();
    choice2.setBounds(150, 120, 60, 50);

    paneSelection.add(lblSelection);
    paneSelection.add(choice1);
    paneSelection.add(choice2);

    /* Results Pane */

    JPanel paneResult = new JPanel();
    paneResult.setBounds(20, 190, 360, 150);
    paneResult.setLayout(null);
    paneResult.setBackground(Color.blue);

    dispButton = new JButton("START");
    dispButton.setBounds(100, 10, 160, 50);
    dispButton.setBackground(new Color(10, 144, 10));
    dispButton.setForeground(Color.white);
    dispButton.addActionListener(this);

    dispResult = new JTextArea();
    dispResult.setLineWrap(true);
    dispResult.setBounds(15, 70, 330, 220);

    JScrollPane scrollPane = new JScrollPane(dispResult);
    scrollPane.setBounds(15, 70, 330, 220);

    paneResult.add(dispButton);
    paneResult.add(dispResult);
    paneResult.add(scrollPane);

    /* Global Pane */

    JPanel globalPane = new JPanel();
    globalPane.setBounds(0, 0, 400, 400);
    globalPane.setLayout(null);
    globalPane.setBackground(Color.yellow);

    globalPane.add(paneSelection);
    globalPane.add(paneResult);
    this.add(globalPane);
  }

  public void actionPerformed (ActionEvent e) {

    // Select the characters from the inputs
    characters[0] = Integer.parseInt(choice1.getText());
    characters[1] = Integer.parseInt(choice2.getText());

    this.getContentPane().removeAll(); // remove all the element of the menu

    // add GamePanel
    gamePanel = new GamePanel(width, height, nbXtiles, nbYtiles, characters);
    this.add(gamePanel);
    this.pack(); // sets the window to its preferred size, should be useless

    gamePanel.startGameThread(); // start game loop
  }
}
