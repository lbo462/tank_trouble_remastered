import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;


// automatically resize the label with the size of the image
public class ResizeImageLabel extends JLabel{

    public void updateResizedImageIcon(ImageIcon img){
        setIcon(new ImageIcon(img.getImage().getScaledInstance(this.getSize().width,this.getSize().height , Image.SCALE_DEFAULT)));
    }
}
