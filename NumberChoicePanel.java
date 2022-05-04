import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
 
//Class for the GUI. Used to choose the number of games wanted.

public class NumberChoicePanel extends JPanel implements MouseListener{
    public JLabel display;
    public JButton incrementButton;
    public JButton decrementButton;
    public int number;

    public NumberChoicePanel(int posX,int posY,int width,int height,int defaultNumber,Font bFont){
        this.setBounds(posX, posY, width, height);
        this.setBackground(Color.WHITE);

        display = new JLabel();
        display.setText(" " + Integer.toString(number) + " game");
        display.setBounds(10,10,width-20,60);
        display.setHorizontalAlignment(SwingConstants.CENTER);
        display.setFont(bFont);
        display.setOpaque(true);
        display.setBackground(Color.WHITE);
        add(display, 0);

        incrementButton = new JButton();
        incrementButton.setIcon(new ImageIcon("assets/menu/incrementButton.gif"));
        incrementButton.setBounds(10, 70, width/2-10, height/2-45);
        incrementButton.setFont(bFont);
        incrementButton.setBackground(Color.darkGray);
        incrementButton.addMouseListener(this);
        add(incrementButton);

        decrementButton = new JButton();
        decrementButton.setIcon(new ImageIcon("assets/menu/decrementButton.gif"));
        decrementButton.setBounds(width/2+10, 70, width/2-10, height/2-45);
        decrementButton.setFont(bFont);
        decrementButton.setBackground(Color.darkGray);
        decrementButton.addMouseListener(this);
        add(decrementButton);

        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getSource() == decrementButton){
            if(number>1){
                number--;
            }
        }else if (e.getSource() == incrementButton){
            number++;
        }
        if(number > 1)
          display.setText(" " + Integer.toString(number) + " games");
        else
          display.setText(" " + Integer.toString(number) + " game");
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
