import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.io.File;
import javax.sound.sampled.*;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HoverButton extends JButton implements MouseListener{

    public Clip click;

    public HoverButton(){
        super();
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setHorizontalAlignment(SwingConstants.CENTER); //Centers text
        setBackground(Color.RED);
        addMouseListener(this);
        try {
          String url = "assets/sounds/click.wav";
          AudioInputStream is = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
          click = AudioSystem.getClip();
          click.open(is);
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      click.setFramePosition(0);
      click.start();
     }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        this.setBorder(BorderFactory.createRaisedBevelBorder());

    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setBorder(BorderFactory.createLoweredBevelBorder());

    }
}
