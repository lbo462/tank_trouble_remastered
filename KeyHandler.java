
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyHandler implements KeyListener {

  public boolean upPressed, leftPressed, downPressed, rightPressed;

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch(e.getKeyCode()) {
      case KeyEvent.VK_Z:
        upPressed = true;
        break;
      case KeyEvent.VK_Q:
        leftPressed = true;
        break;
      case KeyEvent.VK_S:
        downPressed = true;
        break;
      case KeyEvent.VK_D:
        rightPressed = true;
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch(e.getKeyCode()) {
      case KeyEvent.VK_Z:
        upPressed = false;
        break;
      case KeyEvent.VK_Q:
        leftPressed = false;
        break;
      case KeyEvent.VK_S:
        downPressed = false;
        break;
      case KeyEvent.VK_D:
        rightPressed = false;
        break;
    }
  }

}
