package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {

    private List<Key> keys = new ArrayList<Key>();
    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();
    public Key jump = new Key();
    public Key use = new Key();
    public Key nextL = new Key();
    public Key nextR = new Key();
    public Key restart = new Key();

    public InputHandler(Canvas canvas) { canvas.addKeyListener(this); }

    public void tick() {
        for (Key key : keys) {
            key.tick();
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) { toggle(e, true); }
    public void keyReleased(KeyEvent e) { toggle(e, false); }

    private void toggle(KeyEvent e, boolean pressed) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyCode() == KeyEvent.VK_UP) up.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_DOWN) down.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyCode() == KeyEvent.VK_LEFT) left.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_NUMPAD6 || e.getKeyCode() == KeyEvent.VK_RIGHT) right.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_SPACE) jump.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_F || e.getKeyCode() == KeyEvent.VK_0) use.toggle(pressed);

        if (e.getKeyCode() == KeyEvent.VK_Q) nextL.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_E) nextR.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_R) restart.toggle(pressed);
    }

    public class Key {
        private int absorbs;
        private int presses;

        public boolean down;
        public boolean clicked;

        public Key() { keys.add(this); }

        public void tick() {
            if (presses > absorbs) {
                absorbs++;
                clicked = true;
            } else clicked = false;
        }

        public void toggle(boolean pressed) {
            if (down != pressed) down = pressed;
            if (pressed) presses++;
        }
    }

}