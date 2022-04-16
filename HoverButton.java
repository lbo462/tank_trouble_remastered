import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HoverButton extends JButton implements MouseListener{

    public AudioClip click;

    public HoverButton(){
        super();
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setHorizontalAlignment(SwingConstants.CENTER); //Centers text
        setBackground(Color.RED);
        addMouseListener(this);
        URL url = getClass().getResource("assets/sounds/click.wav");
        click = Applet.newAudioClip(url);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      click.stop();
      click.play();
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
