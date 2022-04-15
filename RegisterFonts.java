import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.io.File;

public class RegisterFonts {
    public static void main(String[] args) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/rs125.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/ChunkFive-Regular.otf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Gasalt-Black.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Gasalt-Regular.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Gasalt-Thin.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Chainwhacks-vm72E.ttf")));
       } catch (Exception e) {
            e.printStackTrace();
       } 
    }
}
