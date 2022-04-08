import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;

public class NumberChoicePanel extends JPanel implements MouseListener{
    JLabel display;
    JButton incrementButton;
    JButton decrementButton;
    int number;

    public NumberChoicePanel(int posX,int posY,int width,int height,int defaultNumber,Font bFont,Color labelColor){
        number = defaultNumber;

        this.setBounds(posX, posY, width, height);
        this.setBackground(Color.gray);

        display = new JLabel();
        display.setBounds(10,10,width-20,60);
        display.setBackground(labelColor);
        display.setHorizontalAlignment(SwingConstants.CENTER);
        display.setFont(bFont);
        display.setText(String.valueOf(number));
        display.setOpaque(true);
        add(display);

        incrementButton = new JButton();
        incrementButton.setIcon(new ImageIcon("assets/menu/incrementButton.gif"));
        incrementButton.setBounds(10, 70, width/2-10, height/2-45);
        incrementButton.setFont(bFont);
        incrementButton.setBackground(Color.darkGray);
        add(incrementButton);

        decrementButton = new JButton();
        decrementButton.setIcon(new ImageIcon("assets/menu/incrementButton.gif"));
        decrementButton.setBounds(width/2+10, 70, width/2-10, height/2-45);
        decrementButton.setFont(bFont);
        decrementButton.setBackground(Color.darkGray);
        add(decrementButton);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getSource() == decrementButton){
            if(number>1){
                number--;
            }
        }else if (e.getSource() == incrementButton){
            number++;
        }
        this.revalidate();
        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
