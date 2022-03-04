import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

      // window settings
      JFrame window = new JFrame();
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setResizable(false);
      window.setTitle("Tank trouble");

      // display game window
      window.setLocationRelativeTo(null);
      window.setVisible(true);

    }
}
