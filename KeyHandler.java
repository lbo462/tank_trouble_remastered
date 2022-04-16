
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

// This class implements all the key bindings needed to play

public class KeyHandler implements KeyListener {

  public boolean enter, space, escape; // pause and shoot buttons
  public boolean z, q, s, d, a;  // first player bindings
  public boolean o, k, l, m, p; // second player bindings

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    switch(e.getKeyCode()) {
      case KeyEvent.VK_Z:
        z = true;
        break;
      case KeyEvent.VK_Q:
        q = true;
        break;
      case KeyEvent.VK_S:
        s = true;
        break;
      case KeyEvent.VK_D:
        d = true;
        break;
      case KeyEvent.VK_A:
        a = true;
        break;
      case KeyEvent.VK_O:
        o = true;
        break;
      case KeyEvent.VK_K:
        k = true;
        break;
      case KeyEvent.VK_L:
        l = true;
        break;
      case KeyEvent.VK_M:
        m = true;
        break;
      case KeyEvent.VK_P:
        p = true;
        break;
      case 10: // ENTER
        enter = true;
        break;
      case KeyEvent.VK_SPACE:
        space = true;
        break;
      case KeyEvent.VK_ESCAPE:
        escape = true;
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch(e.getKeyCode()) {
      case KeyEvent.VK_Z:
        z = false;
        break;
      case KeyEvent.VK_Q:
        q = false;
        break;
      case KeyEvent.VK_S:
        s = false;
        break;
      case KeyEvent.VK_D:
        d = false;
        break;
      case KeyEvent.VK_A:
        a = false;
        break;
      case KeyEvent.VK_O:
        o = false;
        break;
      case KeyEvent.VK_K:
        k = false;
        break;
      case KeyEvent.VK_L:
        l = false;
        break;
      case KeyEvent.VK_M:
        m = false;
        break;
      case KeyEvent.VK_P:
        p = false;
        break;
      case 10: // ENTER
        enter = false;
        break;
      case KeyEvent.VK_SPACE:
        space = false;
        break;
      case KeyEvent.VK_ESCAPE:
        escape = false;
        break;
    }
  }
}
