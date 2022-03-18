
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

// This class implements all the key bindings needed to play

public class KeyHandler implements KeyListener {

  public boolean upPressed, leftPressed, downPressed, rightPressed, zPressed, qPressed, sPressed, dPressed;
  public boolean enterPressed, spacePressed, escapePressed;
  public boolean aPressed,mPressed;

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch(e.getKeyCode()) {
      case KeyEvent.VK_Z:
        zPressed = true;
        break;
      case KeyEvent.VK_Q:
        qPressed = true;
        break;
      case KeyEvent.VK_S:
        sPressed = true;
        break;
      case KeyEvent.VK_D:
        dPressed = true;
        break;
      case KeyEvent.VK_UP:
        upPressed = true;
        break;
      case KeyEvent.VK_LEFT:
        leftPressed = true;
        break;
      case KeyEvent.VK_DOWN:
        downPressed = true;
        break;
      case KeyEvent.VK_RIGHT:
        rightPressed = true;
        break;
      case 10: // ENTER
        enterPressed = true;
        break;
      case KeyEvent.VK_SPACE:
        spacePressed = true;
        break;
      case KeyEvent.VK_A:
        aPressed = true;
        break;
      case KeyEvent.VK_M:
        mPressed=true;
        break;
      case KeyEvent.VK_ESCAPE:
        escapePressed = true;
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch(e.getKeyCode()) {
      case KeyEvent.VK_Z:
        zPressed = false;
        break;
      case KeyEvent.VK_Q:
        qPressed = false;
        break;
      case KeyEvent.VK_S:
        sPressed = false;
        break;
      case KeyEvent.VK_D:
        dPressed = false;
        break;
      case KeyEvent.VK_UP:
        upPressed = false;
        break;
      case KeyEvent.VK_LEFT:
        leftPressed = false;
        break;
      case KeyEvent.VK_DOWN:
        downPressed = false;
        break;
      case KeyEvent.VK_RIGHT:
        rightPressed = false;
        break;
      case 10: // ENTER
        enterPressed = false;
        break;
      case KeyEvent.VK_SPACE:
        spacePressed = false;
        break;
      case KeyEvent.VK_A:
        aPressed = false;
        break;
      case KeyEvent.VK_M:
        mPressed=false;
        break;
      case KeyEvent.VK_ESCAPE:
        escapePressed = false;
        break;
    }
  }

}
