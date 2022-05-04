import javax.swing.*;
import java.awt.*;

//JPanel containing the image of the tank, its name and a small description

public class PreviewPanel extends JPanel{
    public final Color transparent = new Color(0, 0, 0,0);
    public JLabel pName;
    public ResizeImageLabel pImage;
    public JTextArea pDescription;
    public Font titleFont = new Font("dialog",Font.BOLD,50); //Choose font
    public Font textFont = new Font("monospaced",Font.PLAIN,20);

    public PreviewPanel(int posX,int posY,int shortSide,Color backColor,int titleHeight){
        super();
        setLayout(null);
        setBounds(posX,posY,2*shortSide,shortSide);
        setBackground(Color.WHITE);

        pName = new JLabel();
        pName.setHorizontalAlignment(SwingConstants.CENTER);
        pName.setFont(titleFont);
        pName.setBackground(backColor);
        pName.setBounds(shortSide,0,shortSide,titleHeight);

        pImage = new ResizeImageLabel();
        pImage.setBounds(0,0,shortSide,shortSide);
        pImage.setBackground(Color.pink);

        pDescription = new JTextArea();
        pDescription.setFont(textFont);
        pDescription.setForeground(Color.BLACK);
        pDescription.setBackground(backColor);
        pDescription.setBounds(shortSide,titleHeight,shortSide,shortSide-titleHeight);
        pDescription.setLineWrap(true);//set to go back to newline when reaching border
        pDescription.setWrapStyleWord(true);//wrap lines at words and not characters
        pDescription.setEditable(false);
        pDescription.setEnabled(false);
        pDescription.setHighlighter(null);
        pDescription.setSelectedTextColor(Color.white);
        pDescription.setDragEnabled(false);
        pDescription.setSelectionColor(null);

        add(pName);
        add(pImage);
        add(pDescription);
    }

    public void updatePreviewPanel(String newName,ImageIcon newImage,String newDescription){
        pName.setText(newName);
        pImage.updateResizedImageIcon(newImage);
        pDescription.setText(newDescription);
    }
}
