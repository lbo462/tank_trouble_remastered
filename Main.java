import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

      // window settings
      JFrame window = new JFrame();
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setResizable(false);
      window.setTitle("Tank trouble");

      // add GamePanel
      GamePanel gamePanel = new GamePanel();
      window.add(gamePanel);
      window.pack(); // sets the window to its preferred size

      // display game window
      window.setLocationRelativeTo(null);
      window.setVisible(true);

      gamePanel.startGameThread();

    }
}
