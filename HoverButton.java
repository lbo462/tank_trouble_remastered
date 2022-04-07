import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HoverButton extends JButton implements MouseListener{

    public HoverButton(){
        super();
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setHorizontalAlignment(SwingConstants.CENTER);
        setBackground(Color.CYAN);
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

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